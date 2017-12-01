package boggle;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.*;

public class BoggleGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextField TimerText;
	private JTextField wordGuessed;
	private JButton[] gameDice = new JButton[16];
	private Socket socket;
	private JTextArea chatOutput, chatInput;

	/**
	 * Create the frame.
	 */
	public BoggleGUI(Socket s) {
		socket = s;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 567, 581);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JPanel bogglePanel = new JPanel();
		bogglePanel.setBounds(0, 0, 546, 291);
		mainPanel.add(bogglePanel);
		bogglePanel.setLayout(new BorderLayout(1, 1));

		JPanel gameArea = new JPanel();
		bogglePanel.add(gameArea, BorderLayout.CENTER);
		gameArea.setLayout(new BorderLayout(0, 0));

		JPanel gameBoard = new JPanel();
		gameArea.add(gameBoard, BorderLayout.CENTER);
		gameBoard.setLayout(new GridLayout(0, 4, 0, 0));

		for (int i = 0; i < 16; i++) {
			GameDie button = new GameDie(i);
			gameBoard.add(button);
			gameDice[i] = button;
		}

		JPanel wordPanel = new JPanel();
		gameArea.add(wordPanel, BorderLayout.SOUTH);
		wordPanel.setLayout(new BorderLayout(0, 0));

		TimerText = new JTextField();
		TimerText.setEditable(false);
		wordPanel.add(TimerText, BorderLayout.WEST);
		TimerText.setColumns(10);

		wordGuessed = new JTextField();
		wordPanel.add(wordGuessed, BorderLayout.CENTER);
		wordGuessed.setColumns(10);

		JPanel gameButtonPanel = new JPanel();
		wordPanel.add(gameButtonPanel, BorderLayout.EAST);

		JButton submit = new JButton("Submit");
		gameButtonPanel.add(submit);

		JButton play = new JButton("Play");
		gameButtonPanel.add(play);
		play.addActionListener(new PlayActionListener());

		JPanel guessedWordPanel = new JPanel();
		bogglePanel.add(guessedWordPanel, BorderLayout.EAST);
		guessedWordPanel.setLayout(new BoxLayout(guessedWordPanel, BoxLayout.Y_AXIS));

		JLabel guessedLabel = new JLabel("Guessed Words");
		guessedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		guessedLabel.setBackground(new Color(173, 216, 230));
		guessedLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		guessedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		guessedWordPanel.add(guessedLabel);

		ScrollPane guessedWordScrollPane = new ScrollPane();
		guessedWordPanel.add(guessedWordScrollPane);

		JTextArea guessedWords = new JTextArea();
		guessedWords.setEditable(false);
		guessedWordScrollPane.add(guessedWords);

		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(0, 297, 546, 237);
		mainPanel.add(chatPanel);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		ScrollPane chatScrollPane = new ScrollPane();
		chatPanel.add(chatScrollPane);

		chatOutput = new JTextArea();
		chatOutput.setBackground(new Color(240, 255, 255));
		chatScrollPane.add(chatOutput);
		chatOutput.setSize(getMaximumSize().width, 500);
		chatOutput.setMinimumSize(new Dimension(600, 250));

		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(new Color(240, 255, 255));
		inputPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		chatPanel.add(inputPanel);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

		chatInput = new JTextArea();
		inputPanel.add(chatInput);
		chatInput.setMaximumSize(new Dimension(600, 500));
		chatInput.setMinimumSize((new Dimension(600, 500)));

		Action ctrlEnter = new ChatAction("Send");
		JButton chatSend = new JButton(ctrlEnter);
		inputPanel.add(chatSend);

		this.setVisible(true);
	}

	public void addChat(String chatMessage) {

		chatOutput.append(chatMessage);
	}

	private class ChatAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ChatAction(String name) {
			putValue(Action.NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				PrintWriter output = new PrintWriter(socket.getOutputStream());
				output.write(JSONConverter.getChatMessage(chatInput.getText()) + "\n");
				output.flush();
				chatInput.setText("");

			} catch (IOException e1) {
				chatOutput.append("Unable to send chat messege");
				e1.printStackTrace();
			}

		}
	}
	
	private class PlayActionListener implements ActionListener {

		private static final long serialVersionUID = 1L;

		public PlayActionListener() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				PrintWriter output = new PrintWriter(socket.getOutputStream());
				output.write(JSONConverter.getPlaytMessage() + "\n");
				output.flush();

			} catch (IOException e1) {
				chatOutput.append("Unable to send play messege");
				e1.printStackTrace();
			}

		}

	}
}
