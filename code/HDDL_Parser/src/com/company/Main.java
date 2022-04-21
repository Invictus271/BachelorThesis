package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean stepwise = true;
        String domainFile = "blocks.hddl";
        String problemFile = "blocks-p.hddl";

        try {
            FileWriter domain;
            FileWriter problem;
            BufferedWriter problemWriter;
            BufferedWriter domainWriter;
            OutputParser op;
            Process proc;
            int n=0;
            File folder = new File("Input/TestSet10");
            int overallMakespan=0;
            int outputMakespan=0;
            int optimizedMakespan=0;
            float makespanRatio=1;
            int problems=0;
            int optimizedProblems=0;
            float optimizationRatio=0;
            float computationTime=0;
            float optimizationPercentage=0;
            int[] makespan10=new int[10];
            int[] outputMakespan10= new int[10];
            int[] optimizedMakespan10=new int[10];
            float[] makespanratio10=new float[10];
            int[] problems10=new int[10];
            float[] optimizationPercentage10=new float[10];
            int[] optimizedProblems10=new int[10];
            float[] optimizationRatio10=new float[10];
            float[] computationTime10=new float[10];
            for(File fileEntry : folder.listFiles()) {
                FileInputStream inputStream = new FileInputStream(fileEntry);
                System.out.println("Domain:" + fileEntry);
                System.setIn(inputStream);
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                InputParser ip = new InputParser(br);
                do{
                    n++;
                    domainFile = "Output/domain" + n + ".hddl";
                    problemFile = "Output/problem" + n + "-p.hddl";
                    planStep[] allOperators = new planStep[ip.getOperators().length + 2];
                    allOperators[0] = ip.getInit();
                    allOperators[1] = ip.getGoal();
                    for (int i = 2; i < ip.getOperators().length + 2; i++) {
                        allOperators[i] = ip.getOperators()[i - 2];
                    }
                    int oldMakespan=Utillity.calculateMakespan(allOperators, ip.getOrderingConstraints());
                    //System.out.print("Old Makespan: " + oldMakespan + "   ");

                    domain = new FileWriter(domainFile);
                    problem = new FileWriter(problemFile);
                    problemWriter = new BufferedWriter(problem);
                    domainWriter = new BufferedWriter(domain);
                    op = new OutputParser(domainWriter, problemWriter, ip);
                    if (ip.isNewDomainStarted()) {
                        op = new OutputParser(domainWriter, problemWriter, ip);
                    }
                    domainWriter.flush();
                    problemWriter.flush();
                    long startTime = System.nanoTime();


                    proc = Runtime.getRuntime().exec("java -jar PANDA.jar " + domainFile + " " + problemFile + " -systemConfig makespan-astar -outputPlan");
                    System.out.println("java -jar PANDA.jar " + domainFile + " " + problemFile + " -systemConfig makespan-astar -outputPlan");

                    proc.waitFor(30, TimeUnit.SECONDS);
                    long endTime = System.nanoTime();
                    float runtime = (float) (endTime - startTime) / 1000000.0f;
                    computationTime=computationTime+runtime/36000;
                    java.io.InputStream is = proc.getInputStream();
                    byte b[] = new byte[is.available()];
                    is.read(b, 0, b.length);
                    String s = new String(b);
                    //System.out.print(s);
                    String[] arr = s.split(" ");
                    int newMakespan=oldMakespan;
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].equals("Makespan:")) {
                            String[] span = arr[i + 1].split("\n");
                            //System.out.println("new"+arr[i] + " " + span[0]);
                            newMakespan=Integer.parseInt(span[0]);
                        }
                    }

                    /*PDDLParser pp = new PDDLParser(s,ip);
                    FileWriter debugWriter = new FileWriter("DebugCasePDDL.log");
                    BufferedWriter debugBuffWriter = new BufferedWriter(debugWriter);
                    for(int i=0;i<pp.getOperators().length+pp.getOrderingConstraints().length;i++)
                    {
                        if(i<pp.getOperators().length){
                            debugBuffWriter.write(i+" "+pp.getOperators()[i].getLabel()+" {");
                            for(int t=0;t<pp.getOperators()[i].getPreconditions().length;t++){
                                debugBuffWriter.write(pp.getOperators()[i].getPreconditions()[t]);
                                if(t<pp.getOperators()[i].getPreconditions().length-1){
                                    debugBuffWriter.write(",");
                                }
                            }
                            debugBuffWriter.write("} {");
                            for(int t=0;t<pp.getOperators()[i].getAddEffects().length;t++){
                                debugBuffWriter.write(pp.getOperators()[i].getAddEffects()[t]);
                                if(t<pp.getOperators()[i].getAddEffects().length-1){
                                    debugBuffWriter.write(",");
                                }
                            }
                            debugBuffWriter.write("} {");
                            for(int t=0;t<pp.getOperators()[i].getDeleteEffects().length;t++){
                                debugBuffWriter.write(pp.getOperators()[i].getDeleteEffects()[t]);
                                if(t<pp.getOperators()[i].getDeleteEffects().length-1){
                                    debugBuffWriter.write(",");
                                }
                            }
                            debugBuffWriter.write("}");
                            debugBuffWriter.write("\n");
                        }
                        if(i>=pp.getOperators().length){
                            debugBuffWriter.write(pp.getOrderingConstraints()[i-pp.getOperators().length][0]+" "+pp.getOrderingConstraints()[i-pp.getOperators().length][1]+"\n");
                        }
                    }
                    //debugBuffWriter.flush();
                    //debugBuffWriter.close();
                    */

                    if(stepwise){
                        problems++;
                        System.out.println(problems);
                        int length=ip.getOperators().length/10;
                        problems10[length]++;
                        makespan10[length]=makespan10[length]+oldMakespan;
                        computationTime10[length]=computationTime10[length]+runtime/36000;
                        if(newMakespan < oldMakespan){
                            outputMakespan10[length]=outputMakespan10[length]+newMakespan;
                            optimizedProblems10[length]++;
                            optimizedMakespan10[length]=optimizedMakespan10[length]+(oldMakespan-newMakespan);
                            optimizationPercentage10[length]=optimizationPercentage10[length]+((float)newMakespan/(float)oldMakespan);
                            if(length==3){
                                //System.out.print(oldMakespan+">"+newMakespan);
                            }
                        }else{
                            outputMakespan10[length] = outputMakespan10[length]+oldMakespan;
                        }



                    }else {
                        problems++;
                        System.out.println(problems);
                        overallMakespan = overallMakespan + oldMakespan;
                        outputMakespan = outputMakespan + newMakespan;
                        if (newMakespan > oldMakespan) {
                            //System.out.print("MAKESPAN=" +newMakespan);
                            //System.out.println("Omegalul");
                            //System.out.println("java -jar PANDA.jar " + domainFile + " " + problemFile + " -systemConfig makespan-astar -outputPlan");
                            //debugBuffWriter.write(s);
                        }
                        if (oldMakespan > newMakespan) {
                            optimizedProblems++;
                            optimizedMakespan = optimizedMakespan + (oldMakespan - newMakespan);
                        }
                    }

                    ip = new InputParser(br);



                }while(ip.isStopped());

            }
            if(stepwise){
                String csvFile = "Output/ResultsMakespanOptimizatonStepwise.csv";
                FileWriter writer = new FileWriter(csvFile);
                BufferedWriter bw = new BufferedWriter(writer);
                CSVUtils.writeLine(bw, Arrays.asList("length","problems","optimized problems","optimization ratio","overall makespan","optimized makespan","average optimization","makespanratio","computationtime"));
               for(int i=0;i<6;i++){
                   optimizationRatio10[i]=(float)optimizedProblems10[i]/(float)problems10[i];
                   makespanratio10[i]=(float)(makespan10[i]-optimizedMakespan10[i])/(float) makespan10[i];
                   if(optimizedProblems10[i]>0) {
                       optimizationPercentage10[i] = optimizationPercentage10[i] / (float) optimizedProblems10[i];
                   }else{
                       optimizationPercentage10[i] = 1;
                   }
                   CSVUtils.writeLine(bw, Arrays.asList(">"+i*10,""+problems10[i],""+optimizedProblems10[i],""+optimizationRatio10[i],""+makespan10[i],""+optimizedMakespan10[i],""+optimizationPercentage10[i],""+makespanratio10[i],""+computationTime10[i]));
               }

                bw.flush();
                bw.close();
            }else {
                optimizationRatio=(float)optimizedProblems/(float)problems;
                makespanRatio=(float)optimizedMakespan/(float)overallMakespan;
                String csvFile = "Output/ResultsMakespanOptimizaton.csv";
                FileWriter writer = new FileWriter(csvFile,true);
                BufferedWriter bw = new BufferedWriter(writer);
                CSVUtils.writeLine(bw, Arrays.asList(""+problems,""+optimizedProblems,""+optimizationRatio,""+overallMakespan,""+optimizedMakespan,""+makespanRatio,""+computationTime));
                bw.flush();
                bw.close();
            }


        } catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            //if(domainWr)
        }

    }
}
