import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddApiReq;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

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

RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
.setContentType(ContentType.JSON).build();

ResponseSpecification resSpec=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

RequestSpecification res=given().spec(req)
.body(ap);

Response response=res.when().post("/maps/api/place/add/json")
.then().spec(resSpec).extract().response();

String result = response.asString();
System.out.println(result);
	}

}
