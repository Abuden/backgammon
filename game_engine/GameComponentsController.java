package game_engine;

import constants.GameConstants;
import constants.MoveResult;
import game.Bar;
import game.Bars;
import game.Board;
import game.Home;
import game.HomePanel;
import game.Pip;
import game.PlayerPanel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class represents the game made up of separate components in Backgammon.
 * This class creates a game made out of modular panes/nodes.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class GameComponentsController extends VBox {
	private PlayerPanel topPlayerPnl, btmPlayerPnl;
	private Bars bars;
	private Board board;
	private HomePanel leftHome, rightHome, mainHome;
	
	/**
	 * Default Constructor
	 * 		- Initializes the modular game components.
	 */
	public GameComponentsController(Player bottomPlayer, Player topPlayer) {
		super();
		initGameComponents(bottomPlayer, topPlayer);
	}
	
	/**
	 * Initializes the game by creating the components and putting them together.
	 * i.e. initializing the game layout.
	 */
	public void initGameComponents(Player bottomPlayer, Player topPlayer) {
		board = new Board(this);
		bars = new Bars();
		leftHome = new HomePanel();
		rightHome = new HomePanel();
		switch (Settings.getMainQuadrant()) {
			case BOTTOM_RIGHT:
			case TOP_RIGHT:
				mainHome = rightHome;
				break;
			case BOTTOM_LEFT:
			case TOP_LEFT:
				mainHome = leftHome;
				break;
		}
		
		HBox middlePart = board;
		middlePart.setMinWidth(GameConstants.getMiddlePartWidth());
		middlePart.getChildren().add(1, bars);
		middlePart.getChildren().add(0, leftHome);
		middlePart.getChildren().add(rightHome);
		
		topPlayerPnl = new PlayerPanel(middlePart.getMinWidth(), topPlayer);
		btmPlayerPnl = new PlayerPanel(middlePart.getMinWidth(), bottomPlayer);
		
		getChildren().addAll(topPlayerPnl, middlePart, btmPlayerPnl);
		setBackground(GameConstants.getGameImage());
		setMaxHeight(topPlayerPnl.getMinHeight() + middlePart.getHeight() + btmPlayerPnl.getMinHeight());
		
		if (GameConstants.FORCE_13_CHECKERS_AT_HOME) {
			mainHome.getHome(topPlayer.getColor()).initCheckers(13, topPlayer.getColor());
			mainHome.getHome(bottomPlayer.getColor()).initCheckers(13, bottomPlayer.getColor());
		}
	}
	
	/**
	 * Removes all checkers from board (pips, homes, bars)
	 */
	public void removeAllCheckers() {		
		// Remove from pips.
		Pip[] pips = board.getPips();		
		for (int i = 0; i < pips.length; i++) 
			pips[i].removeAllCheckers();
		
		// Remove from homes.
		mainHome.getHome(Settings.getTopPerspectiveColor()).removeAllCheckers();
		mainHome.getHome(Settings.getBottomPerspectiveColor()).removeAllCheckers();
		
		// Remove from bars.
		bars.getBar(Settings.getTopPerspectiveColor()).removeAllCheckers();
		bars.getBar(Settings.getBottomPerspectiveColor()).removeAllCheckers();
	}	
	
	/**
	 * Un-highlight everything.
	 * Pips, top checkers, bar, homes.
	 */
	public void unhighlightAll() {
		board.unhighlightPipsAndCheckers();
		mainHome.unhighlight();
		bars.unhighlight();
	}
	
	/**
	 * Moves a checker from a pip to the bar.
	 * i.e. pops a checker from one pip and push it to the bar.
	 * 
	 * @param fro, zero-based index, the pip number to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToBar(int fro) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Pip[] pips = board.getPips();
		Bar bar = bars.getBar(pips[fro].top().getColor());
		// Checking if its empty is actually done by moveCheckers,
		// since this method is always called after moveCheckers.
		// so this is actually not needed, but is left here just in case.
		if (!pips[fro].isEmpty()) {
			bar.push(pips[fro].pop());
			moveResult = MoveResult.MOVED_TO_BAR;
			
			pips[fro].drawCheckers();
			bar.drawCheckers();
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from bar to a pip.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fromBar, color of the bar to pop from.
	 * @param toPip, zero-based index, the pip number to push to.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveFromBar(Color fromBar, int toPip) {
		MoveResult moveResult = board.isBarToPipMove(fromBar, toPip);
		
		switch (moveResult) {
			case MOVED_FROM_BAR:
				Pip[] pips = board.getPips();
				Bar bar = getBars().getBar(fromBar);
				pips[toPip].push(bar.pop());
				pips[toPip].drawCheckers();
				bar.drawCheckers();
				break;
			case MOVE_TO_BAR:
				break;
			default:
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from a pip to its home.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fromPip, zero-based index, the pip number to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToHome(int fromPip) {
		MoveResult moveResult = board.isPipToHomeMove(fromPip, null);
		
		switch (moveResult) {
			case MOVED_TO_HOME_FROM_PIP:
				Pip[] pips = board.getPips();
				Home home = mainHome.getHome(pips[fromPip].top().getColor());
				home.push(pips[fromPip].pop());
				
				pips[fromPip].drawCheckers();
				home.drawCheckers();
				break;
			default:
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from bar to its home.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fromBar, color of the bar to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToHome(Color fromBar) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Bar bar = bars.getBar(fromBar);
		if (!bar.isEmpty()) {
			Home home = mainHome.getHome(bar.top().getColor());
			home.push(bar.pop());
			moveResult = MoveResult.MOVED_TO_HOME_FROM_BAR;

			bar.drawCheckers();
			home.drawCheckers();
		}
		return moveResult;
	}
	
	public Board getBoard() {
		return board;
	}
	public PlayerPanel getTopPlayerPanel() {
		return topPlayerPnl;
	}
	public PlayerPanel getBottomPlayerPanel() {
		return btmPlayerPnl;
	}
	public HomePanel getMainHome() {
		return mainHome;
	}
	public Bars getBars() {
		return bars;
	}
}
