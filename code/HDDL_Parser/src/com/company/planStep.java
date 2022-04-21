package com.company;
public class planStep {

    private String Label;
    private String[] preconditions;
    private String[] addEffects;
    private String[] deleteEffects;
    private int opNummer;
    public planStep(String Label, int opNummer, String[] preconditions, String[] addEffects, String[] deleteEffects){
        this.opNummer=opNummer;
        this.Label=Label;
        this.preconditions=preconditions;
        this.addEffects=addEffects;
        this.deleteEffects=deleteEffects;
    }

    public int getOpNummer() {
        return opNummer;
    }

    public void setOpNummer(int opNummer) {
        this.opNummer = opNummer;
    }
    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String[] getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(String[] preconditions) {
        this.preconditions = preconditions;
    }

    public String[] getAddEffects() {
        return addEffects;
    }

    public void setAddEffects(String[] addEffects) {
        this.addEffects = addEffects;
    }

    public String[] getDeleteEffects() {
        return deleteEffects;
    }

    public void setDeleteEffects(String[] deleteEffects) {
        this.deleteEffects = deleteEffects;
    }



}
