package boggle;

import org.json.*;

public class JSONConverter {

	public static String getLoginMessage(String name) {

		JSONObject login = new JSONObject();
		JSONObject username = new JSONObject();
		username.put("username", name);
		login.put("type", "login");
		login.put("message", username);
		return login.toString() + "\n";

	}

	public static String getChatMessage(String message) {

		JSONObject chatMessage = new JSONObject();
		chatMessage.put("type", "chat");
		chatMessage.put("message", message);
		return chatMessage.toString();

	}
	
	public static String getPlaytMessage() {

		JSONObject playMessage = new JSONObject();
		playMessage.put("module", "Boggle_Of_Epicness");
		playMessage.put("action","PLAY");
		JSONObject wrappedPlayMessage = wrapApplicationMessage(playMessage);
		return wrappedPlayMessage.toString();

	}
	
	

	private static JSONObject wrapType(String type) {
		JSONObject wrappedInType = new JSONObject();
		wrappedInType.put("type", type);
		return wrappedInType;
	}

	private static JSONObject wrapApplicationMessage(JSONObject module) {
		JSONObject wrappedInApplicattion = new JSONObject();
		wrappedInApplicattion.put("type","application");
		wrappedInApplicattion.put("message", module);
		
		
		return wrappedInApplicattion;
	}

	private JSONObject wrapApprlicationMessage(JSONObject object) {

		return object;

	}

	public static boolean verifyLogin(String loginResponse) {
		JSONObject validateLogin = new JSONObject(loginResponse);
		String type = validateLogin.optString("type");
		if (type != null && type.equals("acknowledge")) {
			return true;
		}

		return false;

	}
	
	public static void main(String args[])
	{
		System.out.println(getChatMessage("Hello World")); 
		System.out.println(getPlaytMessage());
	}
}
