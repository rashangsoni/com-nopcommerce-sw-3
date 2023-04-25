package computer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import utilities.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSuite extends Utility {
    String baseUrl = "https://demo.nopcommerce.com/";

    @Before
    public void setUp() {
        openBrowser(baseUrl);
    }

    @Test
    public void verifyProductArrangeInAlphabeticalOrder() throws InterruptedException {
        // Find the computer and desktop element and store to WebElement
        WebElement computer = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[2]/ul[1]/li[1]/a[1]"));
        WebElement desktop = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[2]/ul[1]/li[1]/ul[1]/li[1]/a[1]"));
        // Initialize the action class object
        Actions action = new Actions(driver);
        //Mouse hover to computer and click on desktop
        action.moveToElement(computer).moveToElement(desktop).click().build().perform();

        //Select Z to A sorting option from dropdown list & verify
        List<WebElement> beforeFilterNameZtoAList = driver.findElements(By.className("item-grid"));
        List<String> beforeFileNameZtoAList = new ArrayList<>();
        for (WebElement nameZtoA : beforeFilterNameZtoAList) {
            beforeFileNameZtoAList.add(nameZtoA.getText());
        }
        Select select = new Select(driver.findElement(By.id("products-orderby")));
        select.selectByVisibleText("Name: Z to A");
        List<WebElement> afterFilterNameZtoAList = driver.findElements(By.className("item-grid"));
        List<String> afterFileNameZtoAList = new ArrayList<>();
        for (WebElement nameZtoA : afterFilterNameZtoAList) {
            afterFileNameZtoAList.add(nameZtoA.getText());
        }
        Collections.sort(beforeFileNameZtoAList);
        Assert.assertEquals(beforeFilterNameZtoAList, afterFilterNameZtoAList);
        Thread.sleep(3000);
    }

    @Test
    public void verifyProductAddedToShoppingCartSuccessFully() throws InterruptedException {
        // Find the computer and desktop element and store to WebElement
        WebElement computer = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[2]/ul[1]/li[1]/a[1]"));
        WebElement desktop = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[2]/ul[1]/li[1]/ul[1]/li[1]/a[1]"));
        // Initialize the action class object
        Actions action = new Actions(driver);
        //Mousehover to computer and click on desktop
        action.moveToElement(computer).moveToElement(desktop).click().build().perform();

        //Select A to Z soeting option from dropdown list
        WebElement dropdown = driver.findElement(By.name("products-orderby"));
        Select select = new Select(dropdown);
        select.selectByVisibleText("Name: A to Z");


        // Add to cart
        Thread.sleep(2000);
        clickOnElement(By.xpath("//div[@class='product-item']/div[2]/div[3]/div[2]/button[1]"));

        // Select Processor type
        WebElement processor = driver.findElement(By.name("product_attribute_1"));
        Select select1 = new Select(processor);
        select1.selectByVisibleText("2.2 GHz Intel Pentium Dual-Core E2200");

        // Select Ram size
        WebElement ram = driver.findElement(By.xpath("//select[@id='product_attribute_2']"));
        Select select2 = new Select(ram);
        select2.selectByVisibleText("8GB [+$60.00]");

        //Tick radio button
        clickOnElement(By.xpath("//input[@id='product_attribute_3_7']"));

        //Tick check 2 boxes
        clickOnElement(By.xpath("//input[@id='product_attribute_4_9']"));
        //clickOnElement(By.xpath("//input[@id='product_attribute_5_10']"));
        clickOnElement(By.xpath("//input[@id='product_attribute_5_12']"));

        //verify the price $ 1415.00
        String expectedPrice = "$1,470.00";
        String actualPrice = getTextFromElement(By.xpath("//span[@id='price-value-1']"));
        //Assert.assertEquals(expectedPrice,actualPrice);

        // Click on add to cart
        clickOnElement(By.xpath("//button[@id='add-to-cart-button-1']"));

        // verify the order msg.
        String expectedOrdMsg = "The product has been added to your shopping cart";           // shopping cart
        String actualOrderMsg = getTextFromElement(By.xpath("//p[@class='content']"));
        Assert.assertEquals(actualOrderMsg, expectedOrdMsg);

        // Close the bar
        clickOnElement(By.xpath("//span[@title='Close']"));


        // Verify the cart msg
        String expectedCartMsg = "Shopping cart";
        String actualCartMsg = getTextFromElement(By.xpath("//span[contains(text(),'Shopping cart')]"));
        Assert.assertEquals(expectedCartMsg, actualCartMsg);

        // Mouse hover to shopping cart
        WebElement shoppingCart = driver.findElement(By.xpath("//span[contains(text(),'Shopping cart')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(shoppingCart).build().perform();
        shoppingCart.click();

        String shoppingCartMsg = "Shopping cart";
        String actualShoppingcartMsg = getTextFromElement(By.xpath("//h1[text()='Shopping cart']"));
        Assert.assertEquals(shoppingCartMsg, actualShoppingcartMsg);

        // Change qty. to 2 and click update
        driver.findElement(By.xpath("//input[@class='qty-input']")).clear();
        sendTextToElement(By.xpath("//input[@class='qty-input']"), "2");
        clickOnElement(By.xpath("//button[@id='updatecart']"));

        // Verify the total $2,950.00
        String expectedTotal = "$2,950.00";
        String actualTotal = getTextFromElement(By.xpath("//span[@class='value-summary']//strong[contains(text(),'$2,950.00')]"));
        Assert.assertEquals(expectedTotal, actualTotal);

        // click on I agree & checkout
        clickOnElement(By.xpath("//input[@id='termsofservice']"));
        clickOnElement(By.xpath("//button[@id='checkout']"));

        // verify the msg. Welcome, Please Sign In!
        String expectedMsg1 = "Welcome, Please Sign In!";
        String actulaMsg1 = getTextFromElement(By.xpath("//h1[normalize-space()='Welcome, Please Sign In!']"));

        // click on checkout
        clickOnElement(By.xpath("//button[normalize-space()='Checkout as Guest']"));

        // Fill the required details
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_FirstName']"), "Prime");
        //driver.findElement(RelativeLocator.with(By.tagName()))
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_LastName']"), "Testing");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_Email']"), "prime@gmail.com");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_Company']"), "Prime Ltd.");

        WebElement country = driver.findElement(By.xpath("//select[@id='BillingNewAddress_CountryId']"));
        Select select3 = new Select(country);
        select3.selectByVisibleText("United Kingdom");

        WebElement state = driver.findElement(By.xpath("//select[@id='BillingNewAddress_StateProvinceId']"));
        Select select4 = new Select(state);
        select4.selectByVisibleText("Other");

        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_City']"), "Harrow");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_Address1']"), "100");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_Address2']"), "First Lane");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_ZipPostalCode']"), "HA9 03D");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_PhoneNumber']"), "0745859565");
        sendTextToElement(By.xpath("//input[@id='BillingNewAddress_FaxNumber']"), "0192535448");
        clickOnElement(By.xpath("//button[@onclick='Billing.save()']"));


        // click on next day air
        clickOnElement(By.xpath("//label[normalize-space()='Next Day Air ($0.00)']"));
        clickOnElement(By.xpath("//button[@class='button-1 shipping-method-next-step-button']"));

        // Select credit card payment method
        clickOnElement(By.xpath("//label[normalize-space()='Credit Card']"));
        clickOnElement(By.xpath("//button[@class='button-1 payment-method-next-step-button']"));

        // Select card Type
        WebElement creditCardType = driver.findElement((By.xpath("//select[@id='CreditCardType']")));
        Select select5 = new Select(creditCardType);
        select5.selectByVisibleText("Master card");

        // Fill card details
        sendTextToElement(By.xpath("//input[@id='CardholderName']"), "Prime Testing");
        sendTextToElement(By.xpath("//input[@id='CardNumber']"), "5521573041475125");

        WebElement month = driver.findElement(By.xpath("//select[@id='ExpireMonth']"));
        Select select6 = new Select(month);
        select6.selectByVisibleText("10");

        WebElement year = driver.findElement(By.xpath("//select[@id='ExpireYear']"));
        Select select7 = new Select(year);
        select7.selectByVisibleText("2027");

        sendTextToElement(By.xpath("//input[@id='CardCode']"), "245");

        // click on payment continue button
        clickOnElement(By.xpath("//button[@class='button-1 payment-info-next-step-button']"));

        // click on confirm
        clickOnElement(By.xpath("//button[normalize-space()='Confirm']"));

        // verify the Thankyou Message
        String expectedMsg3 = "Thank you";
        String actualMsg3 = getTextFromElement(By.xpath("//h1[normalize-space()='Thank you']"));
        Assert.assertEquals(expectedMsg3, actualMsg3);

        // Verify the order msg. - Your order has been successfully processed!
        String exporderMsg = "Your order has been successfully processed!";
        String actordMsg = getTextFromElement(By.xpath("//strong[normalize-space()='Your order has been successfully processed!']"));
        Assert.assertEquals(actordMsg, exporderMsg);

        // Click continue
        clickOnElement(By.xpath("//button[normalize-space()='Continue']"));

        // Verify the msg. Welcome to out store
        String actFinalmsg = "Welcome to our store";
        String expFinalmsg = getTextFromElement(By.xpath("//h2[normalize-space()='Welcome to our store']"));

    }
@After
public void tearDown() {
        tearDown();
}
}