package tests;

import com.company.Backward_Justification;
import com.company.Greedy_Justification;
import com.company.InputParser;
import com.company.Well_Justification;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class JUnitTests {
    @Test
    public void backwardJustificationTest() throws Exception{
        //backwardJustificationTest("input/gripper.log","tests/solutions/gripper.log");
        //backwardJustificationTest("tests/problems/money.log","tests/solutions/money1.log","tests/solutions/money2.log");
        backwardJustificationTest("tests/problems/flight1_POCL.txt","tests/solutions/BackwardJustification/flight1_PO.txt");
    }
    public void backwardJustificationTest(String inputFile,String outputFile) throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Backward_Justification bj = new Backward_Justification(ip.getOperators(), ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            assertEquals(bj.getBackwardJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < bj.getBackwardJustifiedOperators().length; i++) {
                assertEquals(ip2.getOperators()[i].getLabel(), bj.getBackwardJustifiedOperators()[i].getLabel());
                assertEquals(ip2.getOperators()[i].getOpNummer(), bj.getBackwardJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < bj.getBackwardJustifiedOrderingConstraints().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], bj.getBackwardJustifiedOrderingConstraints()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], bj.getBackwardJustifiedOrderingConstraints()[i][1]);
            }*/
        }
    }
    public void backwardJustificationTest(String inputFile,String outputFile1,String outputFile2) throws Exception{
        FileInputStream inputStream = new FileInputStream(new File(inputFile));
        System.setIn(inputStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        InputParser ip = new InputParser(br);
        for(int n=0;n<10;n++) {
            Backward_Justification bj = new Backward_Justification(ip.getOperators(), ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile1));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            FileInputStream inputStream2 = new FileInputStream(new File(outputFile2));
            System.setIn(inputStream2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip3= new InputParser(br2);
            assertTrue(bj.getBackwardJustifiedOperators().length==ip2.getOperators().length ||
                    bj.getBackwardJustifiedOperators().length==ip3.getOperators().length);
            //assertEquals(bj.getBackwardJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < bj.getBackwardJustifiedOperators().length; i++) {
                assertTrue(bj.getBackwardJustifiedOperators()[i].getLabel().equals(ip2.getOperators()[i].getLabel()) ||
                        bj.getBackwardJustifiedOperators()[i].getLabel().equals(ip3.getOperators()[i].getLabel()));
                assertTrue(bj.getBackwardJustifiedOperators()[i].getOpNummer()==ip2.getOperators()[i].getOpNummer() ||
                        bj.getBackwardJustifiedOperators()[i].getOpNummer()==ip3.getOperators()[i].getOpNummer());
            }
            for (int i = 0; i < bj.getBackwardJustifiedOrderingConstraints().length; i++) {
                assertTrue(bj.getBackwardJustifiedOrderingConstraints()[i][0]==ip2.getOrderingConstraints()[i][0] ||
                        bj.getBackwardJustifiedOrderingConstraints()[i][0]==ip3.getOrderingConstraints()[i][0]);
                assertTrue(bj.getBackwardJustifiedOrderingConstraints()[i][1]==ip2.getOrderingConstraints()[i][1] ||
                        bj.getBackwardJustifiedOrderingConstraints()[i][1]==ip3.getOrderingConstraints()[i][1]);
            }
        }
    }
    @Test
    public void wellJustificationPOTest() throws Exception{
        //wellJustificationPOTest("input/gripper.log","tests/solutions/gripper.log");
        wellJustificationPOTest("tests/problems/flight1_POCL.txt","tests/solutions/WellJustification/flight1_PO.txt","tests/solutions/WellJustification/flight2_PO.txt");
    }
    public void wellJustificationPOTest(String inputFile,String outputFile)throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Well_Justification wj = new Well_Justification(ip.getOperators(), ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            assertEquals(wj.getWellJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < wj.getWellJustifiedOperators().length; i++) {
                assertEquals(ip2.getOperators()[i].getLabel(), wj.getWellJustifiedOperators()[i].getLabel());
                assertEquals(ip2.getOperators()[i].getOpNummer(), wj.getWellJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < wj.getWellJustifiedOC().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], wj.getWellJustifiedOC()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], wj.getWellJustifiedOC()[i][1]);
            }*/
        }
    }
    public void wellJustificationPOTest(String inputFile,String outputFile1,String outputFile2)throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Well_Justification wj = new Well_Justification(ip.getOperators(), ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile1));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
             FileInputStream inputStream2 = new FileInputStream(new File(outputFile2));
            System.setIn(inputStream2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip3 = new InputParser(br2);
            assertTrue(wj.getWellJustifiedOperators().length==ip2.getOperators().length ||
                    wj.getWellJustifiedOperators().length== ip3.getOperators().length);
            for (int i = 0; i < wj.getWellJustifiedOperators().length; i++) {
                assertTrue(ip2.getOperators()[i].getLabel().equals(wj.getWellJustifiedOperators()[i].getLabel())||
                        ip3.getOperators()[i].getLabel().equals(wj.getWellJustifiedOperators()[i].getLabel()));
                assertTrue(ip2.getOperators()[i].getOpNummer()==wj.getWellJustifiedOperators()[i].getOpNummer()||
                        ip3.getOperators()[i].getOpNummer()==wj.getWellJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < wj.getWellJustifiedOC().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], wj.getWellJustifiedOC()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], wj.getWellJustifiedOC()[i][1]);
            }*/
        }
    }
     @Test
    public void wellJustificationPOCLTest() throws Exception{
        //wellJustificationPOCLTest("input/gripper.log","tests/solutions/gripper.log");
         //wellJustificationPOCLTest("tests/problems/flight1_POCL.txt","tests/solutions/WellJustification/flight2_PO.txt");
         wellJustificationPOCLTest("tests/problems/flight1_POCL.txt","tests/solutions/WellJustification/flight1_PO.txt","tests/solutions/WellJustification/flight2_PO.txt");
    }
    public void wellJustificationPOCLTest(String inputFile,String outputFile)throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Well_Justification wj = new Well_Justification(ip.getOperators(),ip.getCausalLinks() ,ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            assertEquals(wj.getWellJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < wj.getWellJustifiedOperators().length; i++) {
                assertEquals(ip2.getOperators()[i].getLabel(), wj.getWellJustifiedOperators()[i].getLabel());
                assertEquals(ip2.getOperators()[i].getOpNummer(), wj.getWellJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < wj.getWellJustifiedOC().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], wj.getWellJustifiedOC()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], wj.getWellJustifiedOC()[i][1]);
            }
            for(int i=0;i<wj.getWellJustifiedCL().length;i++){
                assertEquals(ip2.getCausalLinks()[i].getProducer(),wj.getWellJustifiedCL()[i].getProducer());
                assertEquals(ip2.getCausalLinks()[i].getLiteral(),wj.getWellJustifiedCL()[i].getLiteral());
                assertEquals(ip2.getCausalLinks()[i].getConsumer(),wj.getWellJustifiedCL()[i].getConsumer());
            }*/
        }
    }
    public void wellJustificationPOCLTest(String inputFile,String outputFile1,String outputFile2)throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Well_Justification wj = new Well_Justification(ip.getOperators(),ip.getCausalLinks() ,ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile1));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            FileInputStream inputStream2 = new FileInputStream(new File(outputFile2));
            System.setIn(inputStream2);
            BufferedReader br2= new BufferedReader(new InputStreamReader(System.in));
            InputParser ip3= new InputParser(br2);
            assertTrue(wj.getWellJustifiedOperators().length==ip2.getOperators().length ||
                    wj.getWellJustifiedOperators().length== ip3.getOperators().length);
            for (int i = 0; i < wj.getWellJustifiedOperators().length; i++) {
                assertTrue(ip2.getOperators()[i].getLabel().equals(wj.getWellJustifiedOperators()[i].getLabel())||
                        ip3.getOperators()[i].getLabel().equals(wj.getWellJustifiedOperators()[i].getLabel()));
                assertTrue(ip2.getOperators()[i].getOpNummer()==wj.getWellJustifiedOperators()[i].getOpNummer()||
                        ip3.getOperators()[i].getOpNummer()==wj.getWellJustifiedOperators()[i].getOpNummer());

            }
            /*for (int i = 0; i < wj.getWellJustifiedOC().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], wj.getWellJustifiedOC()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], wj.getWellJustifiedOC()[i][1]);
            }
            for(int i=0;i<wj.getWellJustifiedCL().length;i++){
                assertEquals(ip2.getCausalLinks()[i].getProducer(),wj.getWellJustifiedCL()[i].getProducer());
                assertEquals(ip2.getCausalLinks()[i].getLiteral(),wj.getWellJustifiedCL()[i].getLiteral());
                assertEquals(ip2.getCausalLinks()[i].getConsumer(),wj.getWellJustifiedCL()[i].getConsumer());
            }*/
        }
    }

    @Test
    public void greedyJustificationPOTest() throws Exception{
        //greedyJustificationPOTest("input/gripper.log","tests/solutions/gripper.log");
        greedyJustificationPOCLTest("tests/problems/flight2_POCL.txt","tests/solutions/GreedyJustification/flight1_POCL.txt");
    }
    public void greedyJustificationPOTest(String inputFile,String outputFile)throws Exception{

        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Greedy_Justification gj = new Greedy_Justification(ip.getOperators(), ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            assertEquals(gj.getGreedyJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < gj.getGreedyJustifiedOperators().length; i++) {
                assertEquals(ip2.getOperators()[i].getLabel(), gj.getGreedyJustifiedOperators()[i].getLabel());
                assertEquals(ip2.getOperators()[i].getOpNummer(), gj.getGreedyJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < gj.getGreedyJustifiedOrderingConstraints().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], gj.getGreedyJustifiedOrderingConstraints()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], gj.getGreedyJustifiedOrderingConstraints()[i][1]);
            }*/
        }
    }     @Test
    public void greedyJustificationPOCLTest() throws Exception{
        //greedyJustificationPOCLTest("input/gripper.log","tests/solutions/gripper.log");
    }
    public void greedyJustificationPOCLTest(String inputFile,String outputFile)throws Exception{
        for(int n=0;n<10;n++) {
            FileInputStream inputStream = new FileInputStream(new File(inputFile));
            System.setIn(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip = new InputParser(br);
            Greedy_Justification gj = new Greedy_Justification(ip.getOperators(),ip.getCausalLinks() ,ip.getOrderingConstraints(), n);
            FileInputStream inputStream1 = new FileInputStream(new File(outputFile));
            System.setIn(inputStream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            InputParser ip2 = new InputParser(br1);
            assertEquals(gj.getGreedyJustifiedOperators().length,ip2.getOperators().length);
            for (int i = 0; i < gj.getGreedyJustifiedOperators().length; i++) {
                assertEquals(ip2.getOperators()[i].getLabel(), gj.getGreedyJustifiedOperators()[i].getLabel());
                assertEquals(ip2.getOperators()[i].getOpNummer(), gj.getGreedyJustifiedOperators()[i].getOpNummer());
            }
            /*for (int i = 0; i < gj.getGreedyJustifiedOrderingConstraints().length; i++) {
                assertEquals(ip2.getOrderingConstraints()[i][0], gj.getGreedyJustifiedOrderingConstraints()[i][0]);
                assertEquals(ip2.getOrderingConstraints()[i][1], gj.getGreedyJustifiedOrderingConstraints()[i][1]);
            }
            for(int i=0;i<gj.getGreedyJustifiedCausalLinks().length;i++){
                assertEquals(ip2.getCausalLinks()[i].getProducer(),gj.getGreedyJustifiedCausalLinks()[i].getProducer());
                assertEquals(ip2.getCausalLinks()[i].getLiteral(),gj.getGreedyJustifiedCausalLinks()[i].getLiteral());
                assertEquals(ip2.getCausalLinks()[i].getConsumer(),gj.getGreedyJustifiedCausalLinks()[i].getConsumer());
            }*/
        }
    }

}
