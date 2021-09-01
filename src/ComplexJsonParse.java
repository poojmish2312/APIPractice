import java.util.Iterator;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String args[])
	{
	JsonPath js = new JsonPath(Payload.CoursePrice()); //we just have the response here, not the request so we have mocked the response to complete rest of the validations
	System.out.println("Print No of courses returned by API");
	int coursecount = js.getInt("courses.size()"); //this size method can only be applied on array and as we know that courses is an array in the response json so it'll give the count of courses
	System.out.println(coursecount);
	
	System.out.println("Print Purchase Amount");
	int amt = js.getInt("dashboard.purchaseAmount");
	System.out.println(amt);
	
	System.out.println("Print Title of the first course");
	String title = js.get("courses[0].title");//get method by default returns String. getString() can also be used
	System.out.println(title);
	
	System.out.println("Print All course titles and their respective Prices");
	for (int i = 0; i < coursecount; i++) {
		System.out.println(js.get("courses["+i+"].title").toString());
		System.out.println(js.get("courses["+i+"].price").toString());
	}
	
	System.out.println("Print no of copies sold by RPA Course");
	for (int i = 0; i < coursecount; i++) {
		String ctitle = js.get("courses["+i+"].title");
		if(ctitle.equals("RPA"))
		{
			System.out.println(js.get("courses["+i+"].copies").toString());
			break;
		}
	}
	}

}
