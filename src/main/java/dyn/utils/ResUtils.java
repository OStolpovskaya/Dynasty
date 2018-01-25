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
        if (resHolder.getFood() > 0) resources.add(foodImg(resHolder.getFood() * coeff));
        if (resHolder.getWood() > 0) resources.add(woodImg(resHolder.getWood() * coeff));
        if (resHolder.getMetall() > 0) resources.add(metallImg(resHolder.getMetall() * coeff));
        if (resHolder.getPlastic() > 0) resources.add(plasticImg(resHolder.getPlastic() * coeff));
        if (resHolder.getMicroelectronics() > 0) resources.add(microelectronicsImg(resHolder.getMicroelectronics() * coeff));
        if (resHolder.getCloth() > 0) resources.add(clothImg(resHolder.getCloth() * coeff));
        if (resHolder.getStone() > 0) resources.add(stoneImg(resHolder.getStone() * coeff));
        if (resHolder.getChemical() > 0) resources.add(chemicalImg(resHolder.getChemical() * coeff));
        return org.springframework.util.StringUtils.arrayToDelimitedString(resources.toArray(), ", ");
    }

    public static String chemicalImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_CHEMICAL_NAME + "' src='/graphics/resources/resources_09.png'/> " + amount;
    }

    public static String stoneImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_STONE_NAME + "' src='/graphics/resources/resources_08.png'/> " + amount;
    }

    public static String clothImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_CLOTH_NAME + "' src='/graphics/resources/resources_07.png'/> " + amount;
    }

    public static String microelectronicsImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_MICROELECTRONICS_NAME + "' src='/graphics/resources/resources_06.png'/> " + amount;
    }

    public static String plasticImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_PLASTIC_NAME + "' src='/graphics/resources/resources_05.png'/> " + amount;
    }

    public static String metallImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_METALL_NAME + "' src='/graphics/resources/resources_04.png'/> " + amount;
    }

    public static String woodImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_WOOD_NAME + "' src='/graphics/resources/resources_03.png'/> " + amount;
    }

    public static String foodImg(int amount) {
        return "<img class='resImage' title='" + Const.RES_FOOD_NAME + "' src='/graphics/resources/resources_02.png'/> " + amount;
    }

    public static String getResString(ResourcesHolder resHolder, double coeff, int amount) {
        List<String> resources = new ArrayList<>();
        if (resHolder.getFood() > 0) resources.add(foodImg(((int) Math.ceil(resHolder.getFood() * coeff)) * amount));
        if (resHolder.getWood() > 0) resources.add(woodImg(((int) Math.ceil(resHolder.getWood() * coeff)) * amount));
        if (resHolder.getMetall() > 0) resources.add(metallImg(((int) Math.ceil(resHolder.getMetall() * coeff)) * amount));
        if (resHolder.getPlastic() > 0) resources.add(plasticImg(((int) Math.ceil(resHolder.getPlastic() * coeff)) * amount));
        if (resHolder.getMicroelectronics() > 0) resources.add(microelectronicsImg(((int) Math.ceil(resHolder.getMicroelectronics() * coeff)) * amount));
        if (resHolder.getCloth() > 0) resources.add(clothImg(((int) Math.ceil(resHolder.getCloth() * coeff)) * amount));
        if (resHolder.getStone() > 0) resources.add(stoneImg(((int) Math.ceil(resHolder.getStone() * coeff)) * amount));
        if (resHolder.getChemical() > 0) resources.add(chemicalImg(((int) Math.ceil(resHolder.getChemical() * coeff)) * amount));
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
