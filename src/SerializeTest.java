import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddApiReq;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializeTest {

	public static void main(String[] args) {
AddApiReq ap = new AddApiReq();
ap.setAccuracy(50);
ap.setAddress("29, side layout, cohen 09");
ap.setLanguage("French-IN");
ap.setName("Frontline house");
ap.setPhone_number("(+91) 983 893 3937");
ap.setWebsites("http://google.com");
List<String> l = new ArrayList<String>();
l.add("shoe park");
l.add("shop");
ap.setTypes(l);
Location lo = new Location();
lo.setLat(-38.383494);
lo.setLng(33.427362);
ap.setLocation(lo);
RestAssured.baseURI="https://rahulshettyacademy.com";
Response res=given().queryParam("key", "qaclick123")
.body(ap)
.when().post("/maps/api/place/add/json")
.then().assertThat().statusCode(200).extract().response();
String response = res.asString();
System.out.println(response);
	}

}
