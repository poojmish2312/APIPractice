import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
public class JiraTest {
	
	//urls-
	//https://developer.atlassian.com/server/jira/platform/rest-apis/
//	/https://docs.atlassian.com/software/jira/docs/api/REST/8.18.2/#issue-addComment
	//https://developer.atlassian.com/server/jira/platform/cookie-based-authentication/

	public static void main(String args[])
	{
	
		SessionFilter session=new SessionFilter(); //This class will help in using the same session for all the scenarios after login so that you need not to login again.
		RestAssured.baseURI = "http://localhost:8080";
		//Login Scenario
		String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json").body("{ \"username\": \"myuser\", \"password\": \"mypassword\" }") //relaxedHTTPSValidation has been added to handle https certification validation which restassured sometimes might not recognise and cause issues
		.log().all().filter(session).when().post("rest/auth/1/session").then().log().all().extract().response().asString();
		
		//Add comment API
		String expectedMessage = "Hola from another world!";
		String addComment = given().pathParam("key", "10101").log().all().header("Content-Type", "application/json").body("{\r\n" //whatever key is given here should match 
				+ "    \"body\": \""+expectedMessage+"\",\r\n" //with the key in post url inside curly braces so that during run time 
				+ "    \"visibility\": {\r\n" //RestAssured will look if any path params are defined and will replace that key with 
				+ "        \"type\": \"role\",\r\n" //with the value provided in the path param
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session).when().post("/rest/issue/{key}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js1 = new JsonPath(addComment);
		String commentId = js1.getString("id");
		
		//Add Attachment
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "10101")
		.header("Content-Type", "multipart/form-data") //here we're not sending any body in json format but we're sending a file using multiplepart so to interpret that this is needed
		.multiPart("file",new File("D:\\EclipseWorkspace\\RestProject1\\jira.txt")) //or you can send the name of the file jira.txt directly as it is stored at project level
		.when().post("rest/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get Issue
		String getResponse = given().filter(session).pathParam("key", "10101").
				queryParam("fields", "comment"). //using this to get only 1 field in the response in place of multiple fields
				log().all()
		.when().get("rest/issue/{key}")
		.then().log().all().extract().response().asString();
		
		//to check the latest comment we created and validating its content
		JsonPath js = new JsonPath(getResponse);
		int commentCount=js.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentCount; i++) {
			String commentIdIssue = js.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equals(commentId))
			{
				String message = js.get("fields.comment.comments["+i+"].body").toString();
				Assert.assertEquals(message, expectedMessage);
			}
			
		}
	}
}
