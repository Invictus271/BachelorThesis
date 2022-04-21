package com.company;

public class PDDLParser {

    public planStep[] getOperators() {
        return operators;
    }

    public void setOperators(planStep[] operators) {
        this.operators = operators;
    }

    public int[][] getOrderingConstraints() {
        return orderingConstraints;
    }

    public void setOrderingConstraints(int[][] orderingConstraints) {
        this.orderingConstraints = orderingConstraints;
    }

    private planStep[] operators;
    private int [][] orderingConstraints;

    public PDDLParser(String s,InputParser ip){
        int OPCount=ip.getOperators().length;
        orderingConstraints = new int[ip.getOrderingConstraints().length][2];
        operators = new planStep[OPCount+2];
        operators[0]=ip.getInit();
        operators[1]=ip.getGoal();
        boolean hasStarted = false;
        int linecount=0;
        String[] arr = s.split("\n");
        for(int i=0;i<arr.length;i++){
            if(hasStarted){
                if(linecount<OPCount){
                    String label = arr[i].split(" ")[1];
                    label = label.substring(0,label.length()-2);
                    //System.out.print(label);
                    for(int n=0;n<ip.getOperators().length;n++){
                        if(ip.getOperators()[n].getLabel().equals(label)){
                            operators[linecount+2]=ip.getOperators()[n];
                        }
                    }
                }
                if(linecount>=OPCount){
                    String[] line = arr[i].split(" ");
                    int op1 =-1;
                    int op2 =-1;
                    String label1 = line[0].substring(0,line[0].length()-2);
                    String label2 = line[2].substring(0,line[2].length()-2);
                    for(int n =0;n<operators.length;n++){
                        if(operators[n].getLabel().equals(label1)){
                            op1=n;
                        }
                        if(operators[n].getLabel().equals(label2)){
                            op2=n;
                        }
                    }
                    orderingConstraints[linecount-OPCount][0]=op1;
                    orderingConstraints[linecount-OPCount][1]=op2;

                }
                linecount++;
            }
            //System.out.println(arr[i]);
            String[] arr2=arr[i].split(" ");
            if(arr2[0].equals("SOLUTION")){
                hasStarted=true;
            }

        }

    }


}
