package com.hugheth.dizgruntled.manager;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.level.TileBehaviour;
import com.hugheth.dizgruntled.level.TilePacker;
import com.hugheth.dizgruntled.level.TileSet;
import com.hugheth.dizgruntled.logic.Logic;
import com.hugheth.dizgruntled.logic.Switch;
import com.hugheth.dizgruntled.logic.TileTrigger;
import com.hugheth.dizgruntled.player.HumanPlayer;
import com.hugheth.dizgruntled.player.Player;


/**
 * A map is a full or partial representation of a particular state of a level e.g. the starting state or a save state
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 *
 * @see com.hugheth.dizgruntled.level.ResourceException
 * @see com.hugheth.dizgruntled.level.Level
 */
public class Map extends Manager {
	/**
	 * The last game version whose maps are compatible with this version
	 */
	public static final String VERSION_LIMIT = "alpha-2";
	
	private MapState _state;
	
	/**
	 * Level name
	 */
	private String _name;
	//private Pair _start;
	private TileSet[] _tilesets;
	private JSONArray _rooms;
	private HashMap<String, TreeSet<String>> _traits;
	
	// Current room
	private Pair _size;
	private int[][][][] _layers;
	private ArrayList<Logic> _logics;
	
	// Fog
	private boolean _useFog = false;
	private Image _fogImage;
	
	// Errors occurred in rendering
	private boolean _renderErrorsOccurred = false;
	
	/**
	 * Create a new Map from JSON
	 * 
	 * @param mapData The JSONObject that represents the map to create
	 */
	public Map(Level level) {
		super(level);
		
		_state = new MapState();
	}
	
	public void load (JSONObject mapData) throws GruntException {
		try {
			// Check that the version of the map is correct
			if (!mapData.getString("version").equals(VERSION_LIMIT)) throw new GruntException("This level was made with an incompatible version of the game. Open in an editor to fix.", null);
			
			// Set name
			_name = mapData.getString("name");
			
			// Set start position
			//String[] xy = mapData.getString("start").split(" ");
			//_start = new Pair {Integer.parseInt(xy[0]), Integer.parseInt(xy[1])};
			
			// Get size
			//String[] size = mapData.getString("size").split(" ");
			//_size = new Pair {Integer.parseInt(size[0]), Integer.parseInt(size[1])}; 
			
			// Check for fog
			_useFog = mapData.getBoolean("fog");
			
			// Get tilesets
			JSONArray tileSets = mapData.getJSONArray("tilesetz");
			// Create tilesets array
			_tilesets = new TileSet[tileSets.length()];
			// Iterate through the tilesets
			for (int i = 0; i < tileSets.length(); i++) {
				
				String name = tileSets.getString(i);
				// Add to the tileset array
				_tilesets[i] = new TileSet(ResourceLoader.getJSONForPath("tilez/" + name));
				
			}
			
			try {
				// Get players
				JSONArray players = mapData.getJSONArray("playerz");
				// Create players array
				Player[] _players = new Player[players.length()];
				// Iterate through players
				for (int i = 0; i < players.length(); i++) {
					
					// Get player properties
					//String name = players.getJSONObject(i).getString("name");
					//int max = (int) players.getJSONObject(i).optLong("max", 20);
					//String tint = players.getJSONObject(i).getString("tint");
					String type = players.getJSONObject(i).getString("type");
										
					// Switch type
					if (type.equals("human")) {
						// Create the player
						_players[i] = new HumanPlayer(_level, _level.getInput());
					}
				}
				// Set level players
				_level.setPlayers(_players);
				
			} catch (Exception e) {
				throw new GruntException("An error occured while creating the players. Open in an editor to fix.", e);
			}
			
			// Get rooms
			_rooms = mapData.getJSONArray("roomz");
			
			// Select the home room
			setCurrentRoom("home");
			
		} catch (Exception e) {
			// Throw a map error
			throw new GruntException("An error occured while creating the map. Open in an editor to fix.", e);
		}	
	}
	
	public void setCurrentRoom(String name) throws GruntException {
		try {
			// Iterate through the rooms to find the room
			for (int i = 0; i < _rooms.length(); i++) {
				// Check for home room
				if (_rooms.getJSONObject(i).getString("name").equals(name)) {
					setCurrentRoom(i);
					break;
				}
			}			
		} catch (Exception e) {
			throw new GruntException("An error occured while setting the room [" + name + "]. Open in an editor to fix.", e);
		}
	}
	
	public void setCurrentRoom(int index) throws GruntException {
		try {
			
			// Get the current room
			JSONObject room = _rooms.getJSONObject(index);
			
			// Get the room size
			_size = Pair.parseString(room.getString("size"));
			
			if (_useFog) {
				_fogImage = new Image("tilez/fog.png");
				_state.fogCache = new int[_size.x()][_size.y()];
			}
			
			JSONArray roomLayers = room.getJSONArray("layerz");
			
			// Create layer object
			_layers = new int[roomLayers.length()][][][];
			
			// Create the traits cache array
			_traits = new HashMap<String, TreeSet<String>>();
			
			// Create a blank logic array
			_logics = new ArrayList<Logic>();
			
			// Iterate through the tile layers
			for (int i = 0; i < roomLayers.length(); i++) {
				
				JSONObject layer = roomLayers.getJSONObject(i);
								
				// Get dimensions
				int width = _size.x();
				int height = _size.y();
				int area = width * height;
				// Set the layer
				_layers[i] = new int[width][height][3];
				
				// Get the tile string
				String tiles = layer.getString("tilez");
				
				// Inflate the tile string
				tiles = TilePacker.Unpack(tiles, area);
									
				// Incrementor for convenience
				int n = 0;
				// Iterate through tiles
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						
						Pair pair = new Pair(x, y);						
						
						int[] tile = _layers[i][x][y];
						
						// Populate the layer array
						tile[0] = Integer.parseInt(tiles.substring(n, n + 1), 16);
						tile[1] = Integer.parseInt(tiles.substring(n + 1, n + 3), 16);
						
						try {
							
							// Get traits
							if (_traits.containsKey(pair.toString()))
								_traits.get(pair.toString()).addAll(TileBehaviour.getTraits(_tilesets[tile[0]].getType(tile[1])));
							else
								_traits.put(pair.toString(), TileBehaviour.getTraits(_tilesets[tile[0]].getType(tile[1])));
							
							try {
								
								AbstractSet<String> traits = getTileTraits(pair);
								
								// Check for invisible tiles
								if (traits.contains("hide")) {
									tile[1] = 0;
									
								// Check for red pyramidz and red switches
								} else if (traits.contains("redSwitch")) {
									// Construct the property list
									TreeMap<String, String> props = new TreeMap<String, String>();
									props.put("group", "red");
									// Create the switch logic and initialize
									Switch sw = new Switch(_level);
									sw.init(pair.multiply(32), props);
									_logics.add(sw);
									
								} else if (traits.contains("redPyramid")) {
									// Construct the property list
									TreeMap<String, String> props = new TreeMap<String, String>();
									props.put("group", "red");
									// Create the switch logic and initialize
									TileTrigger trg = new TileTrigger(_level);
									trg.init(pair.multiply(32), props);
									_logics.add(trg);
									
								} else if (traits.contains("hole"))
									markStatefulTile(0, pair);
									
							} catch (Exception e) {
								System.out.println("IGNORE: Some red switchez and triggerz were not successfully created.");
							}
							
							// Increment
							n += 3;
						
						} catch (Exception e) {
							System.out.println("IGNORE: An error occured while trying to parse the tiles of the map. Open in an editor to fix.");
						}
					}
				}				
			}
			
			// Get logicz
			JSONArray logics = room.getJSONArray("logicz");

			// Iterate through logicz
			for (int i = 0; i < logics.length(); i++) {
				
				JSONObject logic = logics.getJSONObject(i);
				// Create the logic object
				Logic newLogic = Logic.fromType(_level, logic.getString("type"));
				
				// Get position
				Pair xy = Pair.parseString(logic.getString("xy"));
				
				// Iterate through properties
				TreeMap<String, String> props = new TreeMap<String, String>();
				// Iterator
				@SuppressWarnings("unchecked")
				Iterator<String> it = logic.keys();
				// Iterate through logicz
				while (it.hasNext()) {
					String key = it.next();
					
					// Check for name or position
					if (!key.equals("type") && !key.equals("xy")) {
						// Add property
						props.put(key, logic.getString(key));
					}
				}

				try {
					
					// Initialize with information					
					newLogic.init(xy, props);
					
					// Add to the list
					_logics.add(newLogic);
					
				} catch(Exception e) {
					System.out.println("IGNORE: An error occurred initializing the object [" + logic.getString("type") + "] at (" + logic.getString("xy") + ")");
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("IGNORE: The room specified doesn't exist or contains errors.");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Render layers
	 * 
	 * @param g The Graphics context to draw the tiles onto
	 * @param offsetX The x offset of the stage from the window
	 * @param offsetY The y offset of the stage from the window
	 * @param width The width of the stage in tiles
	 * @param height The height of the stage in tiles
	 */
	public void renderLayers(Graphics g, Pair offset, Pair size) {
		
		// No change
		_state.tileChanged = false;
		
		// Edge tiles
		int left = offset.x() / 32;
		int top = offset.y() / 32;
		
		// Fit to size
		size = size.add(Math.min(0, _size.x() - (left + size.x())), Math.min(0, _size.y() - (top + size.y())));
		
		// Errors
		int errors = 0;
		// Render the layer
		for (int[][][] layer: _layers) {
			for (int i = 0; i < size.x(); i++) {
				for (int j = 0; j < size.y(); j++) {
					try {

						// Get the current tile
						int[] tile = layer[i + left][j + top];
	
						// Get the image to draw
						if (tile[1] > 0) {

							// Get the current tileset
							TileSet tileset = _tilesets[tile[0]];
							
							Image image = tileset.getImage(tile[1] - 1);
							
							// Draw the image
							g.drawImage(image, (i + left) * tileset.getSize().x(), (j + top) * tileset.getSize().y());
						}
						
					} catch (Exception e) {
						errors++;
					}
				}
			}
		}
				
		if (!_renderErrorsOccurred && errors > 0) {
			// Flag as occurred
			_renderErrorsOccurred = true;
			// Tell
			System.out.println("IGNORE: At least [" + errors + "] errors have occured while rendering tiles. Open in an editor to fix.");
		}
	}
	
	public void renderFog(Graphics g, Pair offset, Pair size) {
		if (!_useFog) return;
		
		// Edge tiles
		int left = offset.x() / 32;
		int top = offset.y() / 32;
		
		// Fit to size
		size = size.add(Math.min(0, _size.x() - (left + size.x())), Math.min(0, _size.y() - (top + size.y())));
		
		// Render the fog
		for (int i = 0; i < size.x(); i++) {
			for (int j = 0; j < size.y(); j++) {
				
				// Get local fog values
				int value = 0;
				
				if (_state.fogCache[i + left][j + top] > 0) value = value | 8;
				
				if (i + 1 < size.x() && _state.fogCache[i + 1 + left][j + top] > 0) value = value | 1;
				
				if (i + 1 < size.x() && j + 1 < size.y() && _state.fogCache[i + 1 + left][j + 1 + top] > 0) value = value | 2;
				
				if (j + 1 < size.y() && _state.fogCache[i + left][j + 1 + top] > 0) value = value | 4;
			
				if (value < 15) {
					
					Image fog = _fogImage.getSubImage(value * 32, 0, 32, 32);
					
					// Draw image
					g.drawImage(fog, (i + left) * 32, (j + top) * 32);
				}
			}
		}
	}
	
	public boolean fogAt(Pair xy) {
		if (!_useFog) return false;
		
		int val = 0;
		
		if (xy.y() + 1 > _size.y() || xy.x() + 1 > _size.x() || xy.y() < 0 || xy.x() < 0) return true;
		
		if (_state.fogCache[xy.x()][xy.y()] > 0) val++;		
		if (xy.x() + 1 < _size.x() && _state.fogCache[xy.x() + 1][xy.y()] > 0) val++;		
		if (xy.x() + 1 < _size.x() && xy.y() + 1 < _size.y() && _state.fogCache[xy.x() + 1][xy.y() + 1] > 0) val++;
		if (xy.y() + 1 < _size.y() && _state.fogCache[xy.x()][xy.y() + 1] > 0) val++;
		
		if (val == 0) return true;
		
		return false;		
	}
	
	public void addWind(Pair xy, Pair size) {
		
		if (!_useFog) return; 
		
		for (int i = 0; i < size.x(); i++)
			for (int j = 0; j < size.y(); j++)
				_state.fogCache[xy.x() + i][xy.y() + j]++;
		
		_state.tileChanged = true;
	}
	
	public void removeWind(Pair xy, Pair size) {
		
		if (!_useFog) return;
		
		for (int i = 0; i < size.x(); i++)
			for (int j = 0; j < size.y(); j++)
				_state.fogCache[xy.x() + i][xy.y() + j]--;
		
		_state.tileChanged = true;
	}

	public Pair getSize() {
		return _size;
	}
	public String getLevelName() {
		return _name;
	}
	public ArrayList<Logic> getLogics() {
		return _logics;
	}
	public int getTileID(int layer, Pair xy) {
		return _layers[layer][xy.x()][xy.y()][1];
	}
	public String getTileType(int layer, Pair xy) {
		return _tilesets[_layers[layer][xy.x()][xy.y()][0]].getType(_layers[layer][xy.x()][xy.y()][1]);
	}
	public void setTileType(String type, int layer, Pair xy) {
		
		int set = _layers[layer][xy.x()][xy.y()][0];
		int id = _tilesets[set].getIDFromType(type);
		
		// Update the tile
		_layers[layer][xy.x()][xy.y()][1] = id;
		
		_state.tileChanged = true;
		
		updateTileTraits(xy);
		
		// Update in state
		_state.tileDelta.put(xy + ":" + layer, set + " " + id);
	}
	protected void updateTileTraits(Pair xy) {
		
		// Recalculate traits
		_traits.get(xy.toString()).clear();
		
		// Iterate through layers
		for (int ly = 0; ly < _layers.length; ly++)
			_traits.get(xy.toString()).addAll(TileBehaviour.getTraits(getTileType(ly, xy)));
		
	}
	
	public boolean tileToggle(int layer, Pair xy) {
		// Get trigger
		String trigger = TileBehaviour.getToggle(getTileType(layer, xy));

		// Check for real
		if (trigger == null) return false;
		
		// Split
		String[] parts = trigger.split(" ");
		
		// Switch tile
		setTileType(parts[0], layer, xy);
		
		// Check for sfx
		if (parts.length > 1)
			_level.getLogicManager().createSFX(xy.multiply(32), parts[1]);
		
		// Check for sound
		if (parts.length > 2)
			_level.playSoundAt(parts[2], xy, 40);
			
		_state.tileChanged = true;
		
		return true;
	}
	
	public void markStatefulTile(int layer, Pair xy) {
		
		// Get type and tileset
		int set = _layers[layer][xy.x()][xy.y()][0];
		int id = _layers[layer][xy.x()][xy.y()][1];
		
		_state.tileDelta.put(xy + ":" + layer, set + " " + id); 
	}
	
	public boolean tilesChanged() {
		return _state.tileChanged;
	}
	
	public AbstractSet<String> getTileTraits(Pair xy) {
		
		if (_traits.containsKey(xy.toString()))
			return _traits.get(xy.toString());
		else
			return new TreeSet<String>();
	}
	@Override
	public State stateSave() {
		return _state.Apply();
	}
	@Override
	public void stateRestore(State state) {
		
		_state = (MapState) state;
		
		// Update tiles
		for (String key: _state.tileDelta.keySet()) {
			
			// Get the position and layer
			String[] str = key.split(":");
			
			Pair xy = Pair.parseString(str[0]);
			int layer = Integer.parseInt(str[1]);
			
			// Get the tileset and tile type
			str = _state.tileDelta.get(key).split(" ");
			
			_layers[layer][xy.x()][xy.y()][0] = Integer.parseInt(str[0]);
			_layers[layer][xy.x()][xy.y()][1] = Integer.parseInt(str[1]);
			
			// Update the tile traits
			updateTileTraits(xy);
			
		}
	}
	@Override
	public String getName() {
		return "map";
	}
}

class MapState extends State {
	
	public HashMap<String, String> tileDelta = new HashMap<String, String>();
	
	public boolean tileChanged = false;
	
	public int[][] fogCache = new int[1][1];
		
	public MapState clone() throws CloneNotSupportedException {
		
		MapState state = (MapState) super.clone();
		
		// Copy information
		state.fogCache = new int[fogCache.length][fogCache[0].length];

		for (int i = 0; i < fogCache.length; i++)
				state.fogCache[i] = fogCache[i].clone();
		
		state.tileDelta = new HashMap<String, String>();
		for (String key: tileDelta.keySet())
			state.tileDelta.put(key, tileDelta.get(key));
		
		return state;
	}
	
}
