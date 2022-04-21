package com.company;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class Utillity {
    public Utillity(){
    }
    public static boolean checkPOCLPlan(planStep[] operators, CausalLink[] causalLinks, int[][] oc){
        boolean[][] orderingMatrix= createTransitiveClosure(createOrderingMatrix(operators.length,oc));
        if(openPreconditions(operators,causalLinks).isEmpty() && searchCasualFlaw(operators,causalLinks,orderingMatrix).isEmpty()){
            return true;
        }
        else {return false;}
    }
    public static boolean checkPOPlan(planStep[] operators, int[][] oc){
        boolean[][] orderingMatrix= createTransitiveClosure(createOrderingMatrix(operators.length,oc));
        if(openPreconditions(operators,orderingMatrix).isEmpty()){
            return true;
        }
        else {return false;}
    }

    public static int[] createLinearization(planStep[] operators, int[][] oc,long randomSeed){
        ArrayList<Integer> linearization = new ArrayList<Integer>();
        linearization.add(0);
        Random generator = new Random(randomSeed);
        boolean reachedGoal=false;
        while(!reachedGoal){
            ArrayList<Integer> possibleSuccessors=possibleSuccessor(linearization,operators,oc);
            linearization.add(possibleSuccessors.get(generator.nextInt(possibleSuccessors.size())));
            if(linearization.get(linearization.size()-1)==1){
                reachedGoal=true;
            }
        }
        int[] linearizationArray = new int[linearization.size()];
        for(int i=0;i<linearization.size();i++){
            linearizationArray[i]=linearization.get(i);
            //System.out.println(linearization.get(i));
        }
        return linearizationArray;
    }
    public static ArrayList<Integer> possibleSuccessor(ArrayList<Integer> linearization, planStep[] operators, int[][] oc){
        ArrayList<Integer> possiblesuccessor =new ArrayList<Integer>();
        boolean operatorIsValid;
        for(int i=1;i<operators.length;i++) {
            operatorIsValid = true;
            if (linearization.contains(i)) {
                operatorIsValid = false;
            } else {
                for (int n = 0; n < oc.length; n++) {
                   if(oc[n][0]==i && linearization.contains(oc[n][1])){
                       operatorIsValid=false;
                   }
                   if (oc[n][1] == i && !linearization.contains(oc[n][0])) {
                        operatorIsValid=false;
                }
            }
        }
            if (operatorIsValid) {
                possiblesuccessor.add(i);
            }
        }
        return possiblesuccessor;
    }
    /*public static int calculateMakespan(planStep[] operators, int[][] oc) {
        ArrayList<Integer> start = new ArrayList<Integer>();
        start.add(0);
        ArrayList<ArrayList<Integer>> fringe= new ArrayList<ArrayList<Integer>>();
        fringe.add(start);
        while (!fringe.isEmpty()){
            ArrayList<Integer>  node= fringe.get(0);
            fringe.remove(0);
            if (node.get(node.size()-1) == 1) {
                return node.size();
            }
            ArrayList<ArrayList<Integer>> children =new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> possibleSuccessors=possibleSuccessor(node,operators,oc);
            for(int i=0;i<possibleSuccessors.size();i++){
                ArrayList<Integer> newChild= (ArrayList<Integer>) node.clone();
                newChild.add(possibleSuccessors.get(i));
                children.add(newChild);
            }
            for(int i=0;i<children.size();i++){
                fringe.add(children.get(i));
            }

        }
        return -1;
    }*/

    public static int calculateMakespan(planStep[] operators, int[][] oc, CausalLink[] cl){
        int[][] currOC=oc.clone();
        CausalLink[] currCl=cl.clone();
        boolean[][] orderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(operators.length,currOC));
        int[] r = new int[operators.length];
        ArrayList<Integer> remainingOperators = new ArrayList<Integer>();
        ArrayList<Integer> remainingOperatorsOldLabel = new ArrayList<Integer>();
        for(int i=0;i<operators.length;i++){
            r[i]=0;
            remainingOperators.add(i);
            remainingOperatorsOldLabel.add(i);
        }
        //int counter=0;
        while(!(remainingOperators.size()<=1)){
            //counter++;
            //System.out.println(counter);
            for(int i=0;i<remainingOperators.size();i++){
                int a=remainingOperators.get(i);
                boolean hasPredecessor=false;
                for(int k=0;k<currOC.length;k++){
                    if(currOC[k][1]==a){
                        for(int n=0;n< currCl.length;n++){
                            if(currCl[n].getConsumer()==a){
                                hasPredecessor=true;
                            }
                        }
                    }
                }
                if(!hasPredecessor){
                    for(int b=0;b<remainingOperators.size();b++){
                        if(orderingMatrix[a][b]){
                            r[remainingOperatorsOldLabel.get(b)]=Integer.max(r[remainingOperatorsOldLabel.get(b)],r[remainingOperatorsOldLabel.get(a)]+1);
                        }

                    }
                    remainingOperators.remove(a);
                    remainingOperatorsOldLabel.remove(a);
                    for(int n=0;n<remainingOperators.size();n++){
                        if(remainingOperators.get(n)>a){
                            remainingOperators.set(n,remainingOperators.get(n)-1);
                        }
                    }
                    currCl=Utillity.removeLinksFromOperator(a,currCl);
                    currOC=Utillity.removeConstraintsFromOperator(a,currOC);
                    orderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(remainingOperators.size(),currOC));
                }
            }
        }
        return r[1];
    }
    public static int calculateMakespan(planStep[] operators, int[][] oc){
        int[][] currOC=oc.clone();
        boolean[][] orderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(operators.length,currOC));
        int[] r = new int[operators.length];
        ArrayList<Integer> remainingOperators = new ArrayList<Integer>();
        ArrayList<Integer> remainingOperatorsOldLabel = new ArrayList<Integer>();
        for(int i=0;i<operators.length;i++){
            r[i]=0;
            remainingOperators.add(i);
            remainingOperatorsOldLabel.add(i);
        }
        //int counter=0;
        while(!(remainingOperators.size()<=1)){
            //counter++;
            //System.out.println(counter);
            for(int i=0;i<remainingOperators.size();i++){
                int a=remainingOperators.get(i);
                boolean hasPredecessor=false;
                for(int k=0;k<currOC.length;k++){
                    if(currOC[k][1]==a){
                        hasPredecessor=true;
                    }
                }
                if(!hasPredecessor){
                    for(int b=0;b<remainingOperators.size();b++){
                        if(orderingMatrix[a][b]){
                            r[remainingOperatorsOldLabel.get(b)]=Integer.max(r[remainingOperatorsOldLabel.get(b)],r[remainingOperatorsOldLabel.get(a)]+1);
                        }

                    }
                    remainingOperators.remove(a);
                    remainingOperatorsOldLabel.remove(a);
                    for(int n=0;n<remainingOperators.size();n++){
                        if(remainingOperators.get(n)>a){
                            remainingOperators.set(n,remainingOperators.get(n)-1);
                        }
                    }
                    currOC=Utillity.removeConstraintsFromOperator(a,currOC);
                    orderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(remainingOperators.size(),currOC));
                }
            }
        }
        return r[1];
    }

    //returns true if all Strings in subSet are also contained in superSet
    public static boolean isSubset(String[] superSet,String[] subSet){
        boolean result=true;
        boolean curr=false;
        for(int i=0;i<subSet.length;i++){
            curr=false;
            for(int n=0;n<superSet.length;n++){
                if(superSet[n].equals(subSet[i])){
                    curr=true;
                }
            }
            if(!curr){
                result=false;
            }
        }
        return result;
    }
    //returns true if array contains the element
    public static boolean contains(String element, String[] array){
        for(int i=0;i<array.length;i++){
            if(array[i].equals(element)){
                return true;
            }
        }
        return false;
    }
    //returns true if the given total order satisfies the goal
    public static boolean testTotalOrder(int planStepsCount, planStep init,planStep goal, planStep[] operators, int[] totalOrder){
        ArrayList<String> currLiterals = new ArrayList<String>();
        for(int i=0;i<init.getAddEffects().length;i++){
            currLiterals.add(init.getAddEffects()[i]);
        }
        //for every operator, add and delete effects are being applied
        for(int i=1;i<totalOrder.length-1;i++){
            String[] currLitArray = new String[currLiterals.size()];
            currLitArray=currLiterals.toArray(currLitArray);
            if(isSubset(currLitArray,operators[totalOrder[i]].getPreconditions())){
                for(int n=0;n<operators[totalOrder[i]].getAddEffects().length;n++){
                   currLiterals.add(operators[totalOrder[i]].getAddEffects()[n]);
                }
                for(int n=0;n<operators[totalOrder[i]].getDeleteEffects().length;n++){
                    if(currLiterals.contains(asList(operators[totalOrder[i]].getDeleteEffects()).get(n))){
                        currLiterals.remove(operators[totalOrder[i]].getDeleteEffects()[n]);
                    }
                }
            }else{
                //fails if precondition of operator i is not satisfied
                System.out.println("Failed at: "+operators[totalOrder[i]].getLabel()+ "At occurence: "+ i);
                return false;

            }
        }
        if(currLiterals.retainAll(asList(goal.getPreconditions()))){
            return true;
        }

        return false;
    }
    //returns a List of Precondition with their related operators, which are not protected by a causal link
    public static ArrayList<OpenPrecondition> openPreconditions(planStep[] operators, CausalLink[] cl){
        ArrayList<OpenPrecondition> openPreconditions = new ArrayList<OpenPrecondition>();
        //add all Preconditions to openPrecondtions
        for(int i=0; i<operators.length;i++){
            if(!operators[i].getPreconditions()[0].equals("")) {
                openPreconditions.add(new OpenPrecondition(operators[i].getPreconditions().clone(),operators[i]));
            }else{
                openPreconditions.add(new OpenPrecondition(new String[1],operators[i]));
            }
        }
        //delete every literal that is protected
        for(int i=0;i<cl.length;i++){
            for(int n=0;n<openPreconditions.get(cl[i].getConsumer()).getOpenLiterals().length;n++){
                if(openPreconditions.get(cl[i].getConsumer()).getOpenLiterals()[n]!=null && openPreconditions.get(cl[i].getConsumer()).getOpenLiterals()[n].equals(cl[i].getLiteral())){
                    openPreconditions.get(cl[i].getConsumer()).getOpenLiterals()[n]=null;
                }
            }
        }
        //delete preconditions with no unprotected literal
        boolean isEmpty=true;
        int openPreconditionsSize=openPreconditions.size();
        int removedCounter=0;
        for(int i=0;i<openPreconditionsSize;i++){
            isEmpty=true;
            for(int n=0;n<openPreconditions.get(i-removedCounter).getOpenLiterals().length;n++){
                if(openPreconditions.get(i-removedCounter).getOpenLiterals()[n] != null) {
                    isEmpty = false;
                }
            }
            if(isEmpty){
                    openPreconditions.remove(i-removedCounter);
                    removedCounter++;
                }
        }
        return openPreconditions;
    }
    public static ArrayList<OpenPrecondition> openPreconditions(planStep[] operators, boolean[][] orderingMatrix) {
        ArrayList<OpenPrecondition> openPrecondition = new ArrayList<OpenPrecondition>();
        boolean currLiteralIsCreated;
        for (int n = 0; n < operators.length; n++) {
            ArrayList<String> failedLiterals=new ArrayList<String>();
            for (int k = 0; k < operators[n].getPreconditions().length; k++) {
                currLiteralIsCreated = false;
                String currLiteral=operators[n].getPreconditions()[k];
                for (int t = 0; t < operators.length; t++) {
                    if (orderingMatrix[t][n]) {
                        for (int h = 0; h < operators[t].getAddEffects().length; h++) {

                            if (operators[n].getPreconditions()[k].equals(operators[t].getAddEffects()[h])) {
                                currLiteralIsCreated = true;
                            }

                        }
                        if (currLiteralIsCreated) {
                            for (int x = 0; x < operators.length; x++) {
                                for (int h = 0; h < operators[x].getDeleteEffects().length; h++) {
                                    if (orderingMatrix[t][x] && orderingMatrix[x][n]) {
                                        if (operators[n].getPreconditions()[k].equals(operators[x].getDeleteEffects()[h])) {
                                            currLiteralIsCreated = false;
                                            for(int y=0;y<operators.length;y++){
                                                if(orderingMatrix[y][n] && orderingMatrix[x][y]){
                                                    for(int m=0;m<operators[y].getAddEffects().length;m++)
                                                        if(operators[y].getAddEffects()[m].equals(operators[x].getDeleteEffects()[h])){
                                                            currLiteralIsCreated = true;
                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!currLiteralIsCreated && !operators[n].getPreconditions()[0].equals("")) {
                    failedLiterals.add(operators[n].getPreconditions()[k]);
                }
            }
            if(!failedLiterals.isEmpty()){
                String[]failedLiteralsArray=new String[failedLiterals.size()];
                for(int z=0;z<failedLiterals.size();z++){
                    failedLiteralsArray[z]=failedLiterals.get(z);
                }
                openPrecondition.add(new OpenPrecondition(failedLiteralsArray,operators[n]));
            }
        }
        return openPrecondition;
    }

    public static ArrayList<OrderingFlaw> searchOrderingFlaws(planStep[] operators, boolean[][] orderingMatrix){
        ArrayList<OrderingFlaw> orderingFlaws = new ArrayList<OrderingFlaw>();
        for(int n=0;n<operators.length;n++){
                    for(int k=0;k<operators[n].getPreconditions().length;k++){
                        for(int t=0;t<operators.length;t++){
                            if(!orderingMatrix[n][t] && !orderingMatrix[t][n]&& t!=n){
                                for(int h=0;h<operators[t].getDeleteEffects().length;h++){
                                    if(operators[n].getPreconditions()[k].equals(operators[t].getDeleteEffects()[h])){
                                        orderingFlaws.add(new OrderingFlaw(operators[t],operators[n],operators[n].getPreconditions()[k]));
                                    }
                                }

                            }
                        }
                    }
                }
        return orderingFlaws;
    }

    //Parses the ordering constraints into a Matrix with orderingMatrix[2][4]=true meaning
    //operator 2 has to be executed before operator 4
    public static boolean[][] createOrderingMatrix(int operatorCount, int[][] oc){
        boolean [][] orderingMatrix=new boolean[operatorCount][operatorCount];
        for(int i=0;i<oc.length;i++){
            orderingMatrix[oc[i][0]][oc[i][1]]=true;
        }
        return orderingMatrix;
    }
    //adds a transitive closure to an ordering Matrix e.g. if orderingMatrix[2][4]=true
    //and orderingMatrix[4][5]=true => orderingMatrix[2][5]=true;
    public static boolean[][] createTransitiveClosure(boolean[][] orderingMatrix){
        for(int k=0;k<orderingMatrix[0].length;k++){
            for(int i=0;i<orderingMatrix[0].length;i++){
                for(int j=0;j<orderingMatrix[0].length;j++){
                    if(!orderingMatrix[i][j] && orderingMatrix[i][k] && orderingMatrix[k][j]){
                        orderingMatrix[i][j]=true;
                    }
                }
            }
        }
        return orderingMatrix;
    }
    /*searches pairs of causal links and operators that represent a causal flaw in a given POCL plan,
    if operator 4 can delete a Literal that is protected by a causal link, they are getting added to the
    returned list of causal flaws
    */
    public static ArrayList<CausalFlaw> searchCasualFlaw(planStep[] operators, CausalLink[] cl, boolean[][] orderingMatrix){
        ArrayList<CausalFlaw> causalFlaws= new ArrayList<CausalFlaw>();
        ArrayList<Integer> possibleThreat;
        String currLiteral;
        for(int i=0;i<cl.length;i++){
            //search all operators with dangerous delete effects
            currLiteral=cl[i].getLiteral();
            possibleThreat=new ArrayList<Integer>();
            for(int n=0;n<operators.length;n++){
                if(contains(currLiteral,operators[n].getDeleteEffects())) {
                    possibleThreat.add(n);
                }
            }
            //test the ordering constraints of possible threats
            //to see if they can be executed while the threatened link is active
            for(int n=0;n<possibleThreat.size();n++){
                if(!(orderingMatrix[cl[i].getConsumer()][possibleThreat.get(n)] || orderingMatrix[possibleThreat.get(n)][cl[i].getProducer()])){
                    if(possibleThreat.get(n)!=cl[i].getConsumer()) {
                        causalFlaws.add(new CausalFlaw(operators[possibleThreat.get(n)], cl[i]));
                    }
                }
            }
        }
        return causalFlaws;
    }
    //adds a causal link to an array of causal links
    public static CausalLink[] addCausalLink(CausalLink cl,CausalLink[] clArray){
        CausalLink[] newArray=new CausalLink[clArray.length+1];
        for(int i=0;i<clArray.length;i++){
            newArray[i]=clArray[i];
        }
        newArray[clArray.length]=cl;
        return newArray;
    }
    //remove a operator form an array of operators and reordering the opNummer
    public static planStep[] removeOperator(int opNummer,planStep[] operators){
        boolean containsOperator=false;
        for(int i=0;i<operators.length;i++){
            if(operators[i].getOpNummer()==opNummer){
                containsOperator=true;
            }
        }
        if(!containsOperator){
            return operators;
        }
        planStep[] newPlan = new planStep[operators.length-1];
        int newPlanCounter=0;
        for(int i=0;i<operators.length;i++){
            if(!(operators[i].getOpNummer()==opNummer)){
                operators[i].setOpNummer(newPlanCounter);
                newPlan[newPlanCounter]=operators[i];
                newPlanCounter++;
            }
        }
        return newPlan;
    }
    public static void resetOpNumbers(planStep[] currOperators, planStep[] allOperators){

        for(int i=0;i<allOperators.length;i++){
            allOperators[i].setOpNummer(i);
        }

    }
    public static void adjustListNumbers(ArrayList<Integer> rmOperators,ArrayList<Integer> list){
        for(int i=0;i<rmOperators.size();i++){
            for(int n=0;n<list.size();n++){
                if(rmOperators.get(i)==list.get(n)){
                    list.remove(n);
                }
            }
        }
        for(int i=0;i<rmOperators.size();i++){
            for(int n=0;n<list.size();n++){
                if(rmOperators.get(i)<list.get(n)) {
                    list.set(n, list.get(n) - 1);
                }

            }
        }
    }
    //removes a causal links form an array of causal links, that contain a given operator
    //either as a producer or a consumer
    public static CausalLink[] removeLinksFromOperator(int operator, CausalLink[] cl){
        ArrayList<CausalLink> newLinks = new ArrayList<CausalLink>();
        for(int i=0;i<cl.length;i++){
            if(!(cl[i].getConsumer()==operator) && !(cl[i].getProducer()==operator)){
                if(cl[i].getConsumer()>operator && cl[i].getProducer()>operator){
                    newLinks.add(new CausalLink(cl[i].getConsumer()-1,cl[i].getProducer()-1,cl[i].getLiteral()));
                }
                if(cl[i].getProducer()>operator && !(cl[i].getConsumer()>operator)){
                    newLinks.add(new CausalLink(cl[i].getConsumer(),cl[i].getProducer()-1,cl[i].getLiteral()));
                }
                if(!(cl[i].getProducer()>operator) && cl[i].getConsumer()>operator){
                    newLinks.add(new CausalLink(cl[i].getConsumer()-1,cl[i].getProducer(),cl[i].getLiteral()));
                }
                if(!(cl[i].getProducer()>operator) && !(cl[i].getConsumer()>operator)){
                    newLinks.add(new CausalLink(cl[i].getConsumer(),cl[i].getProducer(),cl[i].getLiteral()));
                }
            }
        }
        CausalLink[] newLinksArray = new CausalLink[newLinks.size()];
        for(int i=0;i<newLinks.size();i++){
            newLinksArray[i]=newLinks.get(i);
        }
        return newLinksArray;
    }
    //remove ordering constraints from an array of ordering constraints that, include a given operator
    public static int[][] removeConstraintsFromOperator(int operator, int [][] oc){
        ArrayList<int[]> newConstraints = new ArrayList<int[]>();
        for(int i=0;i<oc.length;i++){
            if(!(oc[i][0]==operator) && !(oc[i][1]==operator)){
                if(oc[i][0]>operator && oc[i][1]>operator){
                    int[] newConstraint={oc[i][0]-1,oc[i][1]-1};
                    newConstraints.add(newConstraint);
                }
                if(oc[i][0]>operator && !(oc[i][1]>operator)){
                    int[] newConstraint={oc[i][0]-1,oc[i][1]};
                    newConstraints.add(newConstraint);
                }
                if(!(oc[i][0]>operator) && oc[i][1]>operator){
                    int[] newConstraint ={oc[i][0],oc[i][1]-1};
                    newConstraints.add(newConstraint);
                }
                 if(!(oc[i][0]>operator) && !(oc[i][1]>operator)){
                    int[] newConstraint ={oc[i][0],oc[i][1]};
                    newConstraints.add(newConstraint);
                }
            }
        }
        int[][] newConstraintsArray = new int[newConstraints.size()][2];
        for(int i=0;i<newConstraints.size();i++){
            newConstraintsArray[i]=newConstraints.get(i);
        }
        return newConstraintsArray;
    }
    //returns array with added ordering constraint element
    public static int[][] addConstraint(int[] element, int[][] constraints){
        int[][] newConstraints= new int[constraints.length+1][2];
        for(int i=0;i<constraints.length;i++){
            newConstraints[i][0]=constraints[i][0];
            newConstraints[i][1]=constraints[i][1];
        }
        newConstraints[constraints.length]=element;
        return newConstraints;
    }
    //test if a POCL plan is consistent
    public static boolean subPlanTest(planStep[] operators,CausalLink[] cl,boolean[][] orderingMatrix){
        ArrayList<OpenPrecondition> openPreconditions = Utillity.openPreconditions(operators,cl);
        ArrayList<CausalFlaw> causalFlaws = Utillity.searchCasualFlaw(operators,cl,orderingMatrix);
        if(openPreconditions.isEmpty() && causalFlaws.isEmpty()){
            return true;
        }else{}
        return false;
    }
}


