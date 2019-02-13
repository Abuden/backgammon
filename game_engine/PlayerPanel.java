package game_engine;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class represents the panel that displays player info at the top/bottom of the board.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 *
 */
public class PlayerPanel extends HBox {
	/**
	 * This class represents labels that stores player info.
	 */
	private static class PlayerInfo extends Label {
		public PlayerInfo(String string) {
			super(string);
			initStyle();
		}
		
		public void initStyle() {
			setFont(new Font("Consolas", 16));
			setTextFill(Color.WHITE);
			setWrapText(true);
		}
	}
	
	private Player player;
	private PlayerInfo playerName;
	private PlayerInfo playerScore;
	private PlayerInfo playerColour;
	
	public PlayerPanel(double width, Player player) {
		super();
		this.player = player;
		initStyle(width);
		initComponents();
		initLayout();
	}
	
	private void initStyle(double width) {
		setMinSize(width, Settings.getPlayerPanelHeight());
		setAlignment(Pos.CENTER);
		setSpacing(Settings.getPlayerLabelSpacing());
	}
	
	private void initComponents() {
		playerName = new PlayerInfo("Name: " + player.getName());
		playerScore = new PlayerInfo("Score: " + Double.toString(player.getScore()));
		
		// TODO put a checker colour beside instead of text, makes things more intuitive.
		playerColour = new PlayerInfo("Colour: " + player.getColour().name());
	}
	
	private void initLayout() {
		getChildren().addAll(playerName, playerColour, playerScore);
	}
}
