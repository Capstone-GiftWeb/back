package com.capstone.giftWeb.gifts;

import com.capstone.giftWeb.domain.Item;
import com.capstone.giftWeb.repository.ItemRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
        //options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");            //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void 카카오크롤링() {
        driver.get(url);
        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(url);
            Actions actions = new Actions(driver);
            int i = -1;
            while (true) {
                i++;
                webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
                WebElement element;
                try {
                    element = driver.findElement(By.cssSelector("ol > li:nth-child(" + i + ") > app-view-best-ranking-product"));
                } catch (NoSuchElementException e) {
                    continue;
                }
                actions.moveToElement(element);
                actions.perform();
                list.add(element.getAttribute("outerHTML"));
                if (list.size() >= 100)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains("mud")) {
                Assertions.fail("이미지 갖고오기 실패");
            }
        }
        Assert.assertEquals(list.size(), 100);

    }

    @Test
    public void 카테고리크롤링() {
        List<String> list = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            Actions actions = new Actions(driver);
            List<String> hrefs = new ArrayList<>();

            driver.get("https://gift.kakao.com/ranking/best/delivery/2");
            int i = -1;
            while (true) {
                i++;
                webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
                WebElement element;
                try{
                    element = driver.findElement(By.cssSelector("ol > li:nth-child(" + i + ") > app-view-best-ranking-product"));
                }catch (NoSuchElementException e){
                    continue;
                }
                actions.moveToElement(element);
                actions.perform();
                list.add(element.getAttribute("outerHTML"));
                String[] href = element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
                int productId = Integer.parseInt(href[href.length - 1]);
                Item item = new Item();
                item.setId((long) productId);
                item.setCategory(2);
                item.setHtml(element.getAttribute("outerHTML"));
                itemList.add(item);
                if(list.size()>=100)
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            itemRepository.saveAll(itemList);


            Assert.assertEquals(itemList.size(), 100);
        for (String s : list) {
            if (s.contains("mud")) {
                Assertions.fail("이미지 갖고오기 실패");
            }
        }

        }


    @Test
    public void 받고만족한_크롤링() {
        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://gift.kakao.com/ranking/review?displayTag=%EC%9D%91%EC%9B%90&priceRange=R_3_5");
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-tag-ranking-review")));
        Actions actions = new Actions(driver);
        List<WebElement> elements = driver.findElements(By.cssSelector("app-tag-ranking-review"));
        for (int i = 0; i < 20; i++) {
            actions.moveToElement(elements.get(i));
            actions.perform();
            list.add(elements.get(i).getAttribute("outerHTML"));
        }
        assertThat(list.size()).isEqualTo(20);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
