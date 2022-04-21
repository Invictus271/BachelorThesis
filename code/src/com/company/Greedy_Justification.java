package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Greedy_Justification {
    public planStep[] getGreedyJustifiedOperators() {
        return greedyJustifiedOperators;
    }

    public void setGreedyJustifiedOperators(planStep[] greedyJustifiedOperators) {
        this.greedyJustifiedOperators = greedyJustifiedOperators;
    }

    public CausalLink[] getGreedyJustifiedCausalLinks() {
        return greedyJustifiedCausalLinks;
    }

    public void setGreedyJustifiedCausalLinks(CausalLink[] greedyJustifiedCausalLinks) {
        this.greedyJustifiedCausalLinks = greedyJustifiedCausalLinks;
    }

    public int[][] getGreedyJustifiedOrderingConstraints() {
        return greedyJustifiedOrderingConstraints;
    }

    public void setGreedyJustifiedOrderingConstraints(int[][] greedyJustifiedOrderingConstraints) {
        this.greedyJustifiedOrderingConstraints = greedyJustifiedOrderingConstraints;
    }

    private planStep[] greedyJustifiedOperators;
    private CausalLink[] greedyJustifiedCausalLinks;
    private int[][] greedyJustifiedOrderingConstraints;
    //removes all operators, causal links and ordering constraints from a POCL plan,
    //which are not greedily justified
    public Greedy_Justification(planStep[] operators, CausalLink[] cl, int[][] oc, long Randomseed){
        setGreedyJustifiedOperators(operators);
        setGreedyJustifiedCausalLinks(cl);
        setGreedyJustifiedOrderingConstraints(oc);
        int size = getGreedyJustifiedOperators().length;

        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for(int i = 2; i < size; i++) {
            list.add(i);
        }

        Random rand = new Random(Randomseed);
        while(list.size() > 0) {
            int index = rand.nextInt(list.size());
            int i=list.remove(index);

           int currOperator=i;
            ArrayList<Integer> rmOperators = new ArrayList<Integer>();
            if(currOperator!=-1){
                //remove currOp from the plan
                planStep[] currOperators=Utillity.removeOperator(currOperator,getGreedyJustifiedOperators());
                rmOperators.add(currOperator);
                CausalLink[] currCL=Utillity.removeLinksFromOperator(currOperator,getGreedyJustifiedCausalLinks());
                int[][] currOC=Utillity.removeConstraintsFromOperator(currOperator,getGreedyJustifiedOrderingConstraints());
                boolean[][] currOrderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length,currOC));
                //create the sets of illeagal operators and earliest illegal operators
                ArrayList<planStep> illegals=searchIllegals(currOperators,currCL,currOC);
                ArrayList<planStep> earliestIllegals=searchEarliestIllegals(currOperators,currOC,currCL,illegals);
                while (!illegals.isEmpty()){

                    //remove all illegal operators from the plan
                    //starting by removing the earliest illegals
                    //after removing the earliest operators
                    // the set of illegals and earliest illeges
                    //are getting updated
                    earliestIllegals=searchEarliestIllegals(currOperators,currOC,currCL,illegals);
                    for(int n=0;n<earliestIllegals.size();n++){
                        rmOperators.add(earliestIllegals.get(n).getOpNummer());
                        currOperators=Utillity.removeOperator(earliestIllegals.get(n).getOpNummer(),currOperators);
                        currCL=Utillity.removeLinksFromOperator(earliestIllegals.get(n).getOpNummer(),currCL);
                        currOC=Utillity.removeConstraintsFromOperator(earliestIllegals.get(n).getOpNummer(),currOC);
                    }
                    illegals=searchIllegals(currOperators,currCL,currOC);
                }
                //check if plan is still consistent
                currOrderingMatrix=Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length,currOC));
                ArrayList<OpenPrecondition> openPreconditions= Utillity.openPreconditions(currOperators,currCL);
                ArrayList<CausalFlaw> causalFlaws= Utillity.searchCasualFlaw(currOperators,currCL,currOrderingMatrix);
                if(openPreconditions.isEmpty() && causalFlaws.isEmpty()){
                    //if there is no open precondition or causalFlaw the plan is consistent
                    //and currOp and all related illegals are not greedily justified
                    setGreedyJustifiedOperators(currOperators);
                    setGreedyJustifiedCausalLinks(currCL);
                    setGreedyJustifiedOrderingConstraints(currOC);
                    Utillity.adjustListNumbers(rmOperators,list);
                }else {
                    //if there are still open preconditions new causal links need to be searched
                    if (!openPreconditions.isEmpty()) {
                        for (int n = 0; n < openPreconditions.size(); n++) {
                            for (int k = 0; k < openPreconditions.get(n).getOpenLiterals().length; k++) {
                                for (int t = 0; t < currOperators.length; t++) {
                                    if (Utillity.contains(openPreconditions.get(n).getOpenLiterals()[k], currOperators[t].getAddEffects())) {
                                        if (currOrderingMatrix[t][openPreconditions.get(n).getOperator().getOpNummer()]) {
                                            CausalLink[] newLinks = Utillity.addCausalLink(new CausalLink(openPreconditions.get(n).getOperator().getOpNummer(), t, openPreconditions.get(n).getOpenLiterals()[k]), currCL);
                                            int[] newConstraint = {t,openPreconditions.get(n).getOperator().getOpNummer()};
                                            int[][] newConstraints = Utillity.addConstraint(newConstraint, currOC);
                                            boolean [][] newOrderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length,newConstraints));
                                            if (Utillity.searchCasualFlaw(currOperators, newLinks, newOrderingMatrix).isEmpty()) {
                                                currOC = newConstraints;
                                                currCL = newLinks;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //System.out.println(Utillity.openPreconditions(currOperators,currOrderingMatrix).isEmpty());
                    //System.out.println(Utillity.openPreconditions(currOperators,currCL).isEmpty());
                    //if there are still open preconditions currOp is greedily justified
                    if (Utillity.openPreconditions(currOperators, currCL).isEmpty()) {
                        //if the new causal links satisfy all open preconditions
                        //currOp and all related illegals are getting removed
                        setGreedyJustifiedOperators(currOperators);
                        setGreedyJustifiedCausalLinks(currCL);
                        setGreedyJustifiedOrderingConstraints(currOC);
                        Utillity.adjustListNumbers(rmOperators,list);
                    }else{
                        Utillity.resetOpNumbers(currOperators,getGreedyJustifiedOperators());
                    }
                }
            }
        }
        Utillity.resetOpNumbers(greedyJustifiedOperators,operators);
        //System.out.println(Utillity.checkPOPlan(greedyJustifiedOperators,greedyJustifiedOrderingConstraints));
        //System.out.println(Utillity.checkPOCLPlan(greedyJustifiedOperators,greedyJustifiedCausalLinks,greedyJustifiedOrderingConstraints));
        for(int i=0;i<getGreedyJustifiedOperators().length;i++){
            //System.out.println(getGreedyJustifiedOperators()[i].getOpNummer()+" "+getGreedyJustifiedOperators()[i].getLabel());
        }

        System.out.println(getGreedyJustifiedOperators().length);
    }
    public Greedy_Justification(planStep[] operators, int[][] oc, long Randomseed){
        setGreedyJustifiedOperators(operators);
        setGreedyJustifiedOrderingConstraints(oc);
        int size = operators.length;

        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for(int i = 2; i < size; i++) {
            list.add(i);
        }

        Random rand = new Random(Randomseed);
        while(list.size() > 0) {
            int index = rand.nextInt(list.size());
            int i=list.remove(index);
        //for(int i=2;i<operators.length;i++){
            //remove one operator at a time
            //if currOp is already deleted currOperator is set to -1
            int currOperator=i;
            /*for(int z=0;z<getGreedyJustifiedOperators().length;z++){
                if(operators[i].getLabel().equals(getGreedyJustifiedOperators()[z].getLabel())){
                    currOperator=z;
                }
            }*/
            ArrayList<Integer> rmOperators = new ArrayList<Integer>();
            if(currOperator!=-1) {
                //remove currOp from the plan
                planStep[] currOperators = Utillity.removeOperator(currOperator, getGreedyJustifiedOperators());
                rmOperators.add(currOperator);
                int[][] currOC = Utillity.removeConstraintsFromOperator(currOperator, getGreedyJustifiedOrderingConstraints());
                boolean[][] currOrderingMatrix= Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length,currOC));
                //create the sets of illeagal operators and earliest illegal operators
                ArrayList<planStep> illegals = searchIllegals(currOperators, currOrderingMatrix);
                ArrayList<planStep> earliestIllegals;
                while (!illegals.isEmpty()) {
                    //remove all illegal operators from the plan
                    //starting by removing the earliest illegals
                    //after removing the earliest operators
                    // the set of illegals and earliest illeges
                    //are getting updated
                    earliestIllegals = searchEarliestIllegals(currOperators, currOC, illegals);
                    for (int n = 0; n < earliestIllegals.size(); n++) {
                        rmOperators.add(earliestIllegals.get(n).getOpNummer());
                        currOperators = Utillity.removeOperator(earliestIllegals.get(n).getOpNummer(), currOperators);
                        currOC = Utillity.removeConstraintsFromOperator(earliestIllegals.get(n).getOpNummer(), currOC);
                        currOrderingMatrix=Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length,currOC));
                    }
                    illegals = searchIllegals(currOperators, currOrderingMatrix);
                }
                ArrayList<OpenPrecondition> openPreconditions=Utillity.openPreconditions(currOperators,currOrderingMatrix);
                if(openPreconditions.isEmpty()){
                    Utillity.adjustListNumbers(rmOperators,list);
                    setGreedyJustifiedOrderingConstraints(currOC);
                    setGreedyJustifiedOperators(currOperators);
                }else{
                    Utillity.resetOpNumbers(currOperators,getGreedyJustifiedOperators());
                }
            }
        }
        Utillity.resetOpNumbers(getGreedyJustifiedOperators(),operators);
        //System.out.println(Utillity.checkPOPlan(greedyJustifiedOperators,greedyJustifiedOrderingConstraints));
        //System.out.println(Utillity.checkPOCLPlan(greedyJustifiedOperators,greedyJustifiedCausalLinks,greedyJustifiedOrderingConstraints));
        for(int i=0;i<getGreedyJustifiedOperators().length;i++){
            //System.out.println(getGreedyJustifiedOperators()[i].getOpNummer()+" "+getGreedyJustifiedOperators()[i].getLabel());
        }
        //System.out.println(getGreedyJustifiedOperators().length);
    }
    public ArrayList<planStep> searchIllegals(planStep[] operators, boolean[][] orderingMatrix){
        ArrayList<planStep> illegals = new ArrayList<planStep>();
        ArrayList<OpenPrecondition> openPreconditions = Utillity.openPreconditions(operators,orderingMatrix);
        ArrayList<OrderingFlaw> orderingFlaws =Utillity.searchOrderingFlaws(operators,orderingMatrix);
        for(int i=2;i<operators.length;i++){
            for(int k=0;k<openPreconditions.size();k++){
               if(operators[i].getOpNummer()==openPreconditions.get(k).getOperator().getOpNummer()){
                   illegals.add(operators[i]);
               }
            }
            /*if(!isAdded){
                for(int k=0;k<orderingFlaws.size();k++){
                    if(operators[i].getLabel().equals(orderingFlaws.get(k).getOperator().getLabel())){
                        System.out.println("Ordering Flaw");
                        illegals.add(operators[i]);
                    }
                }
            }*/
        }
        return illegals;
    }
    //creates a ordering matrix for all illegal operators and adds all operators with no predecessor
    //in this Matrix to the list of earliest Illegals
    public ArrayList<planStep> searchEarliestIllegals(planStep[] operators, int[][] oc ,ArrayList<planStep> illegals){

        ArrayList<planStep> earliestIllegals= new ArrayList<planStep>();
        if(illegals.isEmpty()){
            return earliestIllegals;
        }
        int[][] illegalsOC = oc.clone();
        int currRemoveNummer=0;
        boolean dontRemove=false;
        for(int i=0;i<operators.length;i++){
            dontRemove=false;
            for(int n=0;n<illegals.size();n++){
                if(illegals.get(n).getOpNummer()==i){
                    dontRemove=true;
                }
            }
            if(!dontRemove){
                    illegalsOC=Utillity.removeConstraintsFromOperator(currRemoveNummer,illegalsOC);
            }
            if(dontRemove){
                currRemoveNummer++;
            }
        }
        boolean[][] illegalsOrderingMatrix=Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(illegals.size()+2,illegalsOC));
        boolean isEarlyIllegal;
        for(int i=0;i<illegals.size();i++){
            isEarlyIllegal=true;
            for(int n=0;n<illegals.size();n++){
                if(illegalsOrderingMatrix[n][i]){
                    isEarlyIllegal=false;
                }
            }
            if(isEarlyIllegal){
                earliestIllegals.add(illegals.get(i));
            }
        }
        return earliestIllegals;
    }
    public ArrayList<planStep> searchEarliestIllegals(planStep[] operators, int[][] oc, CausalLink[] cl,ArrayList<planStep> illegals){

        ArrayList<planStep> earliestIllegals= new ArrayList<planStep>();
        if(illegals.isEmpty()){
            return earliestIllegals;
        }
        int[][] illegalsOC = oc.clone();
        CausalLink[] illegalsCL = cl.clone();
        int currRemoveNummer=0;
        boolean dontRemove=false;
        for(int i=0;i<operators.length;i++){
            dontRemove=false;
            for(int n=0;n<illegals.size();n++){
                if(illegals.get(n).getOpNummer()==i){
                    dontRemove=true;
                }
            }
            if(!dontRemove){
                    illegalsOC=Utillity.removeConstraintsFromOperator(currRemoveNummer,illegalsOC);
                    illegalsCL=Utillity.removeLinksFromOperator(currRemoveNummer,illegalsCL);
            }
            if(dontRemove){
                currRemoveNummer++;
            }
        }
        boolean[][] illegalsOrderingMatrix=Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(illegals.size()+2,illegalsOC));
        boolean isEarlyIllegal;
        for(int i=0;i<illegals.size();i++){
            isEarlyIllegal=true;
            for(int n=0;n<illegals.size();n++){
                if(illegalsOrderingMatrix[n][i]){
                    for(int t=0;t<illegalsCL.length;t++){
                        if(illegalsCL[t].getConsumer()==i){
                            isEarlyIllegal=false;
                        }
                    }
                }
            }
            if(isEarlyIllegal){
                earliestIllegals.add(illegals.get(i));
            }
        }
        return earliestIllegals;
    }
    //returns all operators with open preconditions to the list of illegal operators
    public ArrayList<planStep> searchIllegals(planStep[] operators, CausalLink[] cl,int[][] oc){
        ArrayList<planStep> illegals = new ArrayList<planStep>();
        ArrayList<OpenPrecondition> openPreconditions=Utillity.openPreconditions(operators,cl);
        boolean[][] currOrderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(operators.length,oc));
        if (!openPreconditions.isEmpty()){
             for (int n = 0; n < openPreconditions.size(); n++) {
                 for (int k = 0; k < openPreconditions.get(n).getOpenLiterals().length; k++) {
                     for (int t = 0; t < operators.length; t++) {
                         if (Utillity.contains(openPreconditions.get(n).getOpenLiterals()[k], operators[t].getAddEffects())) {
                             if (currOrderingMatrix[t][openPreconditions.get(n).getOperator().getOpNummer()]) {
                                 CausalLink[] newLinks = Utillity.addCausalLink(new CausalLink(openPreconditions.get(n).getOperator().getOpNummer(), t, openPreconditions.get(n).getOpenLiterals()[k]), cl);
                                 int[] newConstraint = {openPreconditions.get(n).getOperator().getOpNummer(), t};
                                 int[][] newConstraints = Utillity.addConstraint(newConstraint, oc);
                                 if (Utillity.searchCasualFlaw(operators, newLinks, currOrderingMatrix).isEmpty()) {
                                     oc = newConstraints;
                                     cl = newLinks;
                                 }
                             }
                         }
                     }
                 }
             }
        }
        openPreconditions=Utillity.openPreconditions(operators,cl);
        for(int i=0;i<openPreconditions.size();i++){
            if(!openPreconditions.get(i).getOperator().getType().equals(Steptype.GOAL)) {
                illegals.add(openPreconditions.get(i).getOperator());
            }
        }
        return illegals;
    }
}
