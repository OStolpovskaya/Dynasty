package dyn;

import org.junit.Test;

/**
 * Created by OM on 03.03.2017.
 */
public class UsefulTest {
    @Test
    public void test1() {
        int min = 1;
        int max = 12;

        int[] am = new int[max];
        for (int i = 0; i < am.length; i++) {
            am[i] = 0;
        }

        for (int i = 0; i < 10000; i++) {
            int feature = (int) (min + Math.random() * max);
            am[feature - 1] = am[feature - 1] + 1;
        }

        for (int i = 0; i < am.length; i++) {
            System.out.println((i + 1) + ":" + am[i]);
        }
    }

}
