package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Well_Justification {
    public planStep[] getWellJustifiedOperators() {
        return wellJustifiedOperators;
    }

    public void setWellJustifiedOperators(planStep[] wellJustifiedOperators) {
        this.wellJustifiedOperators = wellJustifiedOperators;
    }

    public CausalLink[] getWellJustifiedCL() {
        return wellJustifiedCL;
    }

    public void setWellJustifiedCL(CausalLink[] wellJustifiedCL) {
        this.wellJustifiedCL = wellJustifiedCL;
    }

    public int[][] getWellJustifiedOC() {
        return WellJustifiedOC;
    }

    public void setWellJustifiedOC(int[][] WellJustifiedOC) {
        this.WellJustifiedOC = WellJustifiedOC;
    }

    private planStep[] wellJustifiedOperators;
    private CausalLink[] wellJustifiedCL;
    private int[][] WellJustifiedOC;
    //reduces a the operators, causal links and the ordering constraints from a POCL plan
    //in order for the plan to be well justified
    public Well_Justification(planStep[] operators, CausalLink[] cl, int[][] oc,long RandomSeed){
        setWellJustifiedOperators(operators);
        setWellJustifiedCL(cl);
        setWellJustifiedOC(oc);
        //System.out.println(Utillity.checkPOPlan(wellJustifiedOperators,WellJustifiedOC));
        boolean operationGotDeleted=false;
        do{
            operationGotDeleted=false;
            Random rand = new Random(RandomSeed);
            int size = wellJustifiedOperators.length;

            ArrayList<Integer> list = new ArrayList<Integer>(size);
            for(int i = 2; i < size; i++) {
                list.add(i);
            }

            while(list.size() > 0) {
                int index = rand.nextInt(list.size());
                int i=list.remove(index);



            //for (int i = 2; i < operators.length; i++) {
                //tries to remove one operator at a time
                //currOperator represents the opNummer from the observed operator
                int currOperator = i;
                /*for (int z = 0; z < getWellJustifiedOperators().length; z++) {
                    if (operators[i].getLabel().equals(getWellJustifiedOperators()[z].getLabel())) {
                        currOperator = z;
                    }
                }*/

                //removing currOperator from the plan
                if (currOperator != -1) {
                    planStep[] currOperators = Utillity.removeOperator(currOperator, getWellJustifiedOperators());
                    CausalLink[] currCL = Utillity.removeLinksFromOperator(currOperator, getWellJustifiedCL());
                    int[][] currOC = Utillity.removeConstraintsFromOperator(currOperator, WellJustifiedOC);
                    boolean[][] currOrderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length, currOC));
                    ArrayList<OpenPrecondition> openPreconditions = Utillity.openPreconditions(currOperators, currCL);
                    ArrayList<CausalFlaw> causalFlaws = Utillity.searchCasualFlaw(currOperators, currCL, currOrderingMatrix);
                    //checking if plan is still consistent
                    if (openPreconditions.isEmpty() && causalFlaws.isEmpty()) {
                        //if there is no flaw or openPrecondition the plan is well justified without currOperator
                        setWellJustifiedOperators(currOperators);
                        setWellJustifiedCL(currCL);
                        setWellJustifiedOC(currOC);
                        operationGotDeleted = true;
                        Utillity.adjustListNumbers(new ArrayList<Integer>(currOperator),list);
                    } else {
                        //if there are still a open preconditions new causal links need to be added
                        if (!openPreconditions.isEmpty()) {
                            //searching fitting causal links
                            for (int n = 0; n < openPreconditions.size(); n++) {
                                for (int k = 0; k < openPreconditions.get(n).getOpenLiterals().length; k++) {
                                    for (int t = 0; t < currOperators.length; t++) {
                                        if (Utillity.contains(openPreconditions.get(n).getOpenLiterals()[k], currOperators[t].getAddEffects())) {
                                            if (currOrderingMatrix[t][openPreconditions.get(n).getOperator().getOpNummer()]) {
                                                CausalLink[] newLinks = Utillity.addCausalLink(new CausalLink(openPreconditions.get(n).getOperator().getOpNummer(), t, openPreconditions.get(n).getOpenLiterals()[k]), currCL);
                                                int[] newConstraint = {t,openPreconditions.get(n).getOperator().getOpNummer()};
                                                int[][] newConstraints = Utillity.addConstraint(newConstraint, currOC);
                                                if (Utillity.searchCasualFlaw(currOperators, newLinks, currOrderingMatrix).isEmpty()) {
                                                    currOC = newConstraints;
                                                    currCL = newLinks;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //if there are still open preconditions currOp is wellJustified and is not getting removed
                        if (Utillity.openPreconditions(currOperators, currCL).isEmpty()) {
                            //if all preconditions can be satisfied with the new causal links
                            //currOp can be deleted from the plan
                            setWellJustifiedOperators(currOperators);
                            setWellJustifiedCL(currCL);
                            setWellJustifiedOC(currOC);
                            operationGotDeleted = true;
                            Utillity.adjustListNumbers(new ArrayList<Integer>(currOperator),list);
                        }else{
                            Utillity.resetOpNumbers(currOperators,wellJustifiedOperators);
                        }
                    }
                }
            }
        }while (operationGotDeleted);
        /*for(int i=0;i<wellJustifiedOperators.length;i++){
            System.out.println(wellJustifiedOperators[i].getOpNummer()+" "+wellJustifiedOperators[i].getLabel());
        }
        System.out.println(Utillity.checkPOPlan(wellJustifiedOperators,WellJustifiedOC));
        System.out.println(Utillity.checkPOCLPlan(wellJustifiedOperators,wellJustifiedCL,WellJustifiedOC));*/
        Utillity.resetOpNumbers(wellJustifiedOperators,operators);
    }
    public Well_Justification(planStep[] operators, int[][] oc, long RandomSeed){
        setWellJustifiedOperators(operators);
        setWellJustifiedOC(oc);
        boolean operationGotDeleted=false;
        do {
            operationGotDeleted=false;
            Random rand = new Random(RandomSeed);
            int size = wellJustifiedOperators.length;

            ArrayList<Integer> list = new ArrayList<Integer>(size);
            for(int i = 2; i < size; i++) {
                list.add(i);
            }

            while(list.size() > 0) {
                int index = rand.nextInt(list.size());
                int i=list.remove(index);



            //for (int i = 2; i < operators.length; i++) {
                //tries to remove one operator at a time
                //currOperator represents the opNummer from the observed operator
                boolean planIsConsistent = true;
                int currOperator = i;
                /*for (int z = 0; z < getWellJustifiedOperators().length; z++) {
                    if (operators[i].getLabel().equals(getWellJustifiedOperators()[z].getLabel())) {
                        currOperator = z;
                    }
                }*/
                //removing currOperator from the plan
                if(currOperator!=-1) {
                    planStep[] currOperators = Utillity.removeOperator(currOperator, getWellJustifiedOperators());
                    int[][] currOC = Utillity.removeConstraintsFromOperator(currOperator, getWellJustifiedOC());
                    boolean[][] currOrderingMatrix = Utillity.createTransitiveClosure(Utillity.createOrderingMatrix(currOperators.length, currOC));

                    ArrayList<OpenPrecondition> openPreconditions = Utillity.openPreconditions(currOperators, currOrderingMatrix);
                    //ArrayList<OrderingFlaw> orderingFlaws = Utillity.searchOrderingFlaws(currOperators, currOrderingMatrix);
                    //System.out.println(orderingFlaws.size());
                    if (openPreconditions.isEmpty()) {
                        operationGotDeleted = true;
                        setWellJustifiedOC(currOC);
                        setWellJustifiedOperators(currOperators);
                        Utillity.adjustListNumbers(new ArrayList<>(currOperator),list);
                    }else{
                        Utillity.resetOpNumbers(currOperators,wellJustifiedOperators);
                    }
                }
            }
        }while(operationGotDeleted);
        Utillity.resetOpNumbers(wellJustifiedOperators,operators);
        /*for(int i=0;i<wellJustifiedOperators.length;i++){
            System.out.println(wellJustifiedOperators[i].getOpNummer()+" "+wellJustifiedOperators[i].getLabel());
        }
        System.out.println(Utillity.checkPOPlan(wellJustifiedOperators,WellJustifiedOC));*/
    }
}



