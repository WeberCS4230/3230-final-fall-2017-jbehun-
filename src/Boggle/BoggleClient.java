package boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
			gui.addChat(input.readLine());

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

		public InputHandler(BufferedReader inputStream) {
			input = inputStream;
		}

		@Override
		public void run() {
			try {
				while (!socket.isClosed() && socket.isConnected() && !Thread.interrupted()) {
					String newMessage = input.readLine();
					gui.addChat(newMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	}

}