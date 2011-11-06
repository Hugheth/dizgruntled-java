package com.hugheth.dizgruntled.level;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.GruntzGame;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.logic.Logic;
import com.hugheth.dizgruntled.manager.CellManager;
import com.hugheth.dizgruntled.manager.LogicManager;
import com.hugheth.dizgruntled.manager.Manager;
import com.hugheth.dizgruntled.manager.Map;
import com.hugheth.dizgruntled.manager.PressureManager;
import com.hugheth.dizgruntled.manager.TimeManager;
import com.hugheth.dizgruntled.manager.TriggerManager;
import com.hugheth.dizgruntled.manager.SnapshotManager;
import com.hugheth.dizgruntled.manager.UIManager;
import com.hugheth.dizgruntled.player.HumanPlayer;
import com.hugheth.dizgruntled.player.Player;

/**
 * 
 */
public class Level extends BasicGameState implements Stateful {

	/**
	 * The stateful aspects of the level
	 */
	private LevelState _state;
	
	/**
	 * The Map storing the current state
	 */
	private Map _map;
	
	/**
	 * The managers of the level 
	 */
	private TreeMap<String, Manager> _managers = new TreeMap<String, Manager>();
	
	/**
	 * A map of currently pressed keys
	 */
	private HashSet<Integer> _keys = new HashSet<Integer>();
	
	// Game
	private GruntzGame _game;
	
	/**
	 * The human player that controls this computer
	 */
	private HumanPlayer _human;
	
	/**
	 * The array of players in the level
	 */
	private Player[] _players;
	
	/**
	 * Dragging a box
	 */
	private int[] _drag;
	
	public Level() throws Exception {
		
		
	}

	public int getID() {
		return 1;
	}
	
	public GruntzGame getGame() {
		return _game;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		try {
			
			// Create the managers
			
			// Time manager
			new TimeManager(this);
			// Snapshot manager
			new SnapshotManager(this);
			// UI manager
			new UIManager(this);
			// Load a map
			_map = new Map(this);
			// Pressure manager
			new PressureManager(this);
			// Cell manager
			new CellManager(this);
			// Trigger manager
			new TriggerManager(this);
			// Logic manager
			new LogicManager(this);
			
			// Get the initial state
			_state = new LevelState();
			
			// Set the game
			_game = (GruntzGame) game;
			
			// Setup the tile traits
			TileBehaviour.setup();
			
			try {
				
				MessageDigest digest = MessageDigest.getInstance("MD5");
				byte[] code = digest.digest(ResourceLoader.getJSONForPath("mapz/level01").toString().getBytes());
				
				// Prevent level from being edited
				byte[] level = new byte[] {
					122,
					-95,
					-114,
					-2,
					14,
					-82,
					119,
					-106,
					97,
					89,
					72,
					54,
					69,
					63,
					92,
					27
				};
				
				for (int i = 0; i < code.length; i++) {
					if (code[i] != level[i]) {
						GruntzGame.legit = false;
						System.out.println("The level has been modified from its original form and so rewards will not be uploaded. Please revert the level to its original form to re-enable reward saving.");
						break;
					}
				}
				
				_map.load(ResourceLoader.getJSONForPath("mapz/level01"));
				
			} catch (GruntException e) {
				
				throw new GruntException("ABORT: Failed to load the level.", e);
			}
			
			// Copy over logics
			for (Logic logic: _map.getLogics()) {
				getLogicManager().attachLogic(logic);
			}
			
			getLogicManager().sort();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a manager to the level. Typically only the Manager
	 * class itself should use this method.
	 * 
	 *  @param manager The manager to add to the level
	 */
	public void addManager(Manager manager) {
		_managers.put(manager.getName(), manager);
	}
	/**
	 * Retrieve a manager from the level
	 * @param name The name of the manager
	 * @return The manager requested
	 */
	public Manager getManager(String name) {
		return _managers.get(name);
	}
	public TimeManager getTimeManager() {
		return (TimeManager) _managers.get("time");
	}
	public LogicManager getLogicManager() {
		return (LogicManager) _managers.get("logic");
	}
	public PressureManager getPressureManager() {
		return (PressureManager) _managers.get("pressure");
	}
	public TriggerManager getTriggerManager() {
		return (TriggerManager) _managers.get("trigger");
	}
	public CellManager getCellManager() {
		return (CellManager) _managers.get("cell");
	}
	public UIManager getUIManager() {
		return (UIManager) _managers.get("ui");
	}
	public SnapshotManager getSnapshotManager() {
		return (SnapshotManager) _managers.get("snapshot");
	}
	
	@Override
	public void keyPressed(int key, char c) {
		
		if (!getUIManager().key(key)) return;
		
		_keys.add(key);
				
	}
	
	@Override
	public void keyReleased(int key, char c) {
		_keys.remove(key);
	}
	
	public Input getInput() {
		return _game.getContainer().getInput();
	}
	
	public float getOffsetX() {
		return _state.offset[0];
	}
	public float getOffsetY() {
		return _state.offset[1];
	}
	
	// Get player
	public Player getPlayer(int id) {
		return _players[id];
	}
	public void setPlayers(Player[] players) {
		_players = players;
		// Check for human player
		for (Player player: _players) {
			try {
				_human = (HumanPlayer) player;
				break;
			} catch (Exception e) {}
		}
	}
	
	// Get time
	public long getFrame() {
		return 0L;
		//return curTime - startTime;
	}
	
	// Play sound
	public void playSound(String name, float volume) {
		try {
			Sound sound = new Sound("soundz/" + name);
			sound.play(1, volume / 100);
		} catch (Exception e) {
			System.out.println("IGNORE: Couldn't play the sound [" + name + "]");
		}
	}
	public void loopSound(String name, float volume) {
		try {
			Sound sound = new Sound("soundz/" + name);
			sound.loop(1, volume / 100);
		} catch (Exception e) {
			System.out.println("IGNORE: Couldn't play the sound [" + name + "]");
		}
	}
	
	// Play sound at
	public void playSoundAt(String name, Pair xy, float volume) {
		try {
			Sound sound = new Sound("soundz/" + name);
			sound.play(1, volume / 100);
		} catch (Exception e) {
			System.out.println("IGNORE: Couldn't play the sound [" + name + "]");
		}
	}
	
	// Mouse pressed
	@Override
	public void mousePressed(int button, int x, int y) {
		
		if (!getUIManager().click(x, y)) return;
		
		if (button == 0) {
			_human.leftClick(x + (int) _state.offset[0], y + (int) _state.offset[1]);
		} else
			_human.rightClick(x + (int) _state.offset[0], y + (int) _state.offset[1]);
		
		_drag = new int[]{x, y, x, y};
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (_drag == null) return;
		_drag[2] = newx;
		_drag[3] = newy;
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		if (_drag == null) return;
		
		_drag = new int[]{(int) (_drag[0] + _state.offset[0]), (int) (_drag[1] + _state.offset[1]), (int) (_drag[2] + _state.offset[0]), (int) (_drag[3] + _state.offset[1])};
		
		if (_drag[0] > _drag[2]) {
			int d = _drag[0];
			_drag[0] = _drag[2];
			_drag[2] = d;
		}
		if (_drag[1] > _drag[3])
			_human.areaClick(_drag[0], _drag[3], _drag[2] - _drag[0], _drag[1] - _drag[3]);
		else
			_human.areaClick(_drag[0], _drag[1], _drag[2] - _drag[0], _drag[3] - _drag[1]);
		
		_drag = null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		// Update the time manager
		getTimeManager().render(g, new Pair((int)_state.offset[0], (int)_state.offset[1]));
		
		// Draw box
		if (_drag != null) {
			g.setColor(Color.white);
			g.drawRect(_drag[0], _drag[1], _drag[2] - _drag[0], _drag[3] - _drag[1]);
		}	
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		// Check music
		if (!_game.mute && !_game.musicLoop.playing() && !_game.music.playing()) {
			_game.musicLoop.loop(1, 0.5f);
		}
		
		// Update the time manager
		getTimeManager().update(container.getTime(), delta);
		
		// Check for keys
		checkKeys(delta);
	}
	
	public void updateManagers(long currentFrame) {
		for (String key: _managers.keySet()) {
			if (!key.equals("time")) _managers.get(key).update(currentFrame);
		}
	}
	
	public void checkKeys(int delta) {
		
		// Vertical scroll keys
		if (_keys.contains(200)) {
			
			_state.offset[1] = (float)Math.max(0, _state.offset[1] - 0.5 * delta);
			
		} else if(_keys.contains(208)) {
			
			_state.offset[1] = (float)Math.min(_map.getSize().y() * 32 - 600, _state.offset[1] + 0.5 * delta);
		}
		// Horizontal scroll keys
		if (_keys.contains(205)) {
			
			_state.offset[0] = (float)Math.min(_map.getSize().x() * 32 - 800, _state.offset[0] + 0.5 * delta);
			
		} else if (_keys.contains(203)) {
			
			_state.offset[0] = (float)Math.max(0, _state.offset[0] - 0.5 * delta);
		}
			
	}

	public void toggleMusic() {
		_game.mute = !_game.mute;
		
		if (_game.mute) {
			_game.musicLoop.stop();
			_game.music.stop();
		} else
			_game.musicLoop.loop(1, 0.5f);
	}
	
	public Map getMap() {
		return _map;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		
		// Initialize the time manager
		getTimeManager().start(container.getTime());
		
		// Fade in
		getUIManager().fadeIn();
		
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {}

	@Override
	public State stateSave() {
		LevelState state = (LevelState) _state.Apply();
		
		// Add the managers to the state
		for (Manager manager: _managers.values())
			state.applyChild(manager);
			
		return state;
	}

	@Override
	public void stateRestore(State state) {
		_state = (LevelState) state;
		
		for (String key: state.getChildren())
			_managers.get(key).stateRestore(state.getChild(key));
	}
	
	@Override
	public String getName() {
		return "level";
	}
}


class LevelState extends State {
	
	/**
	 * The current offset of the map
	 */
	protected float[] offset = new float[2];
	
	public LevelState clone() throws CloneNotSupportedException {
		
		LevelState state = (LevelState) super.clone();
		
		state.offset = offset.clone();
		
		return state;
	}
	
}
