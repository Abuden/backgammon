package game;

import java.io.InputStream;
import java.util.Random;

import interfaces.ColorParser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class represents the Dice object in Backgammon game.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 *
 */
public class Dice extends ImageView implements ColorParser {
	private final int MAX_DICE_SIZE = 6;
	private Image[] dices;
	private int diceRollResult;
	
	/**
	 * Default Constructor
	 * 		- Initialize the dices array with the possible dice images (i.e. 1-6).
	 * @param colour
	 */
	public Dice(Color colour) {
		super();
		dices = new Image[MAX_DICE_SIZE];
		initImages(colour);
	}
	
	/**
	 * Initializes the dice images:
	 * 		- by getting the images from file,
	 * 		- adding them to the dices array.
	 * @param colour
	 */
	private void initImages(Color color) {
		String colorString = parseColor(color);
		for (int i = 0; i < dices.length; i++) {
			InputStream input = getClass().getResourceAsStream("img/dices/" + colorString + "/" + (i+1) + ".png");
			dices[i] = new Image(input);
		}
	}
	
	/**
	 * Rotate the dice.
	 */
	private void rotate() {
		// rotation range of 15 to -15.
		Random rand = new Random();
		int rotation = rand.nextInt(30) - 15 + 1;
		setRotate(rotation);
	}
	
	/**
	 * Get the roll dice result (i.e. number from 1 to 6),
	 * draw the dice with the result,
	 * return the result.
	 * @return the result of rolling the dice.
	 */
	public int roll() {
		Random rand = new Random();
		int res = rand.nextInt(MAX_DICE_SIZE) + 1;
		this.diceRollResult = res;
		return res;
	}
	
	/**
	 * Set the image of dice based on result.
	 * i.e. If result is 1, show image with dice at 1.
	 * @param result of rolling the dice.
	 */
	public void draw(int result) {
		setImage(dices[result-1]);
		rotate();
	}
	public void draw() {
		setImage(dices[diceRollResult-1]);
		rotate();
	}
}