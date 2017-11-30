package boggle;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;

public class BoggleGUI extends JFrame {

	private JPanel mainWindow;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoggleGUI frame = new BoggleGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BoggleGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 562, 475);
		mainWindow = new JPanel();
		mainWindow.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainWindow);
		mainWindow.setLayout(new BoxLayout(mainWindow, BoxLayout.Y_AXIS));
	}

}
