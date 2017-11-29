package Boggle;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class BoggleMain {

	public static void main(String[] args) {
		Socket s = null;
		String host = JOptionPane.showInputDialog("Enter the server address");
		try {
			s = new Socket(host, 8989);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (s.isConnected()) {
			String name = JOptionPane.showInputDialog("Enter a user name");
			
			
		}

	}

}
