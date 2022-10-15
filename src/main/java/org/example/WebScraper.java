package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebScraper {
        public static void main(String[] args) {

            //Set the path for the chromedriver version 106
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");

            WebDriver driver = new ChromeDriver();
            driver.navigate().to("https://www.comparetv.com.au/streaming-search-library/?ctvcp=1770");

            while(true){
                // If wrapper is not displayed, break
                if ((driver.findElement(By.className("load-more-wrapper")).getAttribute("style")).equals("display: none;")){
                    break;
                }
                else{
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.className("ctv-load-more")));
                    Actions actions = new Actions(driver);
                    actions.moveToElement(btn).click().build().perform();
                }
            };

            WebElement body = driver.findElement(By.className("provider-content-wrapper"));

            List<WebElement> movieList = body.findElements(By.tagName("div"));
            System.out.println("Total Netflix shows: "+ movieList.size());
            System.out.println("CSV output:");
            System.out.println("\"Title\",\"URL\"");


            for (WebElement movie :movieList ){
                String description = movie.findElement(By.tagName("span")).getText();
                String title = movie.getText().replace(description ,"").replace("\n","");
                String href = movie.findElement(By.tagName("a")).getAttribute("href");
                System.out.println("\""+title+"\",\""+href+"\"");
            }

        }

}