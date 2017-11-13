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
        driver.get("http://192.168.1.38:8080/");

        assertDynastyPage();

        driver.findElement(By.id(GAME_LINK)).click();

        HashMap<String, String[]> players = new LinkedHashMap<>();
        players.put("Arbuzov", new String[]{"Арбузовы", "Арбузов", "Арбузова"});
        players.put("Baranov", new String[]{"Барановы", "Баранов", "Баранова"});
        players.put("Voronov", new String[]{"Вороновы", "Воронов", "Воронова"});
        players.put("Gurova", new String[]{"Гуровы", "Гуров", "Гурова"});
        players.put("Durova", new String[]{"Дуровы", "Дуров", "Дурова"});
        players.put("Ezhova", new String[]{"Ежовы", "Ежов", "Ежова"});
        players.put("Gukova", new String[]{"Жуковы", "Жуков", "Жукова"});
        players.put("Zubrov", new String[]{"Зубровы", "Зубров", "Зуброва"});
        players.put("Ignatov", new String[]{"Игнатовы", "Игнатов", "Игнатова"});
        players.put("Kurova", new String[]{"Куровы", "Куров", "Курова"});
        players.put("Leontiev", new String[]{"Леонтьевы", "Леонтьев", "Леонтьева"});
        players.put("Muhina", new String[]{"Мухины", "Мухин", "Мухина"});
        players.put("Nosova", new String[]{"Носовы", "Носов", "Носова"});
        players.put("Osipov", new String[]{"Осиповы", "Осипов", "Осипова"});
        players.put("Prohorov", new String[]{"Прохоровы", "Прохоров", "Прохорова"});
        players.put("Rogova", new String[]{"Роговы", "Рогов", "Рогова"});
        players.put("Somova", new String[]{"Сомовы", "Сомов", "Сомова"});
        players.put("Terehov", new String[]{"Тереховы", "Терехов", "Терехова"});
        players.put("Uhova", new String[]{"Уховы", "Ухов", "Ухова"});
        players.put("Fokin", new String[]{"Фокины", "Фокин", "Фокина"});
        players.put("Hudov", new String[]{"Худовы", "Худов", "Худова"});
        players.put("Tsarev", new String[]{"Царевы", "Царев", "Царева"});
        players.put("Chudov", new String[]{"Чудовы", "Чудов", "Чудова"});
        players.put("Shapkin", new String[]{"Шапкины", "Шапкин", "Шапкина"});
        players.put("Schukin", new String[]{"Щуковы", "Щуков", "Щукова"});
        players.put("Ezhova", new String[]{"Эховы", "Эхов", "Эхова"});
        players.put("Yuriev", new String[]{"Юрьевы", "Юрьев", "Юрьева"});
        players.put("Yamov", new String[]{"Ямовы", "Ямов", "Ямова"});

        /*for (String player : players.keySet()) {
            logIn(player);
            addNewFamily(player, players.get(player)[0], players.get(player)[1], players.get(player)[2]);
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

        Assert.assertTrue(player + ": not main page", isMainPage(player));

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

            if (isMainPage(player) == false) {
                System.out.println("ERROR");
                System.out.println("bbb");
            }
            Assert.assertTrue(player + ": not main page", isMainPage(player));

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

        Assert.assertTrue(player + ": not main page", isMainPage(player));
    }

    public void postFiancees(String player) {
        System.out.println("*** GameControllerTest.postFiancees: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage(player));


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

        Assert.assertTrue(player + ": not main page", isMainPage(player));

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

    public boolean isMainPage(String player) {
//        return pageBody().contains("(" + player + ")");
        return pageBody().contains("Лог уровня");
    }

    public boolean isAddNewFamilyPage() {
        return pageBody().contains("Создание семьи");
    }

    private void assertDynastyPage() {
        Assert.assertTrue("Not Dynasty page", pageBody().contains("В игре вам предстоит создать династию!"));
    }

    private void assertLoginPage() {
        Assert.assertTrue("Not login page", pageBody().contains("Авторизация"));
    }

}
