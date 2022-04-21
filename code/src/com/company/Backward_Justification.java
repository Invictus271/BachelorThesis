package com.company;

import java.util.Arrays;

public class Backward_Justification {
    public int[] getBackwardJustifiedOrder() {
        return backwardJustifiedOrder;
    }

    public void setBackwardJustifiedOrder(int[] backwardJustifiedOrder) {
        this.backwardJustifiedOrder = backwardJustifiedOrder;
    }

    public planStep[] getBackwardJustifiedOperators() {
        return backwardJustifiedOperators;
    }

    public void setBackwardJustifiedOperators(planStep[] backwardJustifiedOperators) {
        this.backwardJustifiedOperators = backwardJustifiedOperators;
    }

    public CausalLink[] getBackwardJustifiedCausalLinks() {
        return backwardJustifiedCausalLinks;
    }

    public void setBackwardJustifiedCausalLinks(CausalLink[] backwardJustifiedCausalLinks) {
        this.backwardJustifiedCausalLinks = backwardJustifiedCausalLinks;
    }

    public int[][] getBackwardJustifiedOrderingConstraints() {
        return backwardJustifiedOrderingConstraints;
    }

    public void setBackwardJustifiedOrderingConstraints(int[][] backwardJustifiedOrderingConstraints) {
        this.backwardJustifiedOrderingConstraints = backwardJustifiedOrderingConstraints;
    }

    private int[] backwardJustifiedOrder;
    private planStep[] backwardJustifiedOperators;
    private CausalLink[] backwardJustifiedCausalLinks;
    private int[][] backwardJustifiedOrderingConstraints;
    //reduces if possible a totally ordered plan
    //backwardJustifiedOrder than only contains operators that are backward justified
    public Backward_Justification(planStep[] operators, int[][] oc, long randomSeed){
        setBackwardJustifiedOperators(operators);
        setBackwardJustifiedOrderingConstraints(oc);
        int[] totalOrder=Utillity.createLinearization(operators,oc,randomSeed);
        boolean justified;
        planStep curr;

        for(int i=totalOrder.length-2;i>0;i--){
                curr=operators[totalOrder[i]];

            String currLiteral;
            boolean establish=false;
            boolean deletedOrAlreadyEstablished;
            if(curr.getAddEffects()!=null) {
                for (int n = 0; n < curr.getAddEffects().length; n++) {
                    deletedOrAlreadyEstablished=false;
                    currLiteral = curr.getAddEffects()[n];
                    //boolean literaldeleted = false;
                    for (int k = i+1; k < totalOrder.length; k++) {
                        if (Arrays.stream(operators[totalOrder[k]].getPreconditions()).anyMatch(currLiteral::equals)&&!deletedOrAlreadyEstablished) {
                            establish=true;
                        }
                        if(Arrays.stream(operators[totalOrder[k]].getAddEffects()).anyMatch(currLiteral::equals)){
                                  deletedOrAlreadyEstablished=true;
                        }
                        if (Arrays.stream(operators[totalOrder[k]].getDeleteEffects()).anyMatch(currLiteral::equals)) {
                                   deletedOrAlreadyEstablished=true;
                        }

                    }



                }
            }
            if (!establish) {
                        setBackwardJustifiedOperators(Utillity.removeOperator(operators[totalOrder[i]].getOpNummer(),this.getBackwardJustifiedOperators()));
                        setBackwardJustifiedOrderingConstraints(Utillity.removeConstraintsFromOperator(totalOrder[i],this.getBackwardJustifiedOrderingConstraints()));
                        totalOrder=removeIntElement(totalOrder, i);
            }

        }
        setBackwardJustifiedOrder(totalOrder);
    }
    public int[] removeIntElement(int[] arr, int removedIdx) {
        int[] n = new int[arr.length - 1];
        System.arraycopy(arr, 0, n, 0, removedIdx);
        System.arraycopy(arr, removedIdx+1, n, removedIdx, arr.length - removedIdx-1);
        return n;
    }


}
