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

	public static String getServerChatMessage(String message) {
		JSONObject chatMessage = new JSONObject();
		chatMessage.put("type", "chat");
		chatMessage.put("message", message);
		return chatMessage.toString();
	}

	public static String getBoggleChatMessage(String message) {

		JSONObject chatMessage = new JSONObject();
		chatMessage.put("action", "CHAT");
		chatMessage.put("message", message);
		JSONObject wrappedChatMessage = wrapApplicationMessage(chatMessage);
		return wrappedChatMessage.toString();

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

	public static String extractChatMessage(JSONObject chatMessage) {
		
		if (chatMessage.optString("module").equals("Boggle_Of_Epicness")) {
			return chatMessage.optString("username") + ": " + chatMessage.optString("message");
		} else if (chatMessage.optString("module").equals("")) {
			return chatMessage.optString("chatMessage").replace("User", "").trim();
		}
		return "Failed to get chat message";

	}

	public static char[] getDieArray(JSONObject gameStartMessage) {
		String[] tempArray = new String[16];
		tempArray = gameStartMessage.optString("board").split(",");
		tempArray[0] = tempArray[0].substring(1); // removes the char [ from the beginning of the string
		char[] dieArray = new char[16];
		if (tempArray.length == 16) {
			for (int i = 0; i < tempArray.length; i++) {
				dieArray[i] = tempArray[i].charAt(1);
			}
		} else {
			System.out.println("Error getting game dice");
		}

		return dieArray;
	}
}
