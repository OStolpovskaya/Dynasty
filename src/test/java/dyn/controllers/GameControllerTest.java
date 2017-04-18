package dyn.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class GameControllerTest {


    public static final String GAME_LINK = "navGameLink";
    public static final String LOGOUT_LINK = "logouta";

    private WebDriver driver;

    @Before
    public void startDriver() {
        File file = new File("f:/SpringProjects/My/Dynasty/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        driver = new ChromeDriver();

        java.util.logging.Logger.getLogger("org.apache.http.*").setLevel(java.util.logging.Level.WARNING);
    }

    @After
    public void stopDriver() {
        driver.close();
    }

    @Test
    public void startFromNothing() {
        // todo: каждый метод начинать с логина. сначала новая семья и ход, потом все постят невест, потом все выбирают невест.
        driver.get("http://localhost:8080/");
        Assert.assertTrue("Not Dynasty page", isDynastyPage());

        driver.findElement(By.id(GAME_LINK)).click();
        Assert.assertTrue("Not login page", isLoginPage());

        HashMap<String, String[]> players = new HashMap<>();
        players.put("YellowSubmarine", new String[]{"Подлодковы", "Подлодков", "Подлодкова"});
        players.put("Hovard", new String[]{"Верещагины", "Верещагин", "Верещагина"});
        players.put("OlgaTheFirst", new String[]{"Котовы", "Котов", "Котова"});

        for (String player : players.keySet()) {
            addNewFamily(player, players.get(player)[0], players.get(player)[1], players.get(player)[2]);
            makeTurn(player);
            postFiancees(player);
            logOut(player);
        }

        int levels = 5;
        for (int level = 0; level < levels; level++) {
            for (String player : players.keySet()) {
                logIn(player);
                boolean success = chooseFiancees(player);
                if (success) {
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
        int chooseFianceeButtonAmount = chooseFianceeButtonList.size();
        int chosenSuccess = 0;
        for (int i = 0; i < chooseFianceeButtonAmount; i++) {
            WebElement chooseFianceeButton = driver.findElement(By.name("chooseFianceeButton"));
            chooseFianceeButton.click();
            Assert.assertTrue("'Выберите невесту' not found!", pageBody().contains("Выберите невесту"));
            WebElement radioFiancee = driver.findElement(By.name("fiancee"));
            if (radioFiancee.isEnabled()) {
                radioFiancee.click();
                WebElement makeFianceeButton = driver.findElement(By.id("makeFianceeButton"));
                makeFianceeButton.click();

                Assert.assertTrue(player + ": not main page", isMainPage(player));
                chosenSuccess += 1;
            } else {
                System.out.println(player + " has no enough money to buy fiancee");
                break;
            }
            System.out.println("i=" + i);
        }
        System.out.println("Choosing fiancees is done.");
        if (chosenSuccess == chooseFianceeButtonAmount) {
            return true;
        } else {
            System.out.println(player + "Error in chosing fiancees. NO TURN");
            return false;
        }
    }

    public void addNewFamily(String player, String familyName, String maleLastname, String femaleLastname) {
        System.out.println("*** GameControllerTest.addNewFamily: " + player + " ***");
        Assert.assertTrue(player + ": not login page", isLoginPage());

        driver.findElement(By.name("username")).sendKeys(player);
        driver.findElement(By.name("password")).sendKeys("201285");
        driver.findElement(By.name("buttonSubmit")).click();

        Assert.assertTrue("'Создание семьи", isAddNewFamilyPage());

        driver.findElement(By.name("family.familyName")).sendKeys(familyName);
        driver.findElement(By.name("family.maleLastname")).sendKeys(maleLastname);
        driver.findElement(By.name("family.femaleLastname")).sendKeys(femaleLastname);
        driver.findElement(By.name("addNewFamilyButton")).click();

        Assert.assertTrue(player + ": not main page", isMainPage(player));
    }

    public void postFiancees(String player) {
        System.out.println("*** GameControllerTest.postFiancees: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage(player));

        List<WebElement> postFianceeButtonList = driver.findElements(By.name("postFianceeButton"));
        System.out.println("postFianceeButtonList.size() = " + postFianceeButtonList.size());
        int postFianceeButtonAmount = postFianceeButtonList.size();
        for (int i = 0; i < postFianceeButtonAmount; i++) {
            WebElement postFianceeButton = driver.findElement(By.name("postFianceeButton"));
            postFianceeButton.click();
            Assert.assertTrue("'Персонаж добавлен в базу невест!' not found!", pageBody().contains("Персонаж добавлен в базу невест!"));
        }
    }

    public void makeTurn(String player) {
        System.out.println("*** GameControllerTest.makeTurn: " + player + " ***");

        Assert.assertTrue(player + ": not main page", isMainPage(player));

        WebElement turn = driver.findElement(By.name("turn"));
        turn.click();
    }

    public void logIn(String player) {
        Assert.assertTrue(player + ": not login page", isLoginPage());

        driver.findElement(By.name("username")).sendKeys(player);
        driver.findElement(By.name("password")).sendKeys("201285");
        driver.findElement(By.name("buttonSubmit")).click();

        Assert.assertTrue(player + ": not main page", isMainPage(player));
    }

    public void logOut(String player) {
        WebElement logout = driver.findElement(By.name(LOGOUT_LINK));
        logout.click();

        driver.findElement(By.id(GAME_LINK)).click();
        Assert.assertTrue(player + ": not login page", isLoginPage());
    }


    public String pageBody() {
        return driver.findElement(By.tagName("body")).getText();
    }

    public boolean isMainPage(String player) {
        return pageBody().contains("Игрок: " + player);
    }

    public boolean isLoginPage() {
        return pageBody().contains("Create an account");
    }

    public boolean isDynastyPage() {
        return pageBody().contains("ru_RU");
    }

    public boolean isAddNewFamilyPage() {
        return pageBody().contains("Создание семьи");
    }

}
