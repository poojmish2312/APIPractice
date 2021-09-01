import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import files.ExcelReader;
import files.ReusableMethods;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import resources.*;

public class MapToJsonInAPI {

	@Test
	public void addBook() throws IOException
	{
		ExcelReader d=new ExcelReader();
		ArrayList data=d.excelReader("RestAddbook","RestAssured");
		
		
		HashMap<String, Object>  map = new HashMap<>();
		map.put("name", data.get(1));
		map.put("isbn", data.get(2));
		map.put("aisle", data.get(3));
		map.put("author", data.get(4));
		
	/*	HashMap<String, Object>  map2 = new HashMap<>(); //for nested json
		map.put("lat", "12");
		map.put("lng", "34");
		map.put("location", map2);*/
		
		
		RestAssured.baseURI="http://216.10.245.166";
		String resp=given().
				header("Content-Type","application/json").
		body(map).
		when().
		post("/Library/Addbook.php").
		then().assertThat().statusCode(200).
		extract().response().asString();
		 JsonPath js= ReusableMethods.rawToJson(resp);
		   String id=js.get("ID");
		   System.out.println(id);
	}

}
