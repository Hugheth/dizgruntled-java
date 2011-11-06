package com.hugheth.dizgruntled;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.tools.TMXConverter;
import com.hugheth.dizgruntled.ui.EditorFrame;
import com.hugheth.dizgruntled.ui.Home;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class GruntzGame extends StateBasedGame {

	private static Home _home;
	
	/**
	 * Singleton Level
	 */
	private static Level _level;
	
	/**
	 * The games main method that is run when the game begins
	 * 
	 * @param args Optional arguments to pass dizGruntled
	 */
	public static void main(String[] args) {
		
		try {
			
			System.out.println("*************************");
			System.out.println("* dizGruntled Alpha 2.0 *");
			System.out.println("*************************");
			
			name = JOptionPane.showInputDialog(null, "Please enter your name if you would like your score to be uploaded to the Hall Of Fame", "dizGruntled Alpha 2.0", 1);
			
			int fullscreen = JOptionPane.showConfirmDialog(null, "Would you like to play the game fullscreen?", "dizGruntled Alpha 2.0", 0, 3);

			System.out.println("Just a sec!");
			
			// Make UI look like the system
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			// Start the game
			GruntzGame game = new GruntzGame();
						
			// Create the game window
			AppGameContainer app = new AppGameContainer(game);
			app.setShowFPS(false);
			
			if (fullscreen == 1)
				app.setDisplayMode(800, 600, false);
			else
				app.setDisplayMode(800, 600, true);
			
			app.setVSync(true);
			app.setAlwaysRender(true);
			
			// Check for tools
			if (args.length > 0) {
				if (args[0].equals("convert")) {
					
					// Convert a TMX level to a JSON map
					TMXConverter converter = new TMXConverter(args[1], "");
					game.addState(converter);
					
					app.start();
					
				} else if (args[0].equals("editor")) {
					
					// Start the level editor
					EditorFrame editor = new EditorFrame();
					editor.setVisible(true);
					
					// Create the Level Manager
					_level = new Level();
					// Add the level manager as a game state
					game.addState(_level);
					
					app.start();
				}
			} else {

				// Create Home
				_home = new Home();
								
				// Create the Level
				_level = new Level();
				
				// Add the states to the game
				game.addState(_home);
				game.addState(_level);
				
				app.start();

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Music Track
	 */
	public Sound music;
	public Sound musicLoop;
	public boolean mute = false;
	
	public static boolean legit = true;
	public static String name;
	
	/**
	 * Create a new StateBasedGame
	 * @throws GruntException 
	 */
	public GruntzGame() throws GruntException {
		// Call super
		super("Dizgruntled");
		
		Log.setVerbose(false);
		
		ResourceLoader.setup();
		ResourceLoader.loadGraphicsConfig();
		
		// Play music
		try {
			music = new Sound("soundz/MusicEntry.ogg");
			musicLoop = new Sound("soundz/MusicLoop.ogg");
		
		} catch (SlickException e) {
			System.out.println("ABORT: Couldn't find music files");
		}
		
		// Show another frame
		//NetFrame netFrame = new NetFrame();
		//netFrame.setVisible(true);
		// Try kryonet
		/*
		final Server s = new Server();
		try {
			Kryo k = s.getKryo();
			k.register(SomeRequest.class);
			// Listen
			s.addListener(new Listener() {
				public void connected (Connection connection) {
					connection.sendTCP(new SomeRequest("meep"));
				}
				public void received (Connection connection, Object object) {
					if (object instanceof SomeRequest) {
						SomeRequest request = (SomeRequest)object;
						System.out.println(request.text);
						SomeRequest response = new SomeRequest("Hello");
						connection.sendTCP(response);
					}
				}
				public void disconnected (Connection c) {
					System.out.println("Connection ended");
				}
			});
			s.bind(6556, 6556);
			s.start();
		} catch (IOException e) {
			// Client instead
			final Client c = new Client();
			c.start();
			Kryo k2 = c.getKryo();
			k2.register(SomeRequest.class);
			final InetAddress addr = c.discoverHost(6556, 6556);
			try {
				c.connect(6556, addr, 6556, 6556);
				// Send message
				c.addListener(new Listener() {
					public void connected (Connection connection) {
						System.out.println("Connected to: " + addr.getHostName());
						// Send hi
						//c.sendTCP(new SomeRequest("meep"));
					}
					public void received (Connection connection, Object object) {
						System.out.println("HI");
						if (object instanceof SomeRequest)
							System.out.println(((SomeRequest) object).text);
					}
					public void disconnected (Connection c) {
						System.out.println("Connection ended");
					}
				});
			} catch (IOException e1) {
				System.out.println("Couldn't connect to server.");
			}
		}
		*/
	}
	
	public Home getHome() {
		return _home;
	}
	
	public void startLevel() {
		enterState(1);
	}
	
	public void finishLevel() {
		enterState(0);
	}

	@Override
	public void initStatesList(GameContainer container) {}
}
