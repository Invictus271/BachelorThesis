
package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.util.ArrayList;

public class InputParser{
    private planStep[] operators;
    private planStep init;
    private planStep goal;
    private int planStepsCount=0;
    private int orderingConstraintCount=0;
    private int causalLinkcount=0;

    public boolean isNewDomainStarted() {
        return newDomainStarted;
    }

    public void setNewDomainStarted(boolean newDomainStarted) {
        this.newDomainStarted = newDomainStarted;
    }

    private boolean newDomainStarted=false;
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int[][] getOrderingConstraints() {
        return orderingConstraints;
    }

    public void setOrderingConstraints(int[][] orderingConstraints) {
        this.orderingConstraints = orderingConstraints;
    }

    private int[][] orderingConstraints;
    private String domain;
    private String problem;

    public boolean isCl() {
        return cl;
    }

    public void setCl(boolean cl) {
        this.cl = cl;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private boolean cl;
    private boolean stopped;
    //This class parses a text with a planning Domain into the data structure given above
    //if the flag partial order is activated InputParser expects to get ordering constraints
    //otherwise it expects a total ordered plan
    //if the flag cl is set the parser expects to get causal links and ordering constraints
    public  InputParser(BufferedReader br) throws IOException {
        if(this.getDomain()==null) {
            setDomain("unnamedDomain");
        }
        if(this.getProblem()==null) {
            setProblem("unnamedProblem");
        }
        String s = "";
        int linecount =0;
        int nextCounter=0;
        setStopped(false);
        newDomainStarted = false;
        while(br.ready()&&!isStopped()) {
            if (linecount == planStepsCount +3 +causalLinkcount +orderingConstraintCount)
            {
                setStopped(true);
            }
            if(!isStopped()){
                s=br.readLine();
            }
            if (linecount == 0) {
                if(s.matches("-?\\d+")) {
                    planStepsCount = Integer.parseInt(s);
                    operators = new planStep[planStepsCount-2];
                }else{
                    String[] arr = s.split(" ");
                    if(arr[0].contains("Domain:")){
                        String[] arr2= arr[1].split("/");
                        arr2[arr2.length-1]=arr2[arr2.length-1].replace(".","");
                        arr2[arr2.length-1]=arr2[arr2.length-1].replace("-","");
                        setDomain(arr2[arr2.length-1]);
                        newDomainStarted=true;
                    }
                    if(arr[0].equals("Problem:")){
                        String[] arr2= arr[1].split("/");
                        arr2[arr2.length-1]=arr2[arr2.length-1].replace(".","");
                        arr2[arr2.length-1]=arr2[arr2.length-1].replace("-","");
                        setProblem(arr2[arr2.length-1]);
                    }
                    linecount--;
                }
            }
            if(linecount==1){
                newDomainStarted=true;
            }
            //parsing operators
            if (linecount > 0 && linecount < planStepsCount+1) {
                String[] preconditions = {""};
                String[] addEffects ={""};
                String[] deleteEffects= {""};
                String[] arr = s.split(" ");
                int opNummer=Integer.parseInt(arr[0]);
                String label = arr[1].replace("[","");
                label=label.replace("]","");
                label=label.replace(",","");
                arr[2]=arr[2].replace("{", "");
                arr[2]=arr[2].replace("}", "");
                arr[2]=arr[2].replace("+","");
                ArrayList<String> preconditionList = new ArrayList<String>();
                boolean bracket=false;
                for(int i=0;i<arr[2].length();i++){
                    if(arr[2].charAt(i)=='['){
                        bracket=true;
                    }
                    if(arr[2].charAt(i)==']'){
                        bracket=false;
                    }
                    if(arr[2].charAt(i)==','&& !bracket){
                        String add = arr[2].substring(0,i).replace("[","");
                        add=add.replace("]","");
                        add=add.replace(",","");
                        preconditionList.add(add);
                        arr[2]=arr[2].substring(i+1,arr[2].length());
                        i=0;
                    }
                    if(i==arr[2].length()-1){
                        arr[2]=arr[2].replace("]","");
                        arr[2]=arr[2].replace("[","");
                        arr[2]=arr[2].replace(",","");
                        arr[2]=arr[2].replace("+","");
                        preconditionList.add(arr[2]);
                    }
                }
                if(!preconditionList.isEmpty()) {
                    preconditions = new String[preconditionList.size()];
                    for (int i = 0; i < preconditionList.size(); i++) {
                        preconditions[i] = preconditionList.get(i);
                    }
                }


                arr[3]=arr[3].replace("{", "");
                arr[3]=arr[3].replace("}", "");
                arr[3]=arr[3].replace("+","");
                ArrayList<String> addEffectList = new ArrayList<String>();
                bracket=false;
                for(int i=0;i<arr[3].length();i++){
                    if(arr[3].charAt(i)=='['){
                        bracket=true;
                    }
                    if(arr[3].charAt(i)==']'){
                        bracket=false;
                    }
                    if(arr[3].charAt(i)==','&& !bracket){
                        String add = arr[3].substring(0,i).replace("[","");
                        add=add.replace("]","");
                        add=add.replace(",","");
                        addEffectList.add(add);
                        arr[3]=arr[3].substring(i+1,arr[3].length());
                        i=0;
                    }
                    if(i==arr[3].length()-1){
                        arr[3]=arr[3].replace("]","");
                        arr[3]=arr[3].replace("[","");
                        arr[3]=arr[3].replace(",","");
                        arr[3]=arr[3].replace("+","");
                        addEffectList.add(arr[3]);

                    }
                }
                if(!addEffectList.isEmpty()) {
                    addEffects = new String[addEffectList.size()];
                    for (int i = 0; i < addEffectList.size(); i++) {
                        addEffects[i] = addEffectList.get(i);
                    }
                }

                arr[4]=arr[4].replace("{", "");
                arr[4]=arr[4].replace("}", "");
                arr[4]=arr[4].replace("+","");
                ArrayList<String> deleteEffectList = new ArrayList<String>();
                bracket=false;
                for(int i=0;i<arr[4].length();i++){
                    if(arr[4].charAt(i)=='['){
                        bracket=true;
                    }
                    if(arr[4].charAt(i)==']'){
                        bracket=false;
                    }
                    if(arr[4].charAt(i)==','&& !bracket){
                        String add = arr[4].substring(0,i).replace("[","");
                        add=add.replace("]","");
                        add=add.replace(",","");
                        deleteEffectList.add(add);
                        arr[4]=arr[4].substring(i+1,arr[4].length());
                        i=0;
                    }
                    if(i==arr[4].length()-1){
                        arr[4]=arr[4].replace("]","");
                        arr[4]=arr[4].replace("[","");
                        arr[4]=arr[4].replace(",","");
                        arr[4]=arr[4].replace("+","");
                        deleteEffectList.add(arr[4]);
                    }
                }
                if(!deleteEffectList.isEmpty()) {
                    deleteEffects = new String[deleteEffectList.size()];
                    for (int i = 0; i < deleteEffectList.size(); i++) {
                        deleteEffects[i] = deleteEffectList.get(i);
                    }
                }

                if (linecount == 1) {
                    init = new planStep(label,opNummer, preconditions, addEffects, deleteEffects);
                    //operators[0]=init;
                }
                if (linecount == 2) {
                    goal = new planStep(label,opNummer, preconditions, addEffects, deleteEffects);
                    //operators[1]=goal;
                }
                if (linecount > 2) {
                    operators[linecount-3] = new planStep(label, opNummer, preconditions, addEffects, deleteEffects);
                }
            }
            if(linecount==planStepsCount+1){
                    causalLinkcount=Integer.parseInt(s);
                }
                if(linecount==planStepsCount+causalLinkcount+2) {
                    orderingConstraintCount = Integer.parseInt(s);
                    orderingConstraints=new int[orderingConstraintCount][2];
                }
                if (linecount >planStepsCount +causalLinkcount +2 && linecount < planStepsCount + 3 + causalLinkcount + orderingConstraintCount) {
                    String[] arr = s.split(" ");
                    orderingConstraints[linecount - planStepsCount - causalLinkcount - 3][0] = Integer.parseInt(arr[0]);
                    orderingConstraints[linecount - planStepsCount - causalLinkcount - 3][1] = Integer.parseInt(arr[1]);
                }



            linecount++;

        }
    }
    public int getPlanStepsCount() {
        return planStepsCount;
    }

    public void setPlanStepsCount(int planStepsCount) {
        this.planStepsCount = planStepsCount;
    }

    public planStep[] getOperators() {
        return operators;
    }

    public void setOperators(planStep[] operators) {
        this.operators = operators;
    }

    public planStep getInit() {
        return init;
    }

    public void setInit(planStep init) {
        this.init = init;
    }

    public planStep getGoal() {
        return goal;
    }

    public void setGoal(planStep goal) {
        this.goal = goal;
    }




}
