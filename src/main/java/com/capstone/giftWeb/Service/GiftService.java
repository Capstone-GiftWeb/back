package com.capstone.giftWeb.Service;

import com.capstone.giftWeb.domain.Item;
import com.capstone.giftWeb.repository.ItemRepository;
import org.asynchttpclient.uri.Uri;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class GiftService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    ItemRepository itemRepository;

    //카테고리별 맵
    private static HashMap<String, Integer> map;

    public GiftService() {
        map = new HashMap<>() {{
            put("패션", 1);
            put("화장품/향수", 2);
            put("출산/유아동", 3);
            put("식품", 4);
            put("리빙", 5);
            put("레저/스포츠", 6);
            put("가전/디지털", 7);
            put("건강", 8);
            put("명품 패션", 9);
            put("꽃배달/도서", 10);
        }};
    }

    private static final String url = "https://gift.kakao.com/ranking/best/delivery"; //카카오톡 선물하기 '많이 선물한' 랭킹

    public List<String> makeAllGifts() {


        List<String> list;
        list = getDataList();


        return list;
    }

    public List<String> makeCategoryGifts(String category) {

        List<String> list;
        int categoryNum = map.get(category);
        list = getCategoryDataList(url + "/" + categoryNum, categoryNum);

        return list;
    }

    public List<String> makeReviewGifts(String displayTag,String priceRange){
        List<String> list;
        list = getReviewGifts(displayTag, priceRange);

        return list;
    }

    private ChromeDriver setDriver() {

        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");            //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        return new ChromeDriver(options);
    }

    private List<String> getDataList() {
        WebDriver driver = setDriver();
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
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            driver.quit();
        }
        return list;
    }

    private List<String> getCategoryDataList(String categoryUrl, Integer categoryNum) {
        WebDriver driver = setDriver();

        List<String> list = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(categoryUrl);
            webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
            Actions actions = new Actions(driver);
            List<WebElement> elements = driver.findElements(By.cssSelector("app-view-best-ranking-product"));
            for (WebElement element : elements
            ) {
                actions.moveToElement(element);
                actions.perform();
                String html = element.getAttribute("outerHTML");
                list.add(html);
                Item item = getCategoryItem(element, categoryNum, html);
                itemList.add(item);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            itemRepository.saveAll(itemList);
            driver.quit();
        }
        return list;
    }

    private Item getCategoryItem(WebElement element, Integer categoryNum, String html) {
        String[] href = element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
        int productId = Integer.parseInt(href[href.length - 1]);
        Item item = new Item();
        item.setId((long) productId);
        item.setCategory(categoryNum);
        item.setHtml(html);
        return item;
    }

    private List<String> getReviewGifts(String displayTag,String priceRange) {
        WebDriver driver = setDriver();
        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String uri="https://gift.kakao.com/ranking/review";
        driver.get(UriComponentsBuilder.fromUriString(uri).queryParam("displayTag",displayTag).queryParam("priceRange",priceRange).build().toUriString());
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-tag-ranking-review")));
        Actions actions = new Actions(driver);
        List<WebElement> elements = driver.findElements(By.cssSelector("app-tag-ranking-review"));
        for(int i=0;i<20;i++){
            actions.moveToElement(elements.get(i));
            actions.perform();
            list.add(elements.get(i).getAttribute("outerHTML"));
        }
        return list;
    }
}

