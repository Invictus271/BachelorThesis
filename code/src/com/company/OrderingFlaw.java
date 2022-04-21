package com.company;

public class OrderingFlaw {
    private planStep operator;
    private planStep threat;
    private String threatenedLiteral;

    public OrderingFlaw(planStep threat,planStep operator,String threatenedLiteral){
        this.threat=threat;
        this.operator=operator;
        this.threatenedLiteral=threatenedLiteral;
    }

    public planStep getOperator() {
        return operator;
    }

    public void setOperator(planStep operator) {
        this.operator = operator;
    }

    public planStep getThreat() {
        return threat;
    }

    public void setThreat(planStep threat) {
        this.threat = threat;
    }

    public String getThreatendLiteral() {
        return threatenedLiteral;
    }

    public void setThreatendLiteral(String threatendLiteral) {
        this.threatenedLiteral = threatendLiteral;
    }


}
