package dyn.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 07.04.2017.
 */
public class ResourcesUtils {
    public static String getResString(ResourcesHolder resHolder) {
        return getResString(resHolder, 1);
    }

    public static String getResString(ResourcesHolder resHolder, int coeff) {
        List<String> resources = new ArrayList<>();
        if (resHolder.getWood() > 0) resources.add("Дерево: " + resHolder.getWood() * coeff);
        if (resHolder.getMetall() > 0) resources.add("Металл: " + resHolder.getMetall() * coeff);
        if (resHolder.getPlastic() > 0) resources.add("Пластик, резина: " + resHolder.getPlastic() * coeff);
        if (resHolder.getMicroelectronics() > 0) resources.add("Микроэлектроника: " + resHolder.getMicroelectronics() * coeff);
        if (resHolder.getCloth() > 0) resources.add("Ткань, кожа, бумага: " + resHolder.getCloth() * coeff);
        if (resHolder.getStone() > 0) resources.add("Камень, стекло, керамика: " + resHolder.getStone() * coeff);
        if (resHolder.getChemical() > 0) resources.add("Химия, краски: " + resHolder.getChemical() * coeff);
        return org.springframework.util.StringUtils.arrayToDelimitedString(resources.toArray(), ", ");
    }
}
