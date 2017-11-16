package dyn.utils;

import dyn.model.FamilyResources;
import dyn.service.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 07.04.2017.
 */
public class ResUtils {
    private Map<String, Integer> initValues;

    public static String getName(String res) {
        switch (res) {
            case "food":
                return Const.RES_FOOD_NAME;
            case "wood":
                return Const.RES_WOOD_NAME;
            case "metall":
                return Const.RES_METALL_NAME;
            case "plastic":
                return Const.RES_PLASTIC_NAME;
            case "microelectronics":
                return Const.RES_MICROELECTRONICS_NAME;
            case "cloth":
                return Const.RES_CLOTH_NAME;
            case "stone":
                return Const.RES_STONE_NAME;
            case "chemical":
                return Const.RES_CHEMICAL_NAME;
            default:
                return "";
        }

    }

    public static void copyResources(ResourcesHolder from, ResourcesHolder to) {
        to.setFood(from.getFood());
        to.setWood(from.getWood());
        to.setMetall(from.getMetall());
        to.setPlastic(from.getPlastic());
        to.setMicroelectronics(from.getMicroelectronics());
        to.setCloth(from.getCloth());
        to.setStone(from.getStone());
        to.setChemical(from.getChemical());
    }

    public static String getResString(ResourcesHolder resHolder) {
        return getResString(resHolder, 1);
    }

    public static String getResString(ResourcesHolder resHolder, int coeff) {
        List<String> resources = new ArrayList<>();
        if (resHolder.getFood() > 0) resources.add("<img class='resImage' title='" + Const.RES_FOOD_NAME + "' src='/graphics/resources/resources_02.png'/> " + resHolder.getFood() * coeff);
        if (resHolder.getWood() > 0) resources.add("<img class='resImage' title='" + Const.RES_WOOD_NAME + "' src='/graphics/resources/resources_03.png''/> " + resHolder.getWood() * coeff);
        if (resHolder.getMetall() > 0) resources.add("<img class='resImage' title='" + Const.RES_METALL_NAME + "' src='/graphics/resources/resources_04.png''/> " + resHolder.getMetall() * coeff);
        if (resHolder.getPlastic() > 0) resources.add("<img class='resImage' title='" + Const.RES_PLASTIC_NAME + "' src='/graphics/resources/resources_05.png''/> " + resHolder.getPlastic() * coeff);
        if (resHolder.getMicroelectronics() > 0) resources.add("<img class='resImage' title='" + Const.RES_MICROELECTRONICS_NAME + "' src='/graphics/resources/resources_06.png''/> " + resHolder.getMicroelectronics() * coeff);
        if (resHolder.getCloth() > 0) resources.add("<img class='resImage' title='" + Const.RES_CLOTH_NAME + "' src='/graphics/resources/resources_07.png''/> " + resHolder.getCloth() * coeff);
        if (resHolder.getStone() > 0) resources.add("<img class='resImage' title='" + Const.RES_STONE_NAME + "' src='/graphics/resources/resources_08.png''/> " + resHolder.getStone() * coeff);
        if (resHolder.getChemical() > 0) resources.add("<img class='resImage' title='" + Const.RES_CHEMICAL_NAME + "' src='/graphics/resources/resources_09.png''/>" + resHolder.getChemical() * coeff);
        return org.springframework.util.StringUtils.arrayToDelimitedString(resources.toArray(), ", ");
    }

    public static String getResString(ResourcesHolder resHolder, double coeff) {
        List<String> resources = new ArrayList<>();
        if (resHolder.getFood() > 0) resources.add("Продукты: " + Math.ceil(resHolder.getFood() * coeff));
        if (resHolder.getWood() > 0) resources.add("Дерево: " + Math.ceil(resHolder.getWood() * coeff));
        if (resHolder.getMetall() > 0) resources.add("Металл: " + Math.ceil(resHolder.getMetall() * coeff));
        if (resHolder.getPlastic() > 0) resources.add("Пластик, резина: " + Math.ceil(resHolder.getPlastic() * coeff));
        if (resHolder.getMicroelectronics() > 0) resources.add("Микроэлектроника: " + Math.ceil(resHolder.getMicroelectronics() * coeff));
        if (resHolder.getCloth() > 0) resources.add("Ткань, кожа, бумага: " + Math.ceil(resHolder.getCloth() * coeff));
        if (resHolder.getStone() > 0) resources.add("Камень, стекло, керамика: " + Math.ceil(resHolder.getStone() * coeff));
        if (resHolder.getChemical() > 0) resources.add("Химия, краски: " + Math.ceil(resHolder.getChemical() * coeff));
        return org.springframework.util.StringUtils.arrayToDelimitedString(resources.toArray(), ", ");
    }

    public static String differenceToString(ResourcesHolder previous, ResourcesHolder current) {
        ResourcesHolder resHolder = new FamilyResources();
        resHolder.setFood(current.getFood() - previous.getFood());
        resHolder.setWood(current.getWood() - previous.getWood());
        resHolder.setMetall(current.getMetall() - previous.getMetall());
        resHolder.setPlastic(current.getPlastic() - previous.getPlastic());
        resHolder.setMicroelectronics(current.getMicroelectronics() - previous.getMicroelectronics());
        resHolder.setCloth(current.getCloth() - previous.getCloth());
        resHolder.setStone(current.getStone() - previous.getStone());
        resHolder.setChemical(current.getChemical() - previous.getChemical());
        return getResString(resHolder);
    }

}
