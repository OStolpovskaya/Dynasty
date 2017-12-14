package dyn;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void Test2() {
        List<String> resources = new ArrayList<>();
        if (5 > 0) resources.add("'Дерево'");
        if (0 > 0) resources.add("'Металл'");
        if (0 > 0) resources.add("'Пластик, резина'");
        if (5 > 0) resources.add("'Микроэлектроника'");
        if (0 > 0) resources.add("'Ткань, кожа, бумага'");
        if (1 > 0) resources.add("'Камень, стекло, керамика'");
        if (0 > 0) resources.add("'Химия, краски'");
        System.out.println(StringUtils.arrayToDelimitedString(resources.toArray(), ", "));
        System.out.println(Arrays.toString(resources.toArray()));
    }

    //int featureToModify = (int) (1 + Math.random() * 12);
    @Test
    public void Test3() {
        int c = 10000;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(i, 0);
        }
        for (int i = 0; i < c; i++) {
            int featureToModify = (int) (1 + Math.random() * 12);
            list.set(featureToModify - 1, list.get(featureToModify - 1) + 1);
        }
        System.out.println(list.toString());
    }

    @Test
    public void Test4() {
        //app.body.fine_with_butterfly_wings
        String name = "app.body.fine_with_butterfly_wings";
        String substring = name.substring(0, name.lastIndexOf("."));
        System.out.println("substring = " + substring);
        substring = name.substring(name.lastIndexOf("."));
        System.out.println("substring = " + substring);
    }
}
