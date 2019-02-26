package ui;

import constants.GameConstants;
import javafx.scene.control.TextField;

/**
 * A TextField that takes in player input.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * 
 */
public class CommandPanel extends TextField {
	public CommandPanel () {
		super();
		setPromptText("Player inputs text here, then hit Enter\n");
		setMinHeight(GameConstants.getUIHeight());
	}
}