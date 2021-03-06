package dyn.service;

import org.springframework.stereotype.Service;

/**
 * Created by OM on 03.05.2017.
 */
@Service
public class Const {
    public static final Long CRAFTBRANCH_FURNITURE = 1L;
    public static final Long CRAFTBRANCH_DEVICES = 2L;
    public static final Long CRAFTBRANCH_DECOR = 3L;
    public static final Long CRAFTBRANCH_CLOTH = 4L;
    public static final Long CRAFTBRANCH_MEAL = 5L;
    public static final Long CRAFTBRANCH_EQUIPMENT = 6L;
    public static final Long CRAFTBRANCH_SERVICE_AND_BUFFS = 10L;

    public static final Long CRAFTBRANCH_MAX = 6L;


    public static final Long THING_SKILL_IMPROVEMENT = 118L;
    public static final Long THING_RESOURCES_SERTIFICATE = 117L;
    public static final Long THING_MARRIED_BUFF = 116L;
    public static final Long THING_PARENTS_BUFF = 97L;
    public static final Long THING_FAMILY_BUFF = 119L;
    public static final Long THING_CHILDREN_BUFF = 120L;

    public static final Long PROJECT_RES_FOOD = 101L;
    public static final Long PROJECT_RES_WOOD = 100L;
    public static final Long PROJECT_RES_METALL = 118L;
    public static final Long PROJECT_RES_PLASTIC = 119L;
    public static final Long PROJECT_RES_MICROELECTRONICS = 120L;
    public static final Long PROJECT_RES_CLOTH = 121L;
    public static final Long PROJECT_RES_STONE = 122L;
    public static final Long PROJECT_RES_CHEMICAL = 123L;
    public static final Long PROJECT_SKILL_INTELLIGENCE = 102L;
    public static final Long PROJECT_SKILL_CHARISMA = 115L;
    public static final Long PROJECT_SKILL_STRENGTH = 116L;
    public static final Long PROJECT_SKILL_CREATIVITY = 117L;
    public static final Long PROJECT_ADOPTED_CHILD = 98L;
    public static final Long PROJECT_ONE_MORE_CHILD = 108L;
    public static final Long PROJECT_MORE_SONS = 99L;
    public static final Long PROJECT_MORE_DAUGHTERS = 107L;
    public static final Long PROJECT_FERTILITY = 103L;
    public static final Long PROJECT_FATHER_DOMINANT = 104L;
    public static final Long PROJECT_MOTHER_DOMINANT = 105L;
    public static final Long PROJECT_GEN_MOD = 106L;
    public static final Long PROJECT_PAIR_ONE_MORE = 109L;
    public static final Long PROJECT_BRIDE_ONE_MORE = 110L;
    public static final Long PROJECT_CLONE = 111L;
    public static final Long PROJECT_BODY_PART_CHANGE = 112L;
    public static final Long PROJECT_VOCATION_CHANGE = 113L;
    public static final Long PROJECT_SALARY_INC = 114L;
    public static final Long PROJECT_ITEM_QUALITY = 315L;


    public static final int EDUCATION_IMPROVE_COST = 350;

    public static final Long PROJECT_SKOROVAR = 4L;

    public static final Long BUFF_FERTILITY = 1L;
    public static final Long BUFF_SEVEN_CHILDREN = 2L;
    public static final Long BUFF_DOMINANT_FATHER = 3L;
    public static final Long BUFF_DOMINANT_MOTHER = 4L;
    public static final Long BUFF_MANY_SONS = 5L;
    public static final Long BUFF_MANY_DAUGHTERS = 6L;
    public static final Long BUFF_GENETIC_MOD = 7L;
    public static final Long BUFF_ONE_MORE_CHILD = 8L;
    public static final Long BUFF_SALARY_INC = 9L;

    public static final String RES_WOOD_NAME = "Дерево";
    public static final String RES_FOOD_NAME = "Продукты";
    public static final String RES_METALL_NAME = "Металл";
    public static final String RES_PLASTIC_NAME = "Пластик, резина";
    public static final String RES_MICROELECTRONICS_NAME = "Микроэлектроника";
    public static final String RES_CLOTH_NAME = "Ткань, кожа, бумага";
    public static final String RES_STONE_NAME = "Камень, стекло, керамика";
    public static final String RES_CHEMICAL_NAME = "Химия, краски";

    public static final int COST_NEW_PROJECT = 10000;

    public static final double DESTROY_ITEM_COEFF = 0.5;

    public static final Long ROOM_KITCHEN = 1L;    //кухня
    public static final Long ROOM_BEDROOM = 2L;    //спальня
    public static final Long ROOM_BATHROOM = 3L;    //ванная
    public static final Long ROOM_LIVINGROOM = 4L;    //гостиная
    public static final Long ROOM_CHILDROOM = 5L;    //детская
    public static final Long ROOM_DININGROOM = 6L;    //столовая
    public static final Long ROOM_WARDROBE = 7L;    //гардероб
    public static final Long ROOM_OFFICE = 8L;    //кабинет
    public static final Long ROOM_GARAGE = 9L;    //гараж
    public static final Long ROOM_LIBRARY = 10L;    //библиотека
    public static final Long ROOM_POOL = 11L;    //спортзал и бассейн
    public static final Long ROOM_GARDEN = 12L;    //сад
    public static final Long ROOM_GALERY = 13L;    //галерея
    public static final Long ROOM_TABLE = 14L;    //стол

    public static final int CRAFT_POINTS_START = 3;
    public static final int CRAFT_POINTS_FOR_LEVEL = 1;
    public static final int CRAFT_POINTS_FOR_NEW_HOUSE = 10;

    public static final int ACHIEVEMENT_CRAFT_POINTS = 2;
    public static final int ACHIEVEMENT_MONEY = 5000;

    public static final int ITEM_QUALITY_1 = 5;
    public static final int ITEM_QUALITY_2 = 25;
    public static final int ITEM_QUALITY_3 = 50;

    public static final float[][] FERTILITY_USUAL = new float[][]{
            {0.05f, 0.45f, 0.85f, 1f, 0f, 0f, 0},
            {0f, 0.2f, 0.7f, 0.95f, 1f, 0f, 0},
            {0f, 0.05f, 0.3f, 0.8f, 0.95f, 1f, 0},
            {0f, 0f, 0.1f, 0.4f, 0.9f, 1f, 0},
            {0f, 0f, 0f, 0.1f, 0.4f, 0.9f, 1},
            {0f, 0f, 0f, 0f, 0.2f, 0.5f, 1}};

    public static final float[][] FERTILITY_BUFFED = new float[][]{
            {0f, 0f, 0.05f, 0.55f, 0.85f, 0.95f, 1f},
            {0f, 0f, 0f, 0.15f, 0.65f, 0.9f, 1f},
            {0f, 0f, 0f, 0.05f, 0.3f, 0.8f, 1f},
            {0f, 0f, 0f, 0f, 0.15f, 0.5f, 1f},
            {0f, 0f, 0f, 0f, 0f, 0.35f, 1f},
            {0f, 0f, 0f, 0f, 0f, 0.05f, 1f}};


    public static final int SKILL_COEFFICIENT = 1;
    public static final int RACE_RESOURCE_COEFFICIENT = 5;
    public static final int AUTOGEN_PRODUCTION_COST_COEFFICIENT = 5;


    public static final double PERCENTAGE_GEN_MOD = 0.05;
    public static final double PERCENTAGE_GEN_MOD_BUFFED = 0.45;
    public static final int FIANCEE_COST = 50;

    public static final int SKILL_IMPROVEMENT_COEFFICIENT = 3;
    public static final int MAX_CHILDREN = 10;
    public static final int MAX_FAMILIES = 5;

    // ========== GIFT ============================================
    public static final int GIFT_LEVEL = 5;
    public static final Long[] GIFT_PROJECTS = {PROJECT_FERTILITY, PROJECT_GEN_MOD, PROJECT_PAIR_ONE_MORE, PROJECT_BRIDE_ONE_MORE};

    // ========== ADVENTURES ======================================
    public static final int ADVENTURE_AMOUNT_PER_TURN = 5;
    public static final int ADVENTURE_COST = 350;
    public static final int ADVENTURE_AUTHOR_AWARD = 1000;
    public static final int ADVENTURE_CREATION_COST = 10000;
    // ============================================================
    public static final int ITEM_MAX_QUALITY = 5;
    public static final int ADVENTURE_AMOUNT_PER_AWARD = 7;
}
