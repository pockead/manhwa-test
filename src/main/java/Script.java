import com.codeborne.selenide.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class Script {
    static String siteUrl = "https://mangabuff.ru/login";
    static String manhwaurl = "https://mangabuff.ru/manga/povtornaya-zhizn";
    static Duration wait2Minutes = Duration.ofMinutes(3);
    static boolean fl = true;

    public static Boolean isVisibleInViewport(WebElement element) {

        WebDriver driver = getWebDriver();

        return (Boolean)((JavascriptExecutor)driver).executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;                            "
                , element);
    }

    public static WebElementCondition viewInViewPort() {
        return new WebElementCondition("viewInViewPort") {
            @Nonnull
            @Override
            public CheckResult check(Driver driver, WebElement webElement) {
                return new CheckResult(isVisibleInViewport(webElement), "long wait");
            }
        };
    }

    public static void main(String[] args) {
        Configuration.timeout = 4;
        open(siteUrl);
        getWebDriver().manage().window().maximize();
        open(manhwaurl);
        $("a[data-slug]").click();
//        open("https://mangabuff.ru/manga/rascvet-molodosti/1/6");
        $(("button[class='reader-menu__item reader-menu__item--settings']")).click();
        $(("button[class='button reader-toggle-autocroll-btn mb-4']")).click();
        $x("/html").click();
        int last = 0;
        do {
            $(("button[class='reader-menu-scroll__start-btn ml-2 mr-2']")).click();
            for (int i = 0; i < 7; i++) {
                $(By.id("fasterBtn")).click();
            }

            last = $$("a[class='button button--primary'").size() - 1;
            SelenideElement element = $$("a[class='button button--primary'").get(last);
             $$("a[class='button button--primary'").get(last)
                    .shouldBe(viewInViewPort(), wait2Minutes)
                    .click();
            if (last>0)
                fl = false;
        } while (last>0||fl);
        System.out.println("МАНХВА ДОЛИСТАНА! ПОЗДРАВЛЯЕМ!");
    }

}
