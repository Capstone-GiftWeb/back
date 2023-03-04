package com.capstone.giftWeb.gifts;

import com.capstone.giftWeb.domain.Item;
import com.capstone.giftWeb.repository.ItemRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GiftCrollingTest {

    private WebDriver driver;

    @Autowired
    private ItemRepository itemRepository;
    private static final String url = "https://gift.kakao.com/ranking/best/delivery"; //카카오톡 선물하기 '많이 선물한' 랭킹


    @BeforeAll
    static void 셋업() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver.exe");
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");            //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        driver = new ChromeDriver(options);
    }

    @Test
    public void 카카오크롤링() {
        driver.get(url);
        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(url);
            webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
            Actions actions = new Actions(driver);
            List<WebElement> elements = driver.findElements(By.cssSelector("app-view-best-ranking-product"));
            for (WebElement element : elements
            ) {
                actions.moveToElement(element);
                actions.perform();
                list.add(element.getAttribute("outerHTML"));
                String[] href = element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
                Integer productId = Integer.valueOf(href[href.length - 1]);
                System.out.println(productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(list.size(), 20);

    }

    @Test
    public void 카테고리크롤링() {
        List<String> list = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://gift.kakao.com/ranking/best/delivery/2");
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
        Actions actions = new Actions(driver);
        List<WebElement> elements = driver.findElements(By.cssSelector("app-view-best-ranking-product"));
        List<String> hrefs=new ArrayList<>();
        for (WebElement element : elements
        ) {
            actions.moveToElement(element);
            actions.perform();
            list.add(element.getAttribute("outerHTML"));
            String[] href=element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
            int productId= Integer.parseInt(href[href.length - 1]);
            Item item=new Item();
            item.setId((long) productId);
            item.setCategory(2);
            item.setHtml(element.getAttribute("outerHTML"));
            itemList.add(item);
        }
        itemRepository.saveAll(itemList);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
