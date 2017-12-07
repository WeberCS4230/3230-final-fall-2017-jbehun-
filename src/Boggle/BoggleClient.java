package boggle;

import java.io.*;
import java.net.*;
import org.json.*;

public class BoggleClient {
	private Socket socket = null;
	private String name;
	private BoggleGUI gui;

	public BoggleClient(String n, Socket s, BoggleGUI g) {
		name = n;
		socket = s;
		gui = g;

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter output = new PrintWriter(s.getOutputStream());
			output.write(JSONConverter.getLoginMessage(name));
			output.flush();
			String message = input.readLine();
			System.out.println(message);
			if (JSONConverter.verifyLogin(message)) {
				gui.addChat("Connected\n");
				new Thread(new InputHandler(input)).start();
			} else {
				gui.addChat("User already connected\n");
				close();
			}

		} catch (IOException e) {
			gui.addChat("Connection Failed");
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			if (!socket.isInputShutdown()) {
				socket.getInputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (!socket.isOutputShutdown()) {
				socket.getOutputStream().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class InputHandler implements Runnable {

		private BufferedReader input;
		private int points = 0;

		public InputHandler(BufferedReader inputStream) {
			input = inputStream;
		}

		@Override
		public void run() {
			try {
				while (!socket.isClosed() && socket.isConnected() && !Thread.interrupted()) {
					String newMessage = input.readLine();
					System.out.println(newMessage);
					if (newMessage != null) {
						JSONObject messageType = new JSONObject(newMessage);
						switch (messageType.optString("type")) {
						case ("application"):
							applicationMessageHandler(newMessage);
							break;
						case ("chat"):
							gui.addChat(messageType.optString("fromUser") + ":  " + messageType.optString("message"));
							break;
						default:
							gui.addChat("Failed to retrieve type\n");
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}

		private void applicationMessageHandler(String newMessage) {
			JSONObject appMessage = JSONConverter.getApplicationMessage(newMessage);
			if (appMessage != null) {
				switch (appMessage.optString("action")) {
				case ("CHAT"):
					gui.addChat(JSONConverter.extractChatMessage(appMessage));
					break;
				case ("STARTGAME"):
					StartGame(appMessage);
					break;
				case ("GAMEEND"):
					gui.stoptGameTimer();
					break;
				case ("POINTS"):
					addPoints(appMessage);
					break;
				case ("WORD"):
					gui.addGuessedWord(appMessage.optString("word"));
					break;
				default:
					gui.addChat("Failed to get action: " + appMessage + "\n");
				}
			}
		}

		private void addPoints(JSONObject appMessage) {
			String p = appMessage.optString("points");
			points += Integer.parseInt(p);
			gui.addChat(name + ": earned " + p + " point(s)\n");
			gui.updatePoints(points);
		}

		private void StartGame(JSONObject gameStartMessage) {
			gui.startGameTimer();
			char[] dieArray = JSONConverter.getDieArray(gameStartMessage);
			gui.setupNewGameBoard(dieArray);
			gui.addChat("Game started! Go!");
			points = 0;
			gui.updatePoints(points);
		}
	}

}