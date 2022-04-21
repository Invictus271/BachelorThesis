package com.company;

public class CausalFlaw {
    public planStep getOperator() {
        return operator;
    }

    public void setOperator(planStep operator) {
        this.operator = operator;
    }

    planStep operator;

    public CausalLink getCausalLink() {
        return causalLink;
    }

    public void setCausalLink(CausalLink causalLink) {
        this.causalLink = causalLink;
    }

    CausalLink causalLink;
    public CausalFlaw(planStep operator, CausalLink causalLink){
        this.operator=operator;
        this.causalLink=causalLink;
    }
}
