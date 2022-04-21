package com.company;

import java.io.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static int overallProblems=0;
    private static int optimizedProblems =0;
    private static int opOptimizedProblems=0;
    private static int mkspnOptimizedProblems=0;
    private static int overallOperators=0;
    private static int overallMakespan=0;
    private static int optimizedOperators=0;
    private static int optimizedMakespan=0;
    private static float computationTime=0;
    public static int lineNumber=0;
    private static int[] optimizedOperations = new int[10];
    private static int[] optimizedMakespan10 = new int[10];
    private static int[] opOptimizedPlans = new int[10];
    private static int[] mkspnOptimizedPlans= new int[10];
    private static int[] plans = new int[10];
    private static int[] operators = new int[10];
    private static int[] mkspn = new int[10];
    private static float[] time = new float[10];
    private static float[] averageOpOptimiziation = new float[10];
    private static float[] averageMkSpanOptimiziation = new float[10];
    private static float avrgOpOptimization = 0;
    private static float avrgMkSpanOptimiziation = 0;


    public static void main(String[] args) throws IOException {
        Scanner scan=new Scanner(System.in);
        System.out.println("Choose Justification Algorithm ");
        System.out.println("1: Backward Justification");
        System.out.println("2: Well Justification");
        System.out.println("3: Greedy Justification");
        int algoId=Integer.parseInt(scan.nextLine());
        boolean forcePO = false;
        boolean stepwise=false;
        //read Planning Domain from .txt

        String resultFile = "output/Test.csv";
        String csvFile = "output/TestStepWise.csv";
        FileWriter writer = new FileWriter(csvFile,true);
        BufferedWriter bw = new BufferedWriter(writer);
        FileWriter ResultWriter= new FileWriter(resultFile,true);
        BufferedWriter brw= new BufferedWriter(ResultWriter);


        boolean comapctStats = true;
        if (!comapctStats) {
                    //CSVUtils.writeLine(bw, Arrays.asList("Domain", "Problem", "Input Operations", "Input Makespan", "Output operations", "Output Makespan", "Operation ratio", "Makespan Ratio", "Runtime in ms", "RandomSeed", "Algorithm"));
                } else {
                    //CSVUtils.writeLine(bw, Arrays.asList("Domain", "Problem", "Input Operations", "Input Makespan", "Average Output operations", "Average Output Makespan", "Operation ratio", "Makespan Ratio", "Runtime in ms", "Algorithm"));
        }
        File folder = new File("input/TestSet");
        for(File fileEntry : folder.listFiles()) {
            System.out.println(fileEntry.getName());
            try {
                FileInputStream inputStream = new FileInputStream(fileEntry);
                System.setIn(inputStream);


                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                //parse .txt into operations, CausalLinks and Ordering Constraints
                InputParser ip = new InputParser(br);
                //System.out.println(Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints()));
                long RandomSeed = 0;
                int reps = 10;
                int modus =0;
                //modus describes which metric should be maximized
                //0: Operation reduction
                //1: Makespan reduction
                //2: average over all reductions
                switch (algoId) {
                    case 1:
                        backwardJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats);
                        break;
                    case 2:
                    if (ip.isCl() && !forcePO) {
                        if(stepwise){
                           wellJustificationPOCLStepwise(ip,bw,reps);
                        }else{
                            wellJustificationOnPOCLtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                        }
                    } else {
                        if(stepwise){
                            wellJustificationPOStepwise(ip,bw,reps);
                        }else {
                            wellJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats, modus);
                        }
                    }
                    break;
                    case 3:
                    if (ip.isCl() && !forcePO) {
                        if(stepwise){
                            greeedyJustificationPOCLStepwise(ip,bw,reps);
                        }
                        else{
                            greedyJustificationOnPOCLtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                        }
                    } else {
                        if(stepwise){
                           greedyJustificationPOStepwise(ip,bw,reps);
                        }else{

                            greedyJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                        }
                    }
                    break;
                    case 4:
                        overallProblems++;
                        Greedy_Justification gJustPO = new Greedy_Justification(ip.getOperators().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                        Greedy_Justification gJustPOCL = new Greedy_Justification(ip.getOperators().clone(), ip.getCausalLinks(), ip.getOrderingConstraints().clone(), RandomSeed);
                        if(gJustPO.getGreedyJustifiedOperators().length<gJustPOCL.getGreedyJustifiedOperators().length){
                            //System.out.println("Case 1, Problem: "+lineNumber);
                        }
                        if(gJustPO.getGreedyJustifiedOperators().length>gJustPOCL.getGreedyJustifiedOperators().length){
                            System.out.println("Case 2, Problem: "+lineNumber);
                            System.out.println(gJustPO.getGreedyJustifiedOperators().length);
                            System.out.println(gJustPOCL.getGreedyJustifiedOperators().length);
                        }
                        break;
                    case 5:
                        overallProblems++;
                        Well_Justification wJustPO = new Well_Justification(ip.getOperators().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                        Well_Justification wJustPOCL = new Well_Justification(ip.getOperators().clone(), ip.getCausalLinks().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                        if(wJustPO.getWellJustifiedOperators().length<wJustPOCL.getWellJustifiedOperators().length){
                            System.out.println("Case 1, Problem: "+lineNumber);
                        }
                        if(wJustPO.getWellJustifiedOperators().length>wJustPOCL.getWellJustifiedOperators().length){
                            System.out.println("Case 2, Problem: "+lineNumber);
                        }
                    break;


                }
                while (ip.isStopped()) {
                    System.out.println("Problem: "+overallProblems);
                    ip = new InputParser(br);
                    switch (algoId) {
                        case 1:
                            backwardJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats);
                            break;
                        case 2:
                            if (ip.isCl() && !forcePO) {
                                if(stepwise){
                                   wellJustificationPOCLStepwise(ip,bw,reps);
                                }else{
                                    wellJustificationOnPOCLtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                                }
                            } else {
                                if(stepwise){
                                    wellJustificationPOStepwise(ip,bw,reps);
                                }else {
                                    wellJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats, modus);
                                }
                            }
                            break;
                        case 3:
                            if (ip.isCl() && !forcePO) {
                                if(stepwise){
                                    greeedyJustificationPOCLStepwise(ip,bw,reps);
                                }
                                else{
                                    greedyJustificationOnPOCLtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                                }
                            } else {
                                if(stepwise){
                                   greedyJustificationPOStepwise(ip,bw,reps);
                                }else{

                                    greedyJustificationOnPOtoCSV(ip, bw, RandomSeed, reps, comapctStats,modus);
                                }
                            }
                            break;
                        case 4:
                            overallProblems++;
                            Greedy_Justification gJustPO = new Greedy_Justification(ip.getOperators().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                            Greedy_Justification gJustPOCL = new Greedy_Justification(ip.getOperators(), ip.getCausalLinks(), ip.getOrderingConstraints(), RandomSeed);
                            if(gJustPO.getGreedyJustifiedOperators().length<gJustPOCL.getGreedyJustifiedOperators().length){
                                //System.out.println("Case 1, Problem: "+lineNumber);
                            }
                            if(gJustPO.getGreedyJustifiedOperators().length>gJustPOCL.getGreedyJustifiedOperators().length){
                                System.out.println("Case 2, Problem: "+lineNumber);
                            }
                            break;
                        case 5:
                            overallProblems++;
                            Well_Justification wJustPO = new Well_Justification(ip.getOperators().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                            Well_Justification wJustPOCL = new Well_Justification(ip.getOperators().clone(), ip.getCausalLinks().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                            if(wJustPO.getWellJustifiedOperators().length<wJustPOCL.getWellJustifiedOperators().length){
                                //System.out.println("Case 1, Problem: "+lineNumber);
                            }
                            if(wJustPO.getWellJustifiedOperators().length>wJustPOCL.getWellJustifiedOperators().length){
                                System.out.println("Case 2, Problem: "+lineNumber);
                            }
                            break;
                    }

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        switch (algoId) {
                case 1:
                    CSVUtils.writeLine(ResultWriter,Arrays.asList(""+overallProblems,""+optimizedProblems,""+mkspnOptimizedProblems,""+opOptimizedProblems,""+overallOperators,""+optimizedOperators,""+overallMakespan,""+ optimizedMakespan,""+computationTime,"Backward Justification"));
                    break;
                case 2:
                    if (!forcePO) {
                        if(stepwise) {
                            for (int i = 0; i < 10; i++) {
                                float planRatio = 1;
                                float opRatio = 1;
                                float mkspnRatio = 1;
                                if (plans[i] != 0) {
                                    planRatio = (float)(plans[i]-opOptimizedPlans[i])/(float)(plans[i]);
                                    opRatio = (float)(operators[i]-optimizedOperations[i])/(float)(operators[i]);
                                    mkspnRatio = (float)(mkspn[i]-optimizedMakespan10[i])/(float)(mkspn[i]);
                                    averageOpOptimiziation[i]=averageOpOptimiziation[i]/(float)opOptimizedPlans[i];
                                    averageMkSpanOptimiziation[i]=averageMkSpanOptimiziation[i]/(float)mkspnOptimizedPlans[i];
                                }
                                CSVUtils.writeLine(bw, Arrays.asList("" + i * 10,""+plans[i], "" + opOptimizedPlans[i], "" + mkspnOptimizedPlans[i], "" + optimizedOperations[i],
                                        "" + optimizedMakespan10[i], "" + planRatio, "" + opRatio,""+averageOpOptimiziation[i], "" + mkspnRatio,""+averageMkSpanOptimiziation[i], "" + time[i]/36000, "well justification on POCL"));
                            }
                        }else {
                            avrgOpOptimization=avrgOpOptimization/(float)opOptimizedProblems;
                            avrgMkSpanOptimiziation=avrgMkSpanOptimiziation/(float)mkspnOptimizedProblems;
                            CSVUtils.writeLine(ResultWriter, Arrays.asList("" + overallProblems, "" + optimizedProblems, "" + mkspnOptimizedProblems, "" + opOptimizedProblems, "" + overallOperators, "" + optimizedOperators, "" + overallMakespan, "" + optimizedMakespan,""+avrgOpOptimization,""+avrgMkSpanOptimiziation, "" + computationTime, "Well Justification on POCL"));
                        }
                    } else {
                        if(stepwise){
                            for(int i=0;i<10;i++){
                                float planRatio=1;
                                float opRatio=1;
                                float mkspnRatio=1;
                                if(plans[i]!=0){
                                    planRatio = (float)(plans[i]-opOptimizedPlans[i])/(float)(plans[i]);
                                    opRatio = (float)(operators[i]-optimizedOperations[i])/(float)(operators[i]);
                                    mkspnRatio = (float)(mkspn[i]-optimizedMakespan10[i])/(float)(mkspn[i]);
                                    averageOpOptimiziation[i]=averageOpOptimiziation[i]/(float)opOptimizedPlans[i];
                                    averageMkSpanOptimiziation[i]=averageMkSpanOptimiziation[i]/(float)mkspnOptimizedPlans[i];
                                }

                                CSVUtils.writeLine(bw,Arrays.asList(""+i*10,""+plans[i],""+opOptimizedPlans[i],""+mkspnOptimizedPlans[i],""+optimizedOperations[i],
                                ""+ optimizedMakespan10[i],""+planRatio,""+opRatio,""+averageOpOptimiziation[i],""+mkspnRatio,""+averageMkSpanOptimiziation[i],""+time[i]/36000,"well justification on PO"));
                            }
                        }else{
                                avrgOpOptimization=avrgOpOptimization/(float)opOptimizedProblems;
                                avrgMkSpanOptimiziation=avrgMkSpanOptimiziation/(float)mkspnOptimizedProblems;
                                CSVUtils.writeLine(ResultWriter, Arrays.asList("" + overallProblems, "" + optimizedProblems, "" + mkspnOptimizedProblems, "" + opOptimizedProblems, "" + overallOperators, "" + optimizedOperators, "" + overallMakespan, "" + optimizedMakespan,""+avrgOpOptimization,""+avrgMkSpanOptimiziation, "" + computationTime, "Well Justification on POCL"));
                        }
                    }
                    break;
                case 3:
                    if (!forcePO) {
                        if(stepwise){
                            for(int i=0;i<10;i++){
                                float planRatio=1;
                                float opRatio=1;
                                float mkspnRatio=1;
                                if(plans[i]!=0){
                                    planRatio = (float)(plans[i]-opOptimizedPlans[i])/(float)(plans[i]);
                                    opRatio = (float)(operators[i]-optimizedOperations[i])/(float)(operators[i]);
                                    mkspnRatio = (float)(mkspn[i]-optimizedMakespan10[i])/(float)(mkspn[i]);
                                    averageOpOptimiziation[i]=averageOpOptimiziation[i]/(float)opOptimizedPlans[i];
                                    averageMkSpanOptimiziation[i]=averageMkSpanOptimiziation[i]/(float)mkspnOptimizedPlans[i];
                                }

                                CSVUtils.writeLine(bw,Arrays.asList(""+i*10,""+plans[i],""+opOptimizedPlans[i],""+mkspnOptimizedPlans[i],""+optimizedOperations[i],
                                        ""+ optimizedMakespan10[i],""+planRatio,""+opRatio,""+averageOpOptimiziation[i],""+mkspnRatio,""+averageMkSpanOptimiziation[i],""+time[i]/36000,"greedy justification on POCL"));
                            }
                        }else {
                            avrgOpOptimization=avrgOpOptimization/(float)opOptimizedProblems;
                            avrgMkSpanOptimiziation=avrgMkSpanOptimiziation/(float)mkspnOptimizedProblems;
                            CSVUtils.writeLine(ResultWriter, Arrays.asList("" + overallProblems, "" + optimizedProblems, "" + mkspnOptimizedProblems, "" + opOptimizedProblems, "" + overallOperators, "" + optimizedOperators, "" + overallMakespan, "" + optimizedMakespan,""+avrgOpOptimization,""+avrgMkSpanOptimiziation, "" + computationTime, "Well Justification on POCL"));
                        }
                    } else {
                        if(stepwise){
                            for(int i=0;i<10;i++){
                                float planRatio=1;
                                float opRatio=1;
                                float mkspnRatio=1;
                                if(plans[i]!=0){
                                    planRatio = (float)(plans[i]-opOptimizedPlans[i])/(float)(plans[i]);
                                    opRatio = (float)(operators[i]-optimizedOperations[i])/(float)(operators[i]);
                                    mkspnRatio = (float)(mkspn[i]-optimizedMakespan10[i])/(float)(mkspn[i]);
                                    averageOpOptimiziation[i]=averageOpOptimiziation[i]/(float)opOptimizedPlans[i];
                                    averageMkSpanOptimiziation[i]=averageMkSpanOptimiziation[i]/(float)mkspnOptimizedPlans[i];

                                }

                                CSVUtils.writeLine(bw,Arrays.asList(""+i*10,""+plans[i],""+opOptimizedPlans[i],""+mkspnOptimizedPlans[i],""+optimizedOperations[i],
                                ""+ optimizedMakespan10[i],""+planRatio,""+opRatio,""+averageOpOptimiziation[i],""+mkspnRatio,""+averageMkSpanOptimiziation[i],""+time[i]/36000,"greedy justification on PO"));
                            }
                        }else {
                            avrgOpOptimization=avrgOpOptimization/(float)opOptimizedProblems;
                            avrgMkSpanOptimiziation=avrgMkSpanOptimiziation/(float)mkspnOptimizedProblems;
                            CSVUtils.writeLine(ResultWriter, Arrays.asList("" + overallProblems, "" + optimizedProblems, "" + mkspnOptimizedProblems, "" + opOptimizedProblems, "" + overallOperators, "" + optimizedOperators, "" + overallMakespan, "" + optimizedMakespan,""+avrgOpOptimization,""+avrgMkSpanOptimiziation, "" + computationTime, "Well Justification on POCL"));
                        }
                    }
                    break;
        }
        bw.flush();
        bw.close();
        brw.flush();
        brw.close();
    }
    public static void wellJustificationPOStepwise(InputParser ip, BufferedWriter bw, int reps){
        overallProblems++;

            for(int i=0;i<reps;i++){
                long startTime = System.nanoTime();
                Well_Justification wJust = new Well_Justification(ip.getOperators(),ip.getOrderingConstraints(),i);
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
                int outputMakespan = Utillity.calculateMakespan(wJust.getWellJustifiedOperators(),wJust.getWellJustifiedOC());
                int opLength = ip.getOperators().length/10;
                if(ip.getOperators().length>100){
                    System.out.println("Too much OPs"+ip.getOperators().length);
                }
                optimizedOperations[opLength]=optimizedOperations[opLength]+(ip.getOperators().length-wJust.getWellJustifiedOperators().length);
                optimizedMakespan10[opLength]=optimizedMakespan10[opLength]+(inputMakespan-outputMakespan);
                if(wJust.getWellJustifiedOperators().length<ip.getOperators().length){
                    opOptimizedPlans[opLength]++;
                    averageOpOptimiziation[opLength] = averageOpOptimiziation[opLength]+(float)wJust.getWellJustifiedOperators().length/(float)ip.getOperators().length;
                }
                if(outputMakespan<inputMakespan){
                    mkspnOptimizedPlans[opLength]++;
                    averageMkSpanOptimiziation[opLength] = averageMkSpanOptimiziation[opLength] +(float)outputMakespan/(float)inputMakespan;
                }
                plans[opLength]++;
                operators[opLength]=operators[opLength]+ip.getOperators().length;
                mkspn[opLength]=mkspn[opLength]+inputMakespan;
                time[opLength]=time[opLength]+runtime;
            }


    }
    public static void wellJustificationPOCLStepwise(InputParser ip, BufferedWriter bw, int reps){
        overallProblems++;

        for(int i=0;i<reps;i++){
            long startTime = System.nanoTime();
            Well_Justification wJust = new Well_Justification(ip.getOperators(),ip.getCausalLinks(),ip.getOrderingConstraints(),i);
            long endTime = System.nanoTime();
            float runtime = (float) (endTime - startTime) / 1000000.0f;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            int outputMakespan = Utillity.calculateMakespan(wJust.getWellJustifiedOperators(),wJust.getWellJustifiedOC());
            int opLength = ip.getOperators().length/10;
            if(ip.getOperators().length>100){
                System.out.println("Too much OPs"+ip.getOperators().length);
            }
            optimizedOperations[opLength]=optimizedOperations[opLength]+(ip.getOperators().length-wJust.getWellJustifiedOperators().length);
            optimizedMakespan10[opLength]=optimizedMakespan10[opLength]+(inputMakespan-outputMakespan);
            if(wJust.getWellJustifiedOperators().length<ip.getOperators().length){
                opOptimizedPlans[opLength]++;
                averageOpOptimiziation[opLength] = averageOpOptimiziation[opLength]+(float)wJust.getWellJustifiedOperators().length/(float)ip.getOperators().length;
            }
            if(outputMakespan<inputMakespan){
                mkspnOptimizedPlans[opLength]++;
                averageMkSpanOptimiziation[opLength] = averageMkSpanOptimiziation[opLength] +(float)outputMakespan/(float)inputMakespan;
            }
            plans[opLength]++;
            operators[opLength]=operators[opLength]+ip.getOperators().length;
            mkspn[opLength]=mkspn[opLength]+inputMakespan;
            time[opLength]=time[opLength]+runtime;
        }


    }
    public static void greedyJustificationPOStepwise(InputParser ip, BufferedWriter bw, int reps){
        overallProblems++;

        for(int i=0;i<reps;i++){
            long startTime = System.nanoTime();
            Greedy_Justification gJust= new Greedy_Justification(ip.getOperators(),ip.getOrderingConstraints(),i);
            long endTime = System.nanoTime();
            float runtime = (float) (endTime - startTime) / 1000000.0f;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            int outputMakespan = Utillity.calculateMakespan(gJust.getGreedyJustifiedOperators(),gJust.getGreedyJustifiedOrderingConstraints());
            int opLength = ip.getOperators().length/10;
            if(ip.getOperators().length>100){
                System.out.println("Too much OPs"+ip.getOperators().length);
            }
            optimizedOperations[opLength]=optimizedOperations[opLength]+(ip.getOperators().length-gJust.getGreedyJustifiedOperators().length);
            optimizedMakespan10[opLength]=optimizedMakespan10[opLength]+(inputMakespan-outputMakespan);
            if(gJust.getGreedyJustifiedOperators().length<ip.getOperators().length){
                opOptimizedPlans[opLength]++;
                averageOpOptimiziation[opLength] = averageOpOptimiziation[opLength]+(float)gJust.getGreedyJustifiedOperators().length/(float)ip.getOperators().length;
            }
            if(outputMakespan<inputMakespan){
                mkspnOptimizedPlans[opLength]++;
                averageMkSpanOptimiziation[opLength] = averageMkSpanOptimiziation[opLength] +(float)outputMakespan/(float)inputMakespan;
            }
            plans[opLength]++;
            operators[opLength]=operators[opLength]+ip.getOperators().length;
            mkspn[opLength]=mkspn[opLength]+inputMakespan;
            time[opLength]=time[opLength]+runtime;
        }


    }
    public static void greeedyJustificationPOCLStepwise(InputParser ip, BufferedWriter bw, int reps){
        overallProblems++;
        for(int i=0;i<reps;i++){
            long startTime = System.nanoTime();
            Greedy_Justification gJust= new Greedy_Justification(ip.getOperators(),ip.getCausalLinks(),ip.getOrderingConstraints(),i);
            long endTime = System.nanoTime();
            float runtime = (float) (endTime - startTime) / 1000000.0f;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            int outputMakespan = Utillity.calculateMakespan(gJust.getGreedyJustifiedOperators(),gJust.getGreedyJustifiedOrderingConstraints());
            int opLength = ip.getOperators().length/10;
            if(ip.getOperators().length>100){
                System.out.println("Too much OPs"+ip.getOperators().length);
            }
            optimizedOperations[opLength]=optimizedOperations[opLength]+(ip.getOperators().length-gJust.getGreedyJustifiedOperators().length);
            optimizedMakespan10[opLength]=optimizedMakespan10[opLength]+(inputMakespan-outputMakespan);
            if(gJust.getGreedyJustifiedOperators().length<ip.getOperators().length){
                opOptimizedPlans[opLength]++;
                averageOpOptimiziation[opLength] = averageOpOptimiziation[opLength]+(float)gJust.getGreedyJustifiedOperators().length/(float)ip.getOperators().length;
            }
            if(outputMakespan<inputMakespan){
                mkspnOptimizedPlans[opLength]++;
                averageMkSpanOptimiziation[opLength] = averageMkSpanOptimiziation[opLength] +(float)outputMakespan/(float)inputMakespan;
            }
            plans[opLength]++;
            operators[opLength]=operators[opLength]+ip.getOperators().length;
            mkspn[opLength]=mkspn[opLength]+inputMakespan;
            time[opLength]=time[opLength]+runtime;
        }



    }
    public static void backwardJustificationOnPOtoCSV(InputParser ip,BufferedWriter bw,long RandomSeed,int reps,boolean compactStats){
        try {
            int sumOutputOperations=0;
            int sumOutputMakespan=0;
            float runtimeSum=0;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            for(int i=0;i<reps;i++) {
                RandomSeed=i;
                long startTime = System.nanoTime();
                Backward_Justification bJust = new Backward_Justification(ip.getOperators(), ip.getOrderingConstraints(), RandomSeed);
                if(ip.getOperators().length!=bJust.getBackwardJustifiedOperators().length){
                    //System.out.println("Optimization");
                }
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int outputMakespan = Utillity.calculateMakespan(bJust.getBackwardJustifiedOperators(),bJust.getBackwardJustifiedOrderingConstraints());
                float makespanRatio = (float)(inputMakespan)/(float)(outputMakespan);
                float operatorRatio = (float)(bJust.getBackwardJustifiedOperators().length)/(float)(ip.getOperators().length);
                if(!compactStats) {
                    CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + bJust.getBackwardJustifiedOperators().length, "" + outputMakespan,
                            "" + operatorRatio, "" + makespanRatio, "" + runtime, "" + RandomSeed, "Backward Justification on PO-Plan"));
                }else{
                    sumOutputOperations=sumOutputOperations+ bJust.getBackwardJustifiedOperators().length;
                    sumOutputMakespan=sumOutputMakespan+outputMakespan;
                    runtimeSum=runtimeSum+runtime;
                }
            }
            if(compactStats){
                float averageOutputOperations=(float)sumOutputOperations/(float)reps;
                float averageOutputMakespan = (float)sumOutputMakespan/(float)reps;
                float makespanRatio = (float)(averageOutputMakespan)/(float)(inputMakespan);
                float operatorRatio = (float)(averageOutputOperations)/(float)(ip.getOperators().length);
                CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + averageOutputOperations, "" + averageOutputMakespan,
                            "" +operatorRatio, "" + makespanRatio, ""+runtimeSum, "Backward Justification on PO-Plan"));
                overallOperators=overallOperators+ip.getOperators().length;
                overallMakespan=overallMakespan+inputMakespan;
                overallProblems++;
                optimizedOperators=optimizedOperators+(ip.getOperators().length-(int)averageOutputOperations);
                optimizedMakespan = optimizedMakespan +(inputMakespan-(int)averageOutputMakespan);
                if((ip.getOperators().length-(int)averageOutputOperations)>0 || (inputMakespan-(int)averageOutputMakespan)>0){
                    optimizedProblems++;
                }
                if((inputMakespan-(int)averageOutputMakespan)>0){
                    mkspnOptimizedProblems++;
                }
                if((ip.getOperators().length-(int)averageOutputOperations)>0){
                    opOptimizedProblems++;
                }
                computationTime=computationTime+runtimeSum;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void wellJustificationOnPOtoCSV(InputParser ip,BufferedWriter bw,long RandomSeed,int reps,boolean compactStats, int modus){
        try {
            int sumOutputOperations=0;
            int sumOutputMakespan=0;
            int minOperations=0;
            int minMakespan=0;
            float runtimeSum=0;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            for(int i=0;i<reps;i++) {
                RandomSeed=i;
                long startTime = System.nanoTime();
                Well_Justification wJust = new Well_Justification(ip.getOperators().clone(), ip.getOrderingConstraints().clone(), RandomSeed);
                if(ip.getOperators().length!=wJust.getWellJustifiedOperators().length){
                    //System.out.println("Optimization");
                }
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int outputMakespan = Utillity.calculateMakespan(wJust.getWellJustifiedOperators(),wJust.getWellJustifiedOC());
                float makespanRatio = (float)(outputMakespan)/(float)(inputMakespan);
                float operatorRatio = (float)(wJust.getWellJustifiedOperators().length)/(float)(ip.getOperators().length);
                if(!compactStats) {
                    CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + wJust.getWellJustifiedOperators().length, "" + outputMakespan,
                            "" + (wJust.getWellJustifiedOperators().length / ip.getOperators().length),""+operatorRatio ,"" + makespanRatio, "" + runtime, "" + RandomSeed, "Well Justification on PO-Plan"));
                }else{
                    if(modus==0){
                        if(minOperations==0 || minOperations>wJust.getWellJustifiedOperators().length){
                            minOperations=wJust.getWellJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;

                        }
                    }
                    if(modus==1){
                        if(minMakespan==0 || minMakespan>outputMakespan){
                            minOperations=wJust.getWellJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==2) {
                        if(minOperations==0 || minOperations>wJust.getWellJustifiedOperators().length){
                            minOperations=wJust.getWellJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                        sumOutputOperations = sumOutputOperations + wJust.getWellJustifiedOperators().length;
                        sumOutputMakespan = sumOutputMakespan + outputMakespan;
                        runtimeSum = runtimeSum + runtime;
                    }
                }
            }
             if(compactStats){
                 if(modus==0 || modus==1){
                     float makespanRatio = (float) (minMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (minOperations) / (float) (ip.getOperators().length);
                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + minOperations, "" + minMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Well Justification on PO-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) minOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) minMakespan);
                     if ((ip.getOperators().length - (int) minOperations) > 0 || (inputMakespan - (int) minMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) minMakespan) > 0) {
                         mkspnOptimizedProblems++;
                         avrgMkSpanOptimiziation=avrgMkSpanOptimiziation+(float)minMakespan/(float)inputMakespan;
                     }
                     if ((ip.getOperators().length - (int) minOperations) > 0) {
                         opOptimizedProblems++;
                         avrgOpOptimization=avrgOpOptimization+(float)minOperations/(float)ip.getOperators().length;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
                 if(modus==2) {
                     float averageOutputOperations = (float) sumOutputOperations / (float) reps;
                     float averageOutputMakespan = (float) sumOutputMakespan / (float) reps;
                     float makespanRatio = (float) (averageOutputMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (averageOutputOperations) / (float) (ip.getOperators().length);
                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + averageOutputOperations, "" + averageOutputMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Well Justification on PO-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) averageOutputOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) averageOutputMakespan);
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0 || (inputMakespan - (int) averageOutputMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) averageOutputMakespan) > 0) {
                         mkspnOptimizedProblems++;
                     }
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0) {
                         opOptimizedProblems++;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void wellJustificationOnPOCLtoCSV(InputParser ip,BufferedWriter bw,long RandomSeed,int reps,boolean compactStats,int modus){
        try {
            int sumOutputOperations=0;
            int sumOutputMakespan=0;
            int minOperations=0;
            int minMakespan=0;
            float runtimeSum=0;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            for(int i=0;i<reps;i++) {
                RandomSeed=i;
                long startTime = System.nanoTime();
                Well_Justification wJust = new Well_Justification(ip.getOperators(), ip.getCausalLinks(), ip.getOrderingConstraints(), RandomSeed);
                if(ip.getOperators().length!=wJust.getWellJustifiedOperators().length){
                    //System.out.println("Optimization");
                }
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int outputMakespan = Utillity.calculateMakespan(wJust.getWellJustifiedOperators(),wJust.getWellJustifiedOC());
                float makespanRatio = (float)(outputMakespan)/(float)(inputMakespan);
                float operatorRatio = (float)(wJust.getWellJustifiedOperators().length)/(float)(ip.getOperators().length);
                if(!compactStats){
                    CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length,""+inputMakespan, "" + wJust.getWellJustifiedOperators().length,""+outputMakespan,
                            "" +operatorRatio,""+ makespanRatio, "" + runtime,""+RandomSeed,"Well Justification on POCL-Plan"));
                }else{
                    if(modus==0){
                        if(minOperations==0 || minOperations>wJust.getWellJustifiedOperators().length){
                            minOperations=wJust.getWellJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==1){
                        if(minMakespan==0 || minMakespan>outputMakespan){
                            minOperations=wJust.getWellJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==2) {
                        sumOutputOperations = sumOutputOperations + wJust.getWellJustifiedOperators().length;
                        sumOutputMakespan = sumOutputMakespan + outputMakespan;
                        runtimeSum = runtimeSum + runtime;
                    }
                }
            }
             if(compactStats) {
                 if (modus == 0 || modus == 1) {
                     float makespanRatio = (float) (minMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (minOperations) / (float) (ip.getOperators().length);


                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + minOperations, "" + minMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Well Justification on POCL-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) minOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) minMakespan);
                     if ((ip.getOperators().length - (int) minOperations) > 0 || (inputMakespan - (int) minMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) minMakespan) > 0) {
                         mkspnOptimizedProblems++;
                         avrgMkSpanOptimiziation=avrgMkSpanOptimiziation+(float)minMakespan/(float)inputMakespan;
                     }
                     if ((ip.getOperators().length - (int) minOperations) > 0) {
                         opOptimizedProblems++;
                         avrgOpOptimization=avrgOpOptimization+(float)minOperations/(float)ip.getOperators().length;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
                 if (modus == 2) {
                     float averageOutputOperations = (float) sumOutputOperations / (float) reps;
                     float averageOutputMakespan = (float) sumOutputMakespan / (float) reps;
                     float makespanRatio = (float) (averageOutputMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (averageOutputOperations) / (float) (ip.getOperators().length);
                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + averageOutputOperations, "" + averageOutputMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Well Justification on POCL-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) averageOutputOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) averageOutputMakespan);
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0 || (inputMakespan - (int) averageOutputMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) averageOutputMakespan) > 0) {
                         mkspnOptimizedProblems++;
                     }
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0) {
                         opOptimizedProblems++;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
             }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void greedyJustificationOnPOtoCSV(InputParser ip,BufferedWriter bw, long RandomSeed,int reps,boolean compactStats,int modus){
        try {
            int sumOutputOperations=0;
            int sumOutputMakespan=0;
            int minOperations=0;
            int minMakespan=0;
            float runtimeSum=0;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            for(int i=0;i<reps;i++) {
                RandomSeed=i;
                long startTime = System.nanoTime();
                Greedy_Justification gJust = new Greedy_Justification(ip.getOperators(), ip.getOrderingConstraints(), RandomSeed);
                if(ip.getOperators().length!=gJust.getGreedyJustifiedOperators().length){
                    //System.out.println("Optimization");
                }
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int outputMakespan = Utillity.calculateMakespan(gJust.getGreedyJustifiedOperators(),gJust.getGreedyJustifiedOrderingConstraints());
                float makespanRatio = (float)(outputMakespan)/(float)(inputMakespan);
                float operatorRatio = (float)(gJust.getGreedyJustifiedOperators().length)/(float)(ip.getOperators().length);
                if(!compactStats){
                    CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length,""+inputMakespan, "" + gJust.getGreedyJustifiedOperators().length,""+outputMakespan,
                            "" +operatorRatio,""+makespanRatio,"" + runtime,""+RandomSeed,"Greedy Justification on PO-Plan"));
                }else{
                    if(modus==0){
                        if(minOperations==0 || minOperations>gJust.getGreedyJustifiedOperators().length){
                            if(minOperations !=0){
                                System.out.println("Lel");
                            }
                            minOperations=gJust.getGreedyJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==1){
                        if(minMakespan==0 || minMakespan>outputMakespan){
                            minOperations=gJust.getGreedyJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==2) {
                        sumOutputOperations = sumOutputOperations + gJust.getGreedyJustifiedOperators().length;
                        sumOutputMakespan = sumOutputMakespan + outputMakespan;
                        runtimeSum = runtimeSum + runtime;
                    }
                }
            }
            if(compactStats){
                 if (modus == 0 || modus == 1) {
                     float makespanRatio = (float) (minMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (minOperations) / (float) (ip.getOperators().length);

                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + minOperations, "" + minMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Greedy Justification on PO-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) minOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) minMakespan);
                     if ((ip.getOperators().length - (int) minOperations) > 0 || (inputMakespan - (int) minMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) minMakespan) > 0) {
                         mkspnOptimizedProblems++;
                         avrgMkSpanOptimiziation=avrgMkSpanOptimiziation+(float)minMakespan/(float)inputMakespan;
                     }
                     if ((ip.getOperators().length - (int) minOperations) > 0) {
                         opOptimizedProblems++;
                         avrgOpOptimization=avrgOpOptimization+(float)minOperations/(float)ip.getOperators().length;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
                 if (modus == 2) {
                     float averageOutputOperations = (float) sumOutputOperations / (float) reps;
                     float averageOutputMakespan = (float) sumOutputMakespan / (float) reps;
                     float makespanRatio = (float) (averageOutputMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (averageOutputOperations) / (float) (ip.getOperators().length);
                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + averageOutputOperations, "" + averageOutputMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Greedy Justification on PO-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) averageOutputOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) averageOutputMakespan);
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0 || (inputMakespan - (int) averageOutputMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) averageOutputMakespan) > 0) {
                         mkspnOptimizedProblems++;
                     }
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0) {
                         opOptimizedProblems++;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void greedyJustificationOnPOCLtoCSV(InputParser ip,BufferedWriter bw, long RandomSeed, int reps,boolean compactStats,int modus){
        try {
            int sumOutputOperations=0;
            int sumOutputMakespan=0;
            int minOperations=0;
            int minMakespan=0;
            float runtimeSum=0;
            int inputMakespan = Utillity.calculateMakespan(ip.getOperators(),ip.getOrderingConstraints());
            for(int i=0;i<reps;i++) {
                RandomSeed=i;
                long startTime = System.nanoTime();
                Greedy_Justification gJust = new Greedy_Justification(ip.getOperators(), ip.getCausalLinks(), ip.getOrderingConstraints(), RandomSeed);
                if(ip.getOperators().length!=gJust.getGreedyJustifiedOperators().length){
                    //System.out.println("Optimization");
                }
                long endTime = System.nanoTime();
                float runtime = (float) (endTime - startTime) / 1000000.0f;
                int outputMakespan = Utillity.calculateMakespan(gJust.getGreedyJustifiedOperators(),gJust.getGreedyJustifiedOrderingConstraints());
                float makespanRatio = (float)(outputMakespan)/(float)(inputMakespan);
                float operatorRatio = (float)(gJust.getGreedyJustifiedOperators().length)/(float)(ip.getOperators().length);
                if(!compactStats){
                    CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length,""+inputMakespan, "" + gJust.getGreedyJustifiedOperators().length,""+outputMakespan,
                            "" +operatorRatio,""+makespanRatio ,"" + runtime,""+RandomSeed,"Greedy Justification on POCL-Plan"));
                }else{
                    if(modus==0){
                        if(minOperations==0 || minOperations>gJust.getGreedyJustifiedOperators().length){
                            minOperations=gJust.getGreedyJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==1){
                        if(minMakespan==0 || minMakespan>outputMakespan){
                            minOperations=gJust.getGreedyJustifiedOperators().length;
                            minMakespan=outputMakespan;
                            runtimeSum=runtimeSum+runtime;
                        }
                    }
                    if(modus==2) {
                        sumOutputOperations = sumOutputOperations + gJust.getGreedyJustifiedOperators().length;
                        sumOutputMakespan = sumOutputMakespan + outputMakespan;
                        runtimeSum = runtimeSum + runtime;
                    }
                }
            }
            if(compactStats){
                if (modus == 0 || modus == 1) {
                     float makespanRatio = (float) (minMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (minOperations) / (float) (ip.getOperators().length);


                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + minOperations, "" + minMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Greedy Justification on POCL-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) minOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) minMakespan);
                     if ((ip.getOperators().length - (int) minOperations) > 0 || (inputMakespan - (int) minMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) minMakespan) > 0) {
                         mkspnOptimizedProblems++;
                         avrgMkSpanOptimiziation=avrgMkSpanOptimiziation+(float)minMakespan/(float)inputMakespan;
                     }
                     if ((ip.getOperators().length - (int) minOperations) > 0) {
                         opOptimizedProblems++;
                         avrgOpOptimization=avrgOpOptimization+(float)minOperations/(float)ip.getOperators().length;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
                 if (modus == 2) {
                     float averageOutputOperations = (float) sumOutputOperations / (float) reps;
                     float averageOutputMakespan = (float) sumOutputMakespan / (float) reps;
                     float makespanRatio = (float) (averageOutputMakespan) / (float) (inputMakespan);
                     float operatorRatio = (float) (averageOutputOperations) / (float) (ip.getOperators().length);
                     CSVUtils.writeLine(bw, Arrays.asList(ip.getDomain(), ip.getProblem(), "" + ip.getOperators().length, "" + inputMakespan, "" + averageOutputOperations, "" + averageOutputMakespan,
                             "" + operatorRatio, "" + makespanRatio, "" + runtimeSum, "Greedy Justification on POCL-Plan"));
                     overallOperators = overallOperators + ip.getOperators().length;
                     overallMakespan = overallMakespan + inputMakespan;
                     overallProblems++;
                     optimizedOperators = optimizedOperators + (ip.getOperators().length - (int) averageOutputOperations);
                     optimizedMakespan = optimizedMakespan + (inputMakespan - (int) averageOutputMakespan);
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0 || (inputMakespan - (int) averageOutputMakespan) > 0) {
                         optimizedProblems++;
                     }
                     if ((inputMakespan - (int) averageOutputMakespan) > 0) {
                         mkspnOptimizedProblems++;
                     }
                     if ((ip.getOperators().length - (int) averageOutputOperations) > 0) {
                         opOptimizedProblems++;
                     }
                     computationTime = computationTime + runtimeSum;
                 }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void testGreedyJustificationOnPO(InputParser ip){
         Greedy_Justification test = new Greedy_Justification(ip.getOperators(),ip.getOrderingConstraints(),10);
       System.out.println("Operatorcount: " +test.getGreedyJustifiedOperators().length);
       for(int i=0;i<test.getGreedyJustifiedOperators().length;i++){
           System.out.println(test.getGreedyJustifiedOperators()[i].getLabel());
       }
       System.out.println("OrderingConstraintsCount: "+ test.getGreedyJustifiedOrderingConstraints().length);
       for(int i=0;i<test.getGreedyJustifiedOrderingConstraints().length;i++){
           System.out.println(test.getGreedyJustifiedOrderingConstraints()[i][0] + " " + test.getGreedyJustifiedOrderingConstraints()[i][1]);
       }
    }
    public static void testWellJustificationOnPO(InputParser ip){
         Well_Justification test = new Well_Justification(ip.getOperators(),ip.getOrderingConstraints(),10);
       System.out.println("Operatorcount: " +test.getWellJustifiedOperators().length);
       for(int i=0;i<test.getWellJustifiedOperators().length;i++){
           System.out.println(test.getWellJustifiedOperators()[i].getLabel());
       }
       System.out.println("OrderingConstraintsCount: "+ test.getWellJustifiedOC().length);
       for(int i=0;i<test.getWellJustifiedOC().length;i++){
           System.out.println(test.getWellJustifiedOC()[i][0] + " " + test.getWellJustifiedOC()[i][1]);
       }
    }

    public static void testWellJustificationOnPOCL(InputParser ip){
        Well_Justification test = new Well_Justification(ip.getOperators(),ip.getCausalLinks(),ip.getOrderingConstraints(),10);
       System.out.println("Operatorcount: " +test.getWellJustifiedOperators().length);
       for(int i=0;i<test.getWellJustifiedOperators().length;i++){
           System.out.println(test.getWellJustifiedOperators()[i].getLabel());
       }
       System.out.println("CausalLinkcount: "+test.getWellJustifiedCL().length);
       for(int i=0;i<test.getWellJustifiedCL().length;i++){
           System.out.println(test.getWellJustifiedCL()[i].getProducer()+ " "+ test.getWellJustifiedCL()[i].getLiteral() + " "+ test.getWellJustifiedCL()[i].getConsumer());
       }
       System.out.println("OrderingConstraintsCount: "+ test.getWellJustifiedOC().length);
       for(int i=0;i<test.getWellJustifiedOC().length;i++){
           System.out.println(test.getWellJustifiedOC()[i][0] + " " + test.getWellJustifiedOC()[i][1]);
       }
    }
    public static void testGreedyJustificationOnPOCL(InputParser ip){
        Greedy_Justification test = new Greedy_Justification(ip.getOperators(),ip.getCausalLinks(),ip.getOrderingConstraints(),10);
       System.out.println("Operatorcount: " +test.getGreedyJustifiedOperators().length);
       for(int i=0;i<test.getGreedyJustifiedOperators().length;i++){
           System.out.println(test.getGreedyJustifiedOperators()[i].getLabel());
       }
       System.out.println("CausalLinkcount: "+test.getGreedyJustifiedCausalLinks().length);
       for(int i=0;i<test.getGreedyJustifiedCausalLinks().length;i++){
           System.out.println(test.getGreedyJustifiedCausalLinks()[i].getProducer()+ " "+ test.getGreedyJustifiedCausalLinks()[i].getLiteral() + " "+ test.getGreedyJustifiedCausalLinks()[i].getConsumer());
       }
       System.out.println("OrderingConstraintsCount: "+ test.getGreedyJustifiedOrderingConstraints().length);
       for(int i=0;i<test.getGreedyJustifiedOrderingConstraints().length;i++){
           System.out.println(test.getGreedyJustifiedOrderingConstraints()[i][0] + " " + test.getGreedyJustifiedOrderingConstraints()[i][1]);
       }
    }
    public static void testBackwardJustificationOnPO(InputParser ip){
        Backward_Justification test= new Backward_Justification(ip.getOperators(),ip.getOrderingConstraints(),1000);
       System.out.println("Operatorcount: "+test.getBackwardJustifiedOperators().length);
       for(int i=0;i<test.getBackwardJustifiedOperators().length;i++){
           System.out.println(test.getBackwardJustifiedOperators()[i].getLabel());
       }
       System.out.println("OrderingConstraintsCount: "+ test.getBackwardJustifiedOrderingConstraints().length);
       for(int i=0;i<test.getBackwardJustifiedOrderingConstraints().length;i++){
           System.out.println(test.getBackwardJustifiedOrderingConstraints()[i][0] + " " + test.getBackwardJustifiedOrderingConstraints()[i][1]);
       }
    }
public int getOverallProblems() {
        return overallProblems;
    }

    public void setOverallProblems(int overallProblems) {
        this.overallProblems = overallProblems;
    }

    public int getOpitmizedProblems() {
        return optimizedProblems;
    }

    public void setOpitmizedProblems(int opitmizedProblems) {
        this.optimizedProblems = opitmizedProblems;
    }

    public int getOpOptimizedProblems() {
        return opOptimizedProblems;
    }

    public void setOpOptimizedProblems(int opOptimizedProblems) {
        this.opOptimizedProblems = opOptimizedProblems;
    }

    public int getMkspnOptimizedProblems() {
        return mkspnOptimizedProblems;
    }

    public void setMkspnOptimizedProblems(int mkspnOptimizedProblems) {
        this.mkspnOptimizedProblems = mkspnOptimizedProblems;
    }

    public int getOverallOperators() {
        return overallOperators;
    }

    public void setOverallOperators(int overallOperators) {
        this.overallOperators = overallOperators;
    }

    public int getOverallMakespan() {
        return overallMakespan;
    }

    public void setOverallMakespan(int overallMakespan) {
        this.overallMakespan = overallMakespan;
    }

    public int getOptimizedOperators() {
        return optimizedOperators;
    }

    public void setOptimizedOperators(int optimizedOperators) {
        this.optimizedOperators = optimizedOperators;
    }

    public int getOptimizedMakespan() {
        return optimizedMakespan;
    }

    public void setOptimizedMakespan(int optimizedMakespan) {
        this.optimizedMakespan = optimizedMakespan;
    }

    public float getComputationTime() {
        return computationTime;
    }

    public void setComputationTime(float computationTime) {
        this.computationTime = computationTime;
    }
    public void lineNumberPlusPlus(){
        this.lineNumber=this.lineNumber++;
    }

}
