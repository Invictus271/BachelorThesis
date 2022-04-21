package com.company;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class Backward_Justifiation {
    public int[] getBackwardJustifiedOrder() {
        return backwardJustifiedOrder;
    }

    public void setBackwardJustifiedOrder(int[] backwardJustifiedOrder) {
        this.backwardJustifiedOrder = backwardJustifiedOrder;
    }

    private int[] backwardJustifiedOrder;
    //reduces if possible a totally ordered plan
    //backwardJustifiedOrder than only contains operators that are backward justified
    public Backward_Justifiation(int planStepsCount, planStep init,planStep goal, planStep[] operators, int[] totalOrder){
        boolean justified;
        planStep curr;

        for(int i=totalOrder.length-2;i>0;i--){
                curr=operators[totalOrder[i]];

            String currLiteral;
            if(curr.getAddEffects()!=null) {
                for (int n = 0; n < curr.getAddEffects().length; n++) {
                    currLiteral = curr.getAddEffects()[n];
                    boolean establish = false;
                    boolean literaldeleted = false;
                    for (int k = i+1; k < totalOrder.length; k++) {
                        if (Arrays.stream(operators[totalOrder[k]].getPreconditions()).anyMatch(currLiteral::equals)) {
                            for (int t = k; t < i; t++) {
                                if (Arrays.stream(operators[t].getDeleteEffects()).anyMatch(currLiteral::equals)) {
                                    literaldeleted = true;
                                }
                            }
                            if (!literaldeleted) {
                                establish = true;
                            }
                        }
                    }

                    if (!establish) {
                        totalOrder=removeIntElement(totalOrder, i);
                    }

                }
            }

        }
        for(int i=0;i<totalOrder.length;i++){
            System.out.println(i+":" +totalOrder[i]);
        }
        backwardJustifiedOrder=totalOrder;
    }
    public int[] removeIntElement(int[] arr, int removedIdx) {
        int[] n = new int[arr.length - 1];
        System.arraycopy(arr, 0, n, 0, removedIdx);
        System.arraycopy(arr, removedIdx+1, n, removedIdx, arr.length - removedIdx-1);
        return n;
    }


}
