package dyn.utils;

import dyn.model.Family;
import dyn.model.FamilyResources;
import dyn.service.Const;

import java.util.ArrayList;
import java.util.HashMap;
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
        if (resHolder.getFood() > 0) resources.add("Продукты: " + resHolder.getFood() * coeff);
        if (resHolder.getWood() > 0) resources.add("Дерево: " + resHolder.getWood() * coeff);
        if (resHolder.getMetall() > 0) resources.add("Металл: " + resHolder.getMetall() * coeff);
        if (resHolder.getPlastic() > 0) resources.add("Пластик, резина: " + resHolder.getPlastic() * coeff);
        if (resHolder.getMicroelectronics() > 0) resources.add("Микроэлектроника: " + resHolder.getMicroelectronics() * coeff);
        if (resHolder.getCloth() > 0) resources.add("Ткань, кожа, бумага: " + resHolder.getCloth() * coeff);
        if (resHolder.getStone() > 0) resources.add("Камень, стекло, керамика: " + resHolder.getStone() * coeff);
        if (resHolder.getChemical() > 0) resources.add("Химия, краски: " + resHolder.getChemical() * coeff);
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
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(Const.RES_FOOD_NAME).append(": ").append(current.getFood() - previous.getFood()).append("; ");
        sb.append(" ").append(Const.RES_WOOD_NAME).append(": ").append(current.getWood() - previous.getWood()).append("; ");
        sb.append(" ").append(Const.RES_METALL_NAME).append(": ").append(current.getMetall() - previous.getMetall()).append("; ");
        sb.append(" ").append(Const.RES_PLASTIC_NAME).append(": ").append(current.getPlastic() - previous.getPlastic()).append("; ");
        sb.append(" ").append(Const.RES_MICROELECTRONICS_NAME).append(": ").append(current.getMicroelectronics() - previous.getMicroelectronics()).append("; ");
        sb.append(" ").append(Const.RES_CLOTH_NAME).append(": ").append(current.getCloth() - previous.getCloth()).append("; ");
        sb.append(" ").append(Const.RES_STONE_NAME).append(": ").append(current.getStone() - previous.getStone()).append("; ");
        sb.append(" ").append(Const.RES_CHEMICAL_NAME).append(": ").append(current.getChemical() - previous.getChemical());

        return sb.toString();
    }

    public void saveInitValues(Family family) {
        FamilyResources familyResources = family.getFamilyResources();
        initValues = new HashMap<>();
        initValues.put("Money", family.getMoney());
        initValues.put("Food", familyResources.getFood());
        initValues.put("Wood", familyResources.getWood());
        initValues.put("Metall", familyResources.getMetall());
        initValues.put("Plastic", familyResources.getPlastic());
        initValues.put("Microelectronics", familyResources.getMicroelectronics());
        initValues.put("Cloth", familyResources.getCloth());
        initValues.put("Stone", familyResources.getStone());
        initValues.put("Chemical", familyResources.getChemical());
        initValues.put("CraftPoints", family.getCraftPoint());
    }
}
