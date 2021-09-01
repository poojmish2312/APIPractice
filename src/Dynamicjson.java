import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReusableMethods;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Dynamicjson {
	
	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
		.body(Payload.addBook(isbn,aisle)).
		when().post("Library/Addbook.php")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
		
		//deletebook
		String resdel = given().log().all().header("Content-Type", "application/json").
		body(Payload.deleteBook(id)).
		when().post("Library/DeleteBook.php")
		.then().assertThat().log().all().statusCode(200)
		.extract().response().asString();
		/*
		 * JsonPath js1 = ReusableMethods.rawToJson(resdel); String msg =
		 * js1.get("msg"); Assert.assertEquals(msg, "book is successfully deleted");
		 */
	}
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		//array- collection of elements
		//Multidimensional array- collection of arrays
		return new Object[][] {{"sjghx","8787"},{"sbsh","7726"},{"sjdbh","27082"}};
	}

}
