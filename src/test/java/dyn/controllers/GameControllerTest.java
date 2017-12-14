package dyn.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class GameControllerTest {


    public static final String GAME_LINK = "navGameLink";
    public static final String LOGOUT_LINK = "logouta";

    private WebDriver driver;

    @Before
    public void startDriver() {
        File file = new File("f:/SpringProjects/My/Dynasty/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        java.util.logging.Logger.getLogger("org.apache.http.*").setLevel(java.util.logging.Level.WARNING);
    }

    @After
    public void stopDriver() {
        driver.close();
    }


    @Test
    public void startFromNothing() {
        driver.get("http://192.168.1.51:8080/");

        assertDynastyPage();

        driver.findElement(By.id(GAME_LINK)).click();

        HashMap<String, String[]> players = new LinkedHashMap<>();
//        players.put("Arbuzov@mail.ru", new String[]{"Arbuzov", "Арбузовы", "Арбузов", "Арбузова"});
        players.put("newchar2@mail.ru", new String[]{"Baranov", "Барановы", "Баранов", "Баранова"});
//        players.put("NewChar3@mail.ru", new String[]{"Voronov", "Вороновы", "Воронов", "Воронова"});
        players.put("NewChar4@mail.ru", new String[]{"Gurova", "Гуровы", "Гуров", "Гурова"});
//        players.put("NewChar5@mail.ru", new String[]{"Durova", "Дуровы", "Дуров", "Дурова"});
//        players.put("ForTest2@mail.ru", new String[]{"Gukova", "Жуковы", "Жуков", "Жукова"});
//        players.put("ForTest3@mail.ru", new String[]{"Zubrov", "Зубровы", "Зубров", "Зуброва"});
//        players.put("ForTest4@mail.ru", new String[]{"Ignatov", "Игнатовы", "Игнатов", "Игнатова"});
//        players.put("ForTest5@mail.ru", new String[]{"Kurova", "Куровы", "Куров", "Курова"});
//        players.put("ForTest6@mail.ru", new String[]{"Leontiev", "Леонтьевы", "Леонтьев", "Леонтьева"});
//        players.put("ForTest7@mail.ru", new String[]{"Muhina", "Мухины", "Мухин", "Мухина"});
//        players.put("ForTest8@mail.ru", new String[]{"Nosova", "Носовы", "Носов", "Носова"});
//        players.put("ForTest9@mail.ru", new String[]{"Osipov", "Осиповы", "Осипов", "Осипова"});
//        players.put("ForTest10@mail.ru", new String[]{"Prohorov", "Прохоровы", "Прохоров", "Прохорова"});
//        players.put("ForTest11@mail.ru", new String[]{"Rogova", "Роговы", "Рогов", "Рогова"});
//        players.put("ForTest12@mail.ru", new String[]{"Somova", "Сомовы", "Сомов", "Сомова"});
        players.put("fortest13@mail.ru", new String[]{"Terehov", "Тереховы", "Терехов", "Терехова"});
//        players.put("fortest14@mail.ru", new String[]{"Uhova", "Уховы", "Ухов", "Ухова"});
//        players.put("fortest15@mail.ru", new String[]{"Fokin", "Фокины", "Фокин", "Фокина"});
        players.put("fortest16@mail.ru", new String[]{"Hudov", "Худовы", "Худов", "Худова"});
//        players.put("fortest17@mail.ru", new String[]{"Tsarev", "Царевы", "Царев", "Царева"});
//        players.put("fortest18@mail.ru", new String[]{"Chudov", "Чудовы", "Чудов", "Чудова"});
//        players.put("fortest19@mail.ru", new String[]{"Shapkin", "Шапкины", "Шапкин", "Шапкина"});
//        players.put("fortest20@mail.ru", new String[]{"Schukin", "Щуковы", "Щуков", "Щукова"});
//        players.put("fortest21@mail.ru", new String[]{"Ezhova", "Эховы", "Эхов", "Эхова"});
//        players.put("fortest22@mail.ru", new String[]{"Yuriev", "Юрьевы", "Юрьев", "Юрьева"});
//        players.put("fortest23@mail.ru", new String[]{"Yamov", "Ямовы", "Ямов", "Ямова"});

        /*for (String player : players.keySet()) {
            logIn(player);
            addNewFamily(player, players.get(player)[1], players.get(player)[2], players.get(player)[3]);
            makeTurn(player);
            postFiancees(player);
            logOut(player);
        }*/

        int levels = 10;
        for (int level = 0; level < levels; level++) {
            for (String player : players.keySet()) {
                System.out.println(" -= player =- " + player);
                logIn(player);
                postFiancees(player);
                boolean bridesWasChosen = chooseFiancees(player);

                if (bridesWasChosen == true) {
                    makeTurn(player);
                    postFiancees(player);
                }
                logOut(player);
            }
        }

    }

    private boolean chooseFiancees(String player) {
        System.out.println("*** GameControllerTest.chooseFianceesAndTurn: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage());

        List<WebElement> chooseFianceeButtonList = driver.findElements(By.name("chooseFianceeButton"));
        System.out.println("chooseFianceeButtonList.size() = " + chooseFianceeButtonList.size());

        while (chooseFianceeButtonList.size() > 0) {
            int randIndex = (int) (0 + Math.random() * (chooseFianceeButtonList.size() - 1));
            WebElement chooseFianceeButton = chooseFianceeButtonList.get(randIndex);

            scrollTo(chooseFianceeButton);
            chooseFianceeButton.click();

            Assert.assertTrue("'Анкеты невест' not found!", pageBody().contains("Анкеты невест"));

            List<WebElement> radioFianceeList = driver.findElements(By.id("fianceeCurrentLevel"));
            if (radioFianceeList.size() == 0) {
                radioFianceeList = driver.findElements(By.id("fianceeLowerLevel"));
                if (radioFianceeList.size() == 0) {
                    return false;
                }
            }
            randIndex = (int) (0 + Math.random() * (radioFianceeList.size() - 1));

            WebElement radioFiancee = radioFianceeList.get(randIndex);
            scrollTo(radioFiancee);
            radioFiancee.click();

            WebElement makeFianceeButton = driver.findElement(By.id("makeFianceeButton"));
            scrollTo(makeFianceeButton);
            makeFianceeButton.click();

            if (isMainPage() == false) {
                System.out.println("ERROR");
                System.out.println("bbb");
            }
            Assert.assertTrue(player + ": not main page", isMainPage());

            chooseFianceeButtonList = driver.findElements(By.name("chooseFianceeButton"));
        }
        return true;
    }

    private void scrollTo(WebElement webElement) {

        /*Actions actions = new Actions(driver);
        actions.moveToElement(webElement);
        actions.perform();

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, 250);");*/

        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, webElement);
    }

    public void addNewFamily(String player, String familyName, String maleLastname, String femaleLastname) {
        System.out.println("*** GameControllerTest.addNewFamily: " + player + " ***");

        Assert.assertTrue("'Создание семьи", isAddNewFamilyPage());

        driver.findElement(By.name("family.familyName")).sendKeys(familyName);
        driver.findElement(By.name("family.maleLastname")).sendKeys(maleLastname);
        driver.findElement(By.name("family.femaleLastname")).sendKeys(femaleLastname);

        WebElement addNewFamilyButton = driver.findElement(By.name("addNewFamilyButton"));
        scrollTo(addNewFamilyButton);
        addNewFamilyButton.click();

        Assert.assertTrue(player + ": not main page", isMainPage());
    }

    public void postFiancees(String player) {
        System.out.println("*** GameControllerTest.postFiancees: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage());


        List<WebElement> postFianceeButtonList = driver.findElements(By.name("postFianceeButton"));
        System.out.println("postFianceeButtonList.size() = " + postFianceeButtonList.size());

        while (postFianceeButtonList.size() > 0) {
            int randIndex = (int) (0 + Math.random() * (postFianceeButtonList.size() - 1));
            WebElement postFianceeButton = postFianceeButtonList.get(randIndex);
            scrollTo(postFianceeButton);
            postFianceeButton.click();
            Assert.assertTrue("'Анкета персонажа добавлена в базу невест' not found!", pageBody().contains("Анкета персонажа добавлена в базу невест"));

            postFianceeButtonList = driver.findElements(By.name("postFianceeButton"));
        }

    }

    public void makeTurn(String player) {
        System.out.println("*** GameControllerTest.makeTurn: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage());

        WebElement turn = driver.findElement(By.name("turn"));
        scrollTo(turn);
        turn.click();
    }

    public void logIn(String player) {
        assertLoginPage();

        driver.findElement(By.name("username")).sendKeys(player);
        driver.findElement(By.name("password")).sendKeys("201285");
        driver.findElement(By.name("buttonSubmit")).click();

    }

    public void logOut(String player) {
        WebElement logout = driver.findElement(By.name(LOGOUT_LINK));
        logout.click();

        driver.findElement(By.id(GAME_LINK)).click();
        assertLoginPage();
    }


    public String pageBody() {
        return driver.findElement(By.tagName("body")).getText();
    }

    public boolean isMainPage() {
        return pageBody().contains("Лог уровня");
    }

    public boolean isAddNewFamilyPage() {
        return pageBody().contains("Создание семьи");
    }

    private void assertDynastyPage() {
        Assert.assertTrue("Not Dynasty page", pageBody().contains("Дом Для Династии"));
    }

    private void assertLoginPage() {
        Assert.assertTrue("Not login page", pageBody().contains("Авторизация"));
    }

}
