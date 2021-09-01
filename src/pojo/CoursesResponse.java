package pojo;

import java.util.List;

public class CoursesResponse {
	private List<WebAutomationResponse> webAutomation; //as this is a nested json so another pojo class is created for this
	private List<ApiResponse> api;
	private List<MobileResponse> mobile;
	public List<WebAutomationResponse> getWebAutomation() {
		return webAutomation;
	}
	public void setWebAutomation(List<WebAutomationResponse> webAutomation) {
		this.webAutomation = webAutomation;
	}
	public List<ApiResponse> getApi() {
		return api;
	}
	public void setApi(List<ApiResponse> api) {
		this.api = api;
	}
	public List<MobileResponse> getMobile() {
		return mobile;
	}
	public void setMobile(List<MobileResponse> mobile) {
		this.mobile = mobile;
	}

}
