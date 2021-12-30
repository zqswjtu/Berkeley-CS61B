package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AList<Integer> aList = new AList<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();
        for (int i = 0; i < 3; ++i) {
            aList.addLast(i);
            buggyAList.addLast(i);
        }
        aList.removeLast();
        buggyAList.removeLast();

        assert aList.size() == buggyAList.size();
        for (int i = 0; i < aList.size(); i++) {
            assertEquals(aList.get(i), buggyAList.get(i));
        }

        aList.removeLast();
        buggyAList.removeLast();

        assert aList.size() == buggyAList.size();
        for (int i = 0; i < aList.size(); i++) {
            assertEquals(aList.get(i), buggyAList.get(i));
        }

        aList.removeLast();
        buggyAList.removeLast();

        assert aList.size() == buggyAList.size();
        for (int i = 0; i < aList.size(); i++) {
            assertEquals(aList.get(i), buggyAList.get(i));
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("AListNoResizing L addLast(" + randVal + ")");

                B.addLast(randVal);
                System.out.println("BuggyAList B addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("AListNoResizing L size: " + size);

                System.out.println("BuggyAList B size: " + B.size());
            } else if (operationNumber == 2) {
                //getLast
                if (L.size() > 0){
                    System.out.println("AListNoResizing L getLast(" + L.getLast() + ")");
                }

                if (B.size() > 0){
                    System.out.println("BuggyAList B getLast(" + B.getLast() + ")");
                }
            } else if (operationNumber == 3) {
                //removeLast
                if (L.size() > 0) {
                    System.out.println("AListNoResizing L removeLast(" + L.removeLast() + ")");
                }

                if (B.size() > 0) {
                    System.out.println("BuggyAList B removeLast(" + B.removeLast() + ")");
                }
            }
        }
    }

    @Test
    public void test(){
        BuggyAList<Integer> list = new BuggyAList<>();
        for (int i = 0; i < 17; i++) {
            list.addLast(i);
        }
        for (int i = 0; i < 12; i++) {
            if(list.size() > 0){
                list.removeLast();
            }
        }
    }
}
