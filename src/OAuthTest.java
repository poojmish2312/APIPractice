import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.ApiResponse;
import pojo.GetCourseResponse;
import pojo.WebAutomationResponse;
public class OAuthTest {
	public static void main(String[] args) throws InterruptedException {
		String[] courseTitles = {"Selenium WebDriver Java","Cypress","Protractor"};
		//get code - commented this as google doesn't allow hitting the url with test automation framework now
		/*
		 * System.setProperty("webdriver.chrome.driver",
		 * "D://EclipseWorkspace/chromedriver.exe"); WebDriver driver = new
		 * ChromeDriver(); driver.get(
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php"
		 * ); driver.findElement(By.cssSelector("input[type='email']")).sendKeys(
		 * "srinath19830");
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER
		 * ); Thread.sleep(3000);
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(
		 * "password");
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER
		 * ); Thread.sleep(3000); String url= driver.getCurrentUrl();
		 */
		String url ="https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FvAHBQUZU6o4WJ719NrGBzSELBFVBI9XbxvOtYpmYpeV47bFVExkaxWaF_XR14PHtTZf7ILSEeamywJKwo_BYs9M&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&session_state=0c32992f0d47e93d273922018ade42d1072b9d1f..a35c&prompt=none#";
		String partialurl=url.split("code=")[1];
		String code=partialurl.split("&scope")[0];
		
		//exchange code
		String accessTokenResponse=given().urlEncodingEnabled(false). //rest assured changes any special character into numerical so we have to add this so that won't be done and we'll get the value as it is
		queryParams("code",code).queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
		queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("grant_type", "authorization_code")
        .queryParams("state", "verifyfjdss")
        .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php").
        when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		//actual request
		GetCourseResponse actualrequestResponse=given().queryParam("access_token", accessToken).
		expect().defaultParser(Parser.JSON). //this is needed in case the content type in the headers of the response is not application/json 
		when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourseResponse.class);
		System.out.println(actualrequestResponse.getLinkedIn());
		System.out.println(actualrequestResponse.getInstructor());
		//to get the title of the course if index is fixed(see ActualRequestResponse.txt file in JSON online editor)
		actualrequestResponse.getCourses().getApi().get(1).getCourseTitle();
		
		//getting price of a particular course dynamically
		List<ApiResponse> apiCourses = actualrequestResponse.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
					System.out.println( apiCourses.get(i).getPrice());
		}
		
		//getting all the course titles under webAutomation
		ArrayList<String> ar = new ArrayList<String>();
		List<String> expected=Arrays.asList(courseTitles);
		List<WebAutomationResponse> webCourses = actualrequestResponse.getCourses().getWebAutomation();
		for (int i = 0; i < webCourses.size(); i++) {
			ar.add(webCourses.get(i).getCourseTitle());
		}
		Assert.assertTrue(ar.equals(expected));
	}
}
