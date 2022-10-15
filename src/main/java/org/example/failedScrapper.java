package org.example;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class failedScrapper {
    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage page;

        String url = "https://www.comparetv.com.au/streaming-search-library/?ctvcp=1770";

        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);


        //
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(10 * 1000);
        webClient.setJavaScriptTimeout(5 * 1000);
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // HtmlElement wrapper = page.getElementByName("load-more-wrapper");

        DomElement loadBtn = page.getFirstByXPath("//*[@id=\"content\"]/section[1]/div/div[2]/div/div/div[3]/div");

        System.out.println(loadBtn);
        while (loadBtn != null) {
            HtmlPage nextPage = loadBtn.click();
            //loadBtn = nextPage.getFirstByXPath("//*[@id=\"content\"]/section[1]/div/div[2]/div/div/div[3]/div");
            if (page == nextPage) {
                System.out.println("Page not changing");
                break;
            }


        }


        String firstPageHtml = page.asXml();


        webClient.waitForBackgroundJavaScript(10 * 1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());


        try {
            final Document document = Jsoup.parse(firstPageHtml);

            Elements body = document.select("div.provider-content-wrapper");

            for (Element e : body.select("div")) {

                e.select(divClassName + " span").remove();
                String title = e.select(divClassName).text();
                String link = e.select(divClassName + " a").attr("href");

                System.out.println(title + " " + link);

            }


            //System.out.println(document.outerHtml());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
