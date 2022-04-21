package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.util.ArrayList;

public class InputParser{
    private int planStepsCount=0;
    private int causalLinkCount=0;
    private int orderingConstraintCount=0;
    private planStep[] operators;
    private planStep init;
    private planStep goal;
    private CausalLink[] causalLinks;

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
            setDomain("unnamed Domain");
        }
        if(this.getProblem()==null) {
            setProblem("unnamed Problem");
        }
        String s = "";
        int linecount =0;
        int nextCounter=0;
        setCl(true);
        setStopped(false);
        while(br.ready()&&!isStopped()) {
            if (cl && linecount == planStepsCount + 3 + causalLinkCount + orderingConstraintCount)
            {
                setStopped(true);
            }
            if (!cl && linecount == orderingConstraintCount + 2 + planStepsCount) {
                setStopped(true);
            }
            if(!isStopped()){
                s=br.readLine();
                Main.lineNumber++;
            }
                if (linecount == 0) {
                    if(s.matches("-?\\d+")) {
                        planStepsCount = Integer.parseInt(s);
                        operators = new planStep[planStepsCount];
                    }else{
                        String[] arr = s.split(" ");
                        if(arr[0].contains("Domain:")){
                            setDomain(arr[1]);
                        }
                        if(arr[0].equals("Problem:")){
                            setProblem(arr[1]);
                        }
                        linecount--;
                    }
                }
                //parsing operators
                if (linecount > 0 && linecount < planStepsCount+1) {
                    String[] preconditions = {""};
                    String[] addEffects ={""};
                    String[] deleteEffects= {""};
                    String[] arr = s.split(" ");
                    int opNummer=Integer.parseInt(arr[0]);
                    String label = arr[1];
                    arr[2]=arr[2].replace("{", "");
                    arr[2]=arr[2].replace("}", "");
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
                            preconditionList.add(arr[2].substring(0,i));
                            arr[2]=arr[2].substring(i+1,arr[2].length());
                            i=0;
                        }
                        if(i==arr[2].length()-1){
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
                            addEffectList.add(arr[3].substring(0,i));
                            arr[3]=arr[3].substring(i+1,arr[3].length());
                            i=0;
                        }
                        if(i==arr[3].length()-1){
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
                            deleteEffectList.add(arr[4].substring(0,i));
                            arr[4]=arr[4].substring(i+1,arr[4].length());
                            i=0;
                        }
                        if(i==arr[4].length()-1){
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
                        init = new planStep(label,opNummer, preconditions, addEffects, deleteEffects, Steptype.INIT);
                        operators[0]=init;
                    }
                    if (linecount == 2) {
                        goal = new planStep(label,opNummer, preconditions, addEffects, deleteEffects, Steptype.GOAL);
                        operators[1]=goal;
                    }
                    if (linecount > 2) {
                        operators[linecount-1] = new planStep(label, opNummer, preconditions, addEffects, deleteEffects, Steptype.OPERATOR);
                    }
                }

                //int nextCounter=0;
                if(linecount==planStepsCount+1){
                    nextCounter=Integer.parseInt(s);
                }
                if(linecount>planStepsCount+1) {

                    if(linecount==planStepsCount+2){
                        String[] arr=s.split(" ");
                        if(arr.length==2){
                            cl=false;
                            orderingConstraintCount=nextCounter;
                            orderingConstraints = new int[nextCounter][2];
                        }else{
                            cl=true;
                            causalLinkCount=nextCounter;
                            causalLinks=new CausalLink[causalLinkCount];
                        }

                    }
                    //parsing causal links and ordering constraints
                    if (cl) {
                        /*if (linecount == planStepsCount + 1) {
                            causalLinkCount = Integer.parseInt(s);
                            causalLinks = new CausalLink[causalLinkCount];
                        }*/
                        if (linecount > planStepsCount + 1 && linecount < planStepsCount + 2 + causalLinkCount) {
                            String[] arr = s.split(" ");
                            causalLinks[linecount - planStepsCount - 2] = new CausalLink(Integer.parseInt(arr[2]), Integer.parseInt(arr[0]), arr[1]);

                        }
                        if (linecount == planStepsCount + 2 + causalLinkCount) {
                            orderingConstraintCount = Integer.parseInt(s);
                            orderingConstraints = new int[orderingConstraintCount][2];
                        }
                        if (linecount > planStepsCount + 2 + causalLinkCount && linecount < planStepsCount + 3 + causalLinkCount + orderingConstraintCount) {
                            String[] arr = s.split(" ");
                            orderingConstraints[linecount - planStepsCount - causalLinkCount - 3][0] = Integer.parseInt(arr[0]);
                            orderingConstraints[linecount - planStepsCount - causalLinkCount - 3][1] = Integer.parseInt(arr[1]);
                        }
                        /*if(linecount==planStepsCount+3+causalLinkCount+orderingConstraintCount&&!s.equals("")){
                            String[] arr = s.split(" ");
                            if(arr[0].equals("Domain:")){
                                setDomain(arr[1]);
                            }
                            setStopped(true);
                        }
                        */
                        //parsing a total order
                    }
                    if (!cl) {
                        /*if (linecount == planStepsCount + 1) {
                            orderingConstraintCount = Integer.parseInt(s);
                            orderingConstraints = new int[orderingConstraintCount][2];
                        }*/
                        if (linecount > planStepsCount + 1 && linecount < planStepsCount + 2 + orderingConstraintCount) {
                            String[] arr = s.split(" ");
                            orderingConstraints[linecount - planStepsCount - 2][0] = Integer.parseInt(arr[0]);
                            orderingConstraints[linecount - planStepsCount - 2][1] = Integer.parseInt(arr[1]);
                        }
                        /*if(linecount==orderingConstraintCount+2+planStepsCount && !s.equals("")){
                            String[] arr = s.split(" ");
                            if(arr[0].equals("Domain:")){
                                 setDomain(arr[1]);
                            }
                            setStopped(true);
                        }*/
                    }
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

    public int getCausalLinkCount() {
        return causalLinkCount;
    }

    public void setCausalLinkCount(int causalLinkCount) {
        this.causalLinkCount = causalLinkCount;
    }

    public int getOrderingConstraintCount() {
        return orderingConstraintCount;
    }

    public void setOrderingConstraintCount(int orderingConstraintCount) {
        this.orderingConstraintCount = orderingConstraintCount;
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

    public CausalLink[] getCausalLinks() {
        return causalLinks;
    }

    public void setCausalLinks(CausalLink[] causalLinks) {
        this.causalLinks = causalLinks;
    }

    public int[][] getOrderingConstraints() {
        return orderingConstraints;
    }

    public void setOrderingConstraints(int[][] orderingConstraints) {
        this.orderingConstraints = orderingConstraints;
    }
}
