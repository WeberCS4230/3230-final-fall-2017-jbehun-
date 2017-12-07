package boggle;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.DefaultCaret;

public class BoggleGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel timerText, pointsText;
	private JTextField wordGuessed;
	private GameDie[] gameDice = new GameDie[16];
	private Socket socket;
	private JTextArea chatOutput, chatInput, guessedWords;
	private Timer gameTimer;
	private int timer;
	private String name;
	private PrintWriter output;
	private ArrayList<Integer> positionList = new ArrayList<Integer>();
	private JRadioButton serverChat, gameChat;
	private Dictionary dictionary;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the frame.
	 */
	public BoggleGUI(String n, Socket s) {

		socket = s;
		name = n;
		dictionary = new Dictionary();

		try {
			output = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			chatOutput.append("Failed to create gui output");
			e.printStackTrace();
		}

		gameTimer = new Timer(1000, new GameTimer());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 569, 704);
		JPanel mainPanel = new JPanel();
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
		wordGuessed.setFont(new Font("Tahoma", Font.BOLD, 20));
		wordGuessed.setHorizontalAlignment(SwingConstants.CENTER);
		wordGuessed.setBorder(new LineBorder(new Color(171, 173, 179), 2));
		wordGuessed.setForeground(new Color(255, 255, 240));
		wordGuessed.setBackground(new Color(0, 0, 139));
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

		JTextField title = new JTextField();
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

		guessedWords = new JTextArea();
		guessedWords.setFont(new Font("Monospaced", Font.PLAIN, 18));
		guessedWords.setPreferredSize(new Dimension(120, 22));
		guessedWords.setEditable(false);
		guessedWordScrollPane.add(guessedWords);

		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(0, 297, 546, 361);
		chatPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(chatPanel);

		JScrollPane chatScrollPane;

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
		chatSend.setPreferredSize(new Dimension(100, 50));
		inputPanel.add(chatSend, BorderLayout.EAST);

		JPanel outputPanel = new JPanel();
		chatPanel.add(outputPanel, BorderLayout.CENTER);

		chatOutput = new JTextArea();
		chatOutput.setFont(new Font("Monospaced", Font.PLAIN, 16));
		chatOutput.setBorder(new EmptyBorder(1, 5, 1, 5));
		chatOutput.setEditable(false);
		chatOutput.setAutoscrolls(true);
		chatOutput.setLineWrap(true);
		chatOutput.setBackground(new Color(245, 255, 250));
		DefaultCaret caret = (DefaultCaret) chatOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		outputPanel.setLayout(new BorderLayout(0, 0));
		chatScrollPane = new JScrollPane(chatOutput);
		outputPanel.add(chatScrollPane);

		JPanel chatSelection = new JPanel();
		outputPanel.add(chatSelection, BorderLayout.EAST);
		chatSelection.setLayout(new BoxLayout(chatSelection, BoxLayout.Y_AXIS));

		JLabel pointTitle = new JLabel("Points");
		pointTitle.setBorder(new LineBorder(new Color(0, 0, 0)));
		pointTitle.setMaximumSize(new Dimension(100, 25));
		pointTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pointTitle.setPreferredSize(new Dimension(100, 25));
		pointTitle.setHorizontalAlignment(SwingConstants.CENTER);
		chatSelection.add(pointTitle);

		pointsText = new JLabel("0");
		pointsText.setBorder(new LineBorder(new Color(0, 0, 0)));
		pointsText.setMaximumSize(new Dimension(100, 50));
		pointsText.setFont(new Font("Tahoma", Font.PLAIN, 30));
		pointsText.setHorizontalAlignment(SwingConstants.CENTER);
		chatSelection.add(pointsText);

		serverChat = new JRadioButton("Server Chat");
		buttonGroup.add(serverChat);
		serverChat.setSelected(true);
		chatSelection.add(serverChat);
		serverChat.setPreferredSize(new Dimension(100, 25));

		gameChat = new JRadioButton("Boggle Chat");
		buttonGroup.add(gameChat);
		chatSelection.add(gameChat);

		InputMap imap = chatPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke("ctrl ENTER"), "addText");
		ActionMap amap = chatPanel.getActionMap();
		amap.put("addText", ctrlEnter);

		for (int i = 0; i < 16; i++) {
			GameDie button = new GameDie(i);
			button.setFont(new Font("Tahoma", Font.PLAIN, 25));
			button.setForeground(Color.WHITE);
			button.addActionListener(new GameButtonListener());
			gameBoard.add(button);
			gameDice[i] = button;
		}

		this.setVisible(true);
	}

	public void addChat(String chatMessage) {

		chatInput.requestFocus();
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

	public void updatePoints(int points) {
		pointsText.setText(String.valueOf(points));
	}

	public void addGuessedWord(String word) {
		guessedWords.append(" " + word + "\n");
	}

	public void setupNewGameBoard(char[] charArray) {

		if (charArray.length == 16) {
			for (int i = 0; i < charArray.length; i++) {
				gameDice[i].setText(String.valueOf(charArray[i]));
				((GameDie) gameDice[i]).setLetter(charArray[i]);
			}
		}
	}

	private class ChatAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ChatAction(String name) {
			putValue(Action.NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (serverChat.isSelected()) {
				output.write(JSONConverter.getServerChatMessage(chatInput.getText()) + "\n");
				output.flush();
				addChat(name + ": " + chatInput.getText() + "\n");
				System.out.println(JSONConverter.getServerChatMessage(chatInput.getText()));
			} else if (gameChat.isSelected()) {
				output.write(JSONConverter.getBoggleChatMessage(chatInput.getText()) + "\n");
				output.flush();
				System.out.println(JSONConverter.getBoggleChatMessage(chatInput.getText()));
			} else {
				addChat("Chat type not specified. Select type and try again");
			}

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
			guessedWords.setText("");
		}

	}

	private class SubmitActionListener implements ActionListener {

		public SubmitActionListener() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] positions = new int[positionList.size()];
			StringBuilder word = new StringBuilder();
			for (int i = 0; i < positions.length; i++) {
				positions[i] = positionList.get(i);
				word.append(gameDice[positions[i]].getLetter());
			}

			if (dictionary.isAdjancentWord(positions) && dictionary.isValidWord(word.toString())) {
				output.write(JSONConverter.getGuesstMessage(positions) + "\n");
				output.flush();
			} else {
				chatOutput.append("Guessed word is invalid \n");
			}

			positionList.clear();
			wordGuessed.setText("");

			for (int i = 0; i < gameDice.length; i++) {
				gameDice[i].setEnabled(true);
				;
			}
		}

	}

	public class GameButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GameDie currentDie = (GameDie) arg0.getSource();
			positionList.add(currentDie.getPosition());
			wordGuessed.setText(wordGuessed.getText() + currentDie.getLetter());
			currentDie.setEnabled(false);
		}

	}

	private class GameTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			timer--;
			timerText.setText(String.valueOf(timer));
			if (timer <= 0) {
				stoptGameTimer();
			}
		}
	}
}
