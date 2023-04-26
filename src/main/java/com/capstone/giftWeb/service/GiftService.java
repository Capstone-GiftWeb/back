package com.capstone.giftWeb.service;

import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.repository.GiftRepository;
import com.capstone.giftWeb.repository.GiftSearchRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class GiftService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    GiftRepository giftRepository;

    @Autowired
    GiftSearchRepository giftSearchRepository;

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

    public List<Gift> testGetGifts() {
        return giftRepository.findTop100ByOrderByIdDesc();
    }

    public List<Gift> searchGifts(String search) {
        return giftRepository.findAllByTitleContains(search);
    }

    public List<String> wordSearchShow(String searchWord) {
        return giftSearchRepository.wordSearchShow(searchWord);
    }

    private static final String url = "https://gift.kakao.com/ranking/best/delivery"; //카카오톡 선물하기 '많이 선물한' 랭킹

    private ChromeDriver setDriver() {

        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");            //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    @Scheduled(fixedDelay = 1000 * 60) //우선 1분마다 한번씩 실행
    private void getDataSchedule() {
        for (int i = 1; i < 11; i++) {
            getCategoryDataList(url + "/" + i, i);
        }
        getCategoryDataList(url + "/" + 20, 20);  //반려동물

    }

//    private List<String> getAllDataList() {
//        WebDriver driver = setDriver();
//        List<String> list = new ArrayList<>();
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        try {
//            driver.get(url);
//            Actions actions = new Actions(driver);
//            int i=0;
//           while (true) {
//               i++;
//               webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-view-best-ranking-product")));
//               WebElement element;
//               try {
//                   element = driver.findElement(By.cssSelector("ol > li:nth-child(" + i + ") > app-view-best-ranking-product"));
//               } catch (NoSuchElementException e) {
//                   continue;
//               }
//               actions.moveToElement(element);
//               actions.perform();
//               list.add(element.getAttribute("outerHTML"));
//               if (list.size() >= 100)
//                   break;
//           }
//
//        } catch (Exception e) {
//            log.warn(e.getMessage());
//        } finally {
//            driver.quit();
//        }
//        return list;
//    }

    private void getCategoryDataList(String categoryUrl, Integer categoryNum) {
        WebDriver driver = setDriver();

        List<Gift> giftList = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get(categoryUrl);
            Actions actions = new Actions(driver);
            int i = 0;
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
                String[] href = element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
                Integer productId = Integer.parseInt(href[href.length - 1]);
                String title = element.findElement(By.className("txt_prdname")).getText();
                String company = element.findElement(By.className("txt_brand")).getText();
                Integer price = Integer.parseInt(element.findElement(By.className("num_price")).getText().replaceAll(",", "").replace("원", ""));
                String image = element.findElement(By.className("img_thumb")).getAttribute("src");
                Gift gift = new Gift();
                gift.setId(Long.valueOf(productId));
                gift.setTitle(title);
                gift.setCompany(company);
                gift.setPrice(price);
                gift.setCategory(categoryNum);
                gift.setImage(image);
                gift.setHref(String.join("/", Arrays.copyOfRange(href, href.length - 2, href.length)));

                giftList.add(gift);
                if (giftList.size() >= 100)
                    break;
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            giftRepository.saveAll(giftList);
            driver.quit();
        }
    }

    private Gift getCategoryItem(WebElement element, Integer categoryNum) {
        String[] href = element.findElement(By.cssSelector("div > div.thumb_prd > gc-link > a")).getAttribute("href").split("/");
        String title = element.findElement(By.cssSelector("txt_prdname")).getText();
        int productId = Integer.parseInt(href[href.length - 1]);
        Gift gift = new Gift();
        gift.setId((long) productId);
        gift.setCategory(categoryNum);
        return gift;
    }

    private List<String> getReviewGifts(String displayTag, String priceRange) {
        WebDriver driver = setDriver();
        List<String> list = new ArrayList<>();
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String uri = "https://gift.kakao.com/ranking/review";
        driver.get(UriComponentsBuilder.fromUriString(uri).queryParam("displayTag", displayTag).queryParam("priceRange", priceRange).build().toUriString());
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-tag-ranking-review")));
        Actions actions = new Actions(driver);
        List<WebElement> elements = driver.findElements(By.cssSelector("app-tag-ranking-review"));
        for (int i = 0; i < 20; i++) {
            actions.moveToElement(elements.get(i));
            actions.perform();
            list.add(elements.get(i).getAttribute("outerHTML"));
        }
        return list;
    }
}

