package dyn.utils;

import dyn.model.Family;
import dyn.model.FamilyResources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 07.04.2017.
 */
public class ResourcesUtils {
    private Map<String, Integer> initValues;

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

    public void saveInitValues(Family family) {
        FamilyResources familyResources = family.getFamilyResources();
        initValues = new HashMap<>();
        initValues.put("Money", family.getMoney());
        initValues.put("Wood", familyResources.getWood());
        initValues.put("Metall", familyResources.getMetall());
        initValues.put("Plastic", familyResources.getPlastic());
        initValues.put("Microelectronics", familyResources.getMicroelectronics());
        initValues.put("Cloth", familyResources.getCloth());
        initValues.put("Stone", familyResources.getStone());
        initValues.put("Chemical", familyResources.getChemical());
    }

    public String getDifference(Family family) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Деньги: ").append(family.getMoney() - initValues.get("Money")).append(" р. <br>");
        sb.append(" Дерево: ").append(family.getFamilyResources().getWood() - initValues.get("Wood")).append(", ");
        sb.append(" Металл: ").append(family.getFamilyResources().getMetall() - initValues.get("Metall")).append(", ");
        sb.append(" Пластик, резина: ").append(family.getFamilyResources().getPlastic() - initValues.get("Plastic")).append(", ");
        sb.append(" Микроэлектроника: ").append(family.getFamilyResources().getMicroelectronics() - initValues.get("Microelectronics")).append(", ");
        sb.append(" Ткань, кожа, бумага: ").append(family.getFamilyResources().getCloth() - initValues.get("Cloth")).append(", ");
        sb.append(" Камень, стекло, керамика: ").append(family.getFamilyResources().getStone() - initValues.get("Stone")).append(", ");
        sb.append(" Химия, краски: ").append(family.getFamilyResources().getChemical() - initValues.get("Chemical")).append("<br>");
        return sb.toString();
    }
}
