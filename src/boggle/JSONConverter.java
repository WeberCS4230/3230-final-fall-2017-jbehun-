package boggle;

import org.json.*;

public class JSONConverter {

	
	public static String getLoginMessage(String name) {
		
		JSONObject login = new JSONObject();
        JSONObject username = new JSONObject();
        username.put("username","name");
        login.put("type", "login");
        login.put("message", username);
		return login.toString() + "\n";
		
	}
	
	
	public static boolean verifyLogin(String loginResponse)
	{
		JSONObject validateLogin = new JSONObject(loginResponse);
		String type = validateLogin.optString("type");
		
		return false;
		
	}
}
