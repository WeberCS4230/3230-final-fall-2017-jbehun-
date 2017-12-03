package boggle;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.*;

public class BoggleGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JLabel timerText;
	private JTextField wordGuessed;
	private JButton[] gameDice = new JButton[16];
	private Socket socket;
	private JTextArea chatOutput, chatInput;
	private JTextField title;
	private Timer gameTimer;
	private int timer;
	private PrintWriter output;

	/**
	 * Create the frame.
	 */
	public BoggleGUI(Socket s) {
		socket = s;
		try {
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			chatOutput.append("Failed to create gui output");
			e.printStackTrace();
		}

		gameTimer = new Timer(1000, new GameTimer());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 569, 583);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		JPanel bogglePanel = new JPanel();
		bogglePanel.setBackground(new Color(176, 196, 222));
		bogglePanel.setBounds(0, 0, 546, 298);
		mainPanel.add(bogglePanel);
		bogglePanel.setLayout(new BorderLayout(1, 1));

		JPanel gameArea = new JPanel();
		gameArea.setForeground(new Color(0, 0, 0));
		gameArea.setBackground(new Color(224, 255, 255));
		bogglePanel.add(gameArea, BorderLayout.CENTER);
		gameArea.setLayout(new BorderLayout(0, 0));

		JPanel gameBoard = new JPanel();
		gameArea.add(gameBoard, BorderLayout.CENTER);
		gameBoard.setLayout(new GridLayout(0, 4, 0, 0));

		JPanel wordPanel = new JPanel();
		wordPanel.setBackground(new Color(176, 196, 222));
		gameArea.add(wordPanel, BorderLayout.SOUTH);
		wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.X_AXIS));

		timerText = new JLabel();
		timerText.setBackground(new Color(176, 196, 222));
		timerText.setOpaque(true);
		timerText.setPreferredSize(new Dimension(70, 60));
		timerText.setMinimumSize(new Dimension(40, 0));
		timerText.setFont(new Font("Tahoma", Font.PLAIN, 25));
		timerText.setHorizontalAlignment(SwingConstants.CENTER);
		timerText.setText("60");
		wordPanel.add(timerText);

		wordGuessed = new JTextField();
		wordGuessed.setBorder(new LineBorder(new Color(171, 173, 179), 2));
		wordGuessed.setForeground(new Color(255, 255, 255));
		wordGuessed.setBackground(new Color(176, 224, 230));
		wordGuessed.setEditable(false);
		wordPanel.add(wordGuessed);
		wordGuessed.setColumns(10);

		JPanel gameButtonPanel = new JPanel();
		wordPanel.add(gameButtonPanel);

		JButton submit = new JButton("Submit");
		submit.setFont(new Font("Tahoma", Font.PLAIN, 13));
		submit.setPreferredSize(new Dimension(75, 55));
		submit.addActionListener(new SubmitActionListener());
		gameButtonPanel.add(submit);

		JButton play = new JButton("Play");
		play.setFont(new Font("Tahoma", Font.PLAIN, 13));
		play.setPreferredSize(new Dimension(70, 55));
		gameButtonPanel.add(play);

		title = new JTextField();
		title.setPreferredSize(new Dimension(6, 40));
		title.setForeground(new Color(255, 255, 255));
		title.setBackground(new Color(0, 0, 255));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		title.setText("Boggle Of Epicness");
		gameArea.add(title, BorderLayout.NORTH);
		title.setColumns(10);
		play.addActionListener(new PlayActionListener());

		JPanel guessedWordPanel = new JPanel();
		guessedWordPanel.setBackground(new Color(230, 230, 250));
		bogglePanel.add(guessedWordPanel, BorderLayout.EAST);
		guessedWordPanel.setLayout(new BoxLayout(guessedWordPanel, BoxLayout.Y_AXIS));

		JLabel guessedLabel = new JLabel("Guessed Words");
		guessedLabel.setOpaque(true);
		guessedLabel.setForeground(new Color(0, 0, 205));
		guessedLabel.setPreferredSize(new Dimension(130, 25));
		guessedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		guessedLabel.setBackground(new Color(230, 230, 250));
		guessedLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		guessedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		guessedWordPanel.add(guessedLabel);

		ScrollPane guessedWordScrollPane = new ScrollPane();
		guessedWordPanel.add(guessedWordScrollPane);

		JTextArea guessedWords = new JTextArea();
		guessedWords.setPreferredSize(new Dimension(120, 22));
		guessedWords.setEditable(false);
		guessedWordScrollPane.add(guessedWords);

		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(0, 297, 546, 237);
		mainPanel.add(chatPanel);
		chatPanel.setLayout(new BorderLayout(0, 0));

		ScrollPane chatScrollPane = new ScrollPane();
		chatPanel.add(chatScrollPane, BorderLayout.CENTER);

		chatOutput = new JTextArea();
		chatOutput.setFont(new Font("Monospaced", Font.PLAIN, 16));
		chatOutput.setBorder(new EmptyBorder(1, 5, 1, 5));
		chatOutput.setEditable(false);
		chatOutput.setBackground(new Color(240, 255, 255));
		chatScrollPane.add(chatOutput);


		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(new Color(240, 255, 255));
		inputPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setLayout(new BorderLayout(0, 0));

		chatInput = new JTextArea();
		chatInput.setFont(new Font("Monospaced", Font.PLAIN, 14));
		chatInput.setBorder(new EmptyBorder(1, 5, 1, 5));
		chatInput.setPreferredSize(new Dimension(40, 60));
		inputPanel.add(chatInput);
		chatInput.setMaximumSize(new Dimension(600, 100));
		chatInput.setMinimumSize(new Dimension(600, 100));

		Action ctrlEnter = new ChatAction("Send");
		JButton chatSend = new JButton(ctrlEnter);
		chatSend.setPreferredSize(new Dimension(90, 50));
		inputPanel.add(chatSend, BorderLayout.EAST);
		
		InputMap imap = chatPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke("ctrl ENTER"), "addText");
		ActionMap amap = chatPanel.getActionMap();
		amap.put("addText", ctrlEnter);

		for (int i = 0; i < 16; i++) {
			GameDie button = new GameDie(i);
			gameBoard.add(button);
			gameDice[i] = button;
		}

		this.setVisible(true);
	}

	public void addChat(String chatMessage) {

		chatOutput.append(chatMessage + "\n");
	}

	public void startGameTimer() {
		gameTimer.start();
		timer = 60;
		timerText.setText(String.valueOf(timer));
	}

	public void stoptGameTimer() {
		gameTimer.stop();
		timerText.setText(String.valueOf(0));
	}

	private class ChatAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ChatAction(String name) {
			putValue(Action.NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			output.write(JSONConverter.getChatMessage(chatInput.getText()) + "\n");
			output.flush();
			System.out.println(JSONConverter.getChatMessage(chatInput.getText()));
			chatInput.setText("");
		}
	}

	private class PlayActionListener implements ActionListener {

		public PlayActionListener() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			output.write(JSONConverter.getPlaytMessage() + "\n");
			output.flush();
		}

	}

	private class SubmitActionListener implements ActionListener {

		public SubmitActionListener() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] positions = { 8, 4, 1 };
			output.write(JSONConverter.getGuesstMessage(positions) + "\n");
			output.flush();
			chatOutput.append(JSONConverter.getGuesstMessage(positions) + "\n");
		}

	}

	private class GameTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			timer--;
			timerText.setText(String.valueOf(timer));
		}
	}
}
