package com.company;

public class OpenPrecondition {
    public String[] getOpenLiterals() {
        return openLiterals;
    }

    public void setOpenLiterals(String[] openLiterals) {
        this.openLiterals = openLiterals;
    }

    public planStep getOperator() {
        return operator;
    }

    public void setOperator(planStep operator) {
        this.operator = operator;
    }

    String[] openLiterals;
    planStep operator;
    public OpenPrecondition(String[] openLiterals,planStep operator){
        this.openLiterals=openLiterals;
        this.operator=operator;
    }
}
