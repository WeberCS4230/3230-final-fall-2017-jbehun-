package boggle;

import java.awt.Color;

import javax.swing.*;

public class GameDie extends JButton{
	
	private static final long serialVersionUID = 1L;
	private int position;
	private char letter;

	public GameDie(int p) {
		position = p;
		this.setOpaque(true);
		this.setBackground(new Color(100, 149, 237));
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "GameDice [position=" + position + ", letter=" + letter + "]";
	}
	
	
}
