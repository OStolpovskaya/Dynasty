package dyn;

import org.junit.Test;

/**
 * Created by OM on 03.03.2017.
 */
public class UsefulTest {
    @Test
    public void test1() {

        int[] am = new int[7];
        for (int i = 0; i < am.length; i++) {
            am[i] = 0;
        }

        for (int i = 0; i < 10000; i++) {
            int amountOfChildren = getAmountOfChildren();
            am[amountOfChildren - 1] = am[amountOfChildren - 1] + 1;
        }

        for (int i = 0; i < am.length; i++) {
            System.out.println((i + 1) + ":" + am[i]);
        }
    }

    public int getAmountOfChildren() {
        //double[] percentage = new double[] {0.25, 0.55, 0.80, 0.90, 0.95, 0.98, 1.00}; // normal
        //double[] percentage = new double[] {0.05, 0.15, 0.30, 0.50, 0.75, 0.90, 1.00}; // buff fertility
        double[] percentage = new double[]{0.00, 0.00, 0.00, 0.00, 1.00, 0.00, 0.00}; // buff 5 children

        double random = Math.random();
        if (0 <= random && random < percentage[0]) {
            return 1;
        } else if (percentage[0] <= random && random < percentage[1]) {
            return 2;
        } else if (percentage[1] <= random && random < percentage[2]) {
            return 3;
        } else if (percentage[2] <= random && random < percentage[3]) {
            return 4;
        } else if (percentage[3] <= random && random < percentage[4]) {
            return 5;
        } else if (percentage[4] <= random && random < percentage[5]) {
            return 6;
        } else if (percentage[5] <= random && random < percentage[6]) {
            return 7;
        }

        return 1;
    }


}
