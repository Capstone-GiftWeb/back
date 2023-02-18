package com.capstone.giftWeb.Service;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HomeService {

    private WebDriver driver;

    private static final String url = "https://gift.kakao.com/ranking/best/delivery"; //카카오톡 선물하기 '많이 선물한' 랭킹

    public List<String> makeGifts() {

        System.setProperty("webdriver.chrome.driver", "./chromedriver/chromedriver.exe");
        //파이어폭스 드라이버 셋팅 (드라이버 설치한 경로 입력)

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        options.addArguments("headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");            //gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
        driver = new ChromeDriver(options);

        List<String> list;
        try {
            list = getDataList();
        } finally {
            driver.close();
        }

        return list;
    }

    private List<String> getDataList() {
        List<String> list = new ArrayList<>();
        try {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            Actions actions=new Actions(driver);
            List<WebElement> elements = driver.findElements(By.cssSelector("app-view-best-ranking-product"));
            for (WebElement element : elements
            ) {
                actions.moveToElement(element);
                actions.perform();
                list.add(element.getAttribute("outerHTML"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
