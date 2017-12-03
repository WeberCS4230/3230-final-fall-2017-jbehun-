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
		chatMessage.put("action", "Chat");
		chatMessage.put("message", message);
		JSONObject wrappedPlayMessage = wrapApplicationMessage(chatMessage);
		return wrappedPlayMessage.toString();

	}

	public static String getPlaytMessage() {

		JSONObject playMessage = new JSONObject();
		playMessage.put("action", "PLAY");
		JSONObject wrappedPlayMessage = wrapApplicationMessage(playMessage);
		return wrappedPlayMessage.toString();

	}

	public static String getGuesstMessage(int[] positions) {

		JSONObject guessMessage = new JSONObject();
		guessMessage.put("action", "GUESS");
		JSONArray p = new JSONArray();
		for (int i = 0; i < positions.length; i++) {
			p.put(positions[i]);
		}
		guessMessage.put("positions", p);
		JSONObject wrappedGuessMessage = wrapApplicationMessage(guessMessage);
		return wrappedGuessMessage.toString();

	}

	private static JSONObject wrapApplicationMessage(JSONObject module) {
		JSONObject wrappedInApplicattion = new JSONObject();
		module.put("module", "Boggle_Of_Epicness");
		wrappedInApplicattion.put("type", "application");
		wrappedInApplicattion.put("message", module);

		return wrappedInApplicattion;
	}

	public static boolean verifyLogin(String loginResponse) {
		JSONObject validateLogin = new JSONObject(loginResponse);
		String type = validateLogin.optString("type");
		if (type != null && type.equals("acknowledge")) {
			return true;
		}

		return false;

	}

	public static JSONObject getApplicationMessage(String serverMessage) {

		JSONObject applicationMessage = null;
		JSONObject message = new JSONObject(serverMessage);
		if (message.optString("type") != null && message.optString("type").equals("application")) {
			applicationMessage = new JSONObject(message.optString("message"));
		}
		return applicationMessage;

	}

	public static void main(String args[]) {
		System.out.println(getChatMessage("Hello World"));
		System.out.println(getPlaytMessage());
		int[] positions = { 4, 5, 6, 7 };
		System.out.println(getGuesstMessage(positions));
	}
}
