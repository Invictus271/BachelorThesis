package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OutputParser {
    public OutputParser(BufferedWriter domainWriter, BufferedWriter problemWriter, InputParser ip)throws IOException{
        writeDomain(domainWriter, ip);
        writeProblem(problemWriter,ip);
    }

    public void writeDomain(BufferedWriter domainWriter,InputParser ip)throws IOException {
        domainWriter.write("(define (domain "+ ip.getDomain() +")\n");
        domainWriter.write("(:requirements :equality :typing :conditional-effects :negative-preconditions)\n");
        domainWriter.write("(:types object)\n");
        ArrayList<String> predicates = getPredicates(ip);
        domainWriter.write("(:predicates\n");
        for(int i=0;i<predicates.size();i++){
            domainWriter.write("    ("+ predicates.get(i)+")\n");
        }
        domainWriter.write(")\n");
        for(int i=0;i<ip.getOperators().length;i++) {
            domainWriter.write("    (:action " + ip.getOperators()[i].getLabel() + "\n");
            domainWriter.write("    :parameters()\n");
            if (!ip.getOperators()[i].getPreconditions()[0].equals("")) {
                domainWriter.write("    :precondition (and ");
                for (int n = 0; n < ip.getOperators()[i].getPreconditions().length; n++) {
                    domainWriter.write(" (" + ip.getOperators()[i].getPreconditions()[n] + ") ");
                }

                domainWriter.write(")\n");
            }
                domainWriter.write("    :effect(and ");
                for(int n=0;n<ip.getOperators()[i].getAddEffects().length;n++){
                    domainWriter.write(" ("+ip.getOperators()[i].getAddEffects()[n]+") ");
                }
                if(!ip.getOperators()[i].getDeleteEffects()[0].equals("")) {
                    for (int n = 0; n < ip.getOperators()[i].getDeleteEffects().length; n++) {
                        domainWriter.write(" (not (" + ip.getOperators()[i].getDeleteEffects()[n] + ")) ");
                    }
                }
            domainWriter.write(")\n");
            domainWriter.write(")\n");
        }
        domainWriter.write(")");
    }

    public  void writeProblem(BufferedWriter problemWriter, InputParser ip) throws IOException{
        problemWriter.write("(define (problem "+ ip.getProblem()+")\n");
        problemWriter.write("(:domain "+ip.getDomain()+")\n");
        problemWriter.write("(:objects \n");
        problemWriter.write("   o - object \n)\n");
        problemWriter.write("(:htn \n   :tasks (and\n");
        for(int i=0;i<ip.getOperators().length;i++){
            problemWriter.write("   ("+ip.getOperators()[i].getLabel()+")\n");
        }
        problemWriter.write("))\n\n");
        problemWriter.write("(:init \n");
        for(int i=0;i<ip.getInit().getAddEffects().length;i++){
            problemWriter.write("   ("+ip.getInit().getAddEffects()[i]+")\n");
        }
        problemWriter.write(")\n");
        problemWriter.write("(:goal (and \n");
        for(int i=0;i<ip.getGoal().getPreconditions().length;i++){
            problemWriter.write("   ("+ip.getGoal().getPreconditions()[i]+")\n");
        }
        problemWriter.write("))\n\n)");
    }

    public ArrayList<String> getPredicates(InputParser ip){
       ArrayList<String> predicates = new ArrayList<String>();
       for(int i=0;i< ip.getOperators().length;i++){
           for(int x=0;x<ip.getOperators()[i].getPreconditions().length;x++){
              if(!predicates.contains(ip.getOperators()[i].getPreconditions()[x])
              && !ip.getOperators()[i].getPreconditions()[x].equals("")){
                  predicates.add(ip.getOperators()[i].getPreconditions()[x]);
              }
           }
           for(int x=0;x<ip.getOperators()[i].getAddEffects().length;x++){
               if(!predicates.contains(ip.getOperators()[i].getAddEffects()[x])
               && !ip.getOperators()[i].getAddEffects()[x].equals("")){
                  predicates.add(ip.getOperators()[i].getAddEffects()[x]);
               }
           }
           for(int x=0;x<ip.getOperators()[i].getDeleteEffects().length;x++){
               if(!predicates.contains(ip.getOperators()[i].getDeleteEffects()[x])
               && !ip.getOperators()[i].getDeleteEffects()[x].equals("")){
                  predicates.add(ip.getOperators()[i].getDeleteEffects()[x]);
               }
           }
       }
       for(int x=0;x<ip.getInit().getAddEffects().length;x++){
           if(!predicates.contains((ip.getInit().getAddEffects())[x])&&
           !ip.getInit().getAddEffects()[0].equals("")){
               predicates.add(ip.getInit().getAddEffects()[x]);
           }
       }
       for(int x=0;x<ip.getGoal().getPreconditions().length;x++){
           if(!predicates.contains((ip.getGoal().getPreconditions())[x])&&
           !ip.getGoal().getPreconditions()[0].equals("")){
               predicates.add(ip.getGoal().getPreconditions()[x]);
           }
       }
       return predicates;
    }
}
