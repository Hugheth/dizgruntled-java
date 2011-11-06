package com.hugheth.dizgruntled.level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;


/**
 * A tileset is a collection of sprite images and behaviours that represent tiles in the game
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.ResourceLoader
 * @see com.hugheth.dizgruntled.level.ResourceException
 */
public class TileSet {
	
	/**
	 * Tileset name
	 */
	private String _name;
	private Pair _size;
	private String[] _types; 
	
	/**
	 * Tileset image
	 */
	private SpriteSheet _image;
	
	/**
	 * Creates a new Tileset from JSON
	 * 
	 * @param tilesetData The JSONObject that represents the tileset to create
	 * @throws ResourceException 
	 */	
	public TileSet(JSONObject tilesetData) throws GruntException {
		try {
			// Load properties
			_name = tilesetData.getString("name");

			String[] size = tilesetData.getString("size").split(" ");
			_size = new Pair(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
			
			// Load tile types
			JSONArray types = tilesetData.getJSONArray("types");
			_types = new String[types.length()];
			// Iterate through types and add to array
			for (int i = 0; i < types.length(); i++) {
				_types[i] = types.getString(i);
			}
			
			try {
				// Load image
				_image = new SpriteSheet("tilez/" + _name + ".png", _size.x(), _size.y());
				
			} catch (SlickException e) {
				throw new GruntException("An error occurred while trying to load the media, [" + _name + "]", e);
			}
			
		} catch (JSONException e) {
			throw new GruntException("A JSON error occurred while trying to load the media, [" + _name + "]", e);
		}
	}
	
	/**
	 * Get an image of a tile from the TileSet
	 * 
	 * @param tileID The ID number of the tile to retrieve
	 * @return The image requested
	 */
	public Image getImage(int tileID) {
		return _image.getSprite(tileID % _image.getHorizontalCount(), tileID / _image.getHorizontalCount());
	}
	
	public String getType(int tileID) {
		try {
			return _types[tileID - 1];
		} catch (Exception e) {
			return "";
		}
	}
	public int getIDFromType(String type) {
		// Iterate through the set to find a suitable tile
		for (int i = 0; i < _types.length; i++) {
			if (_types[i].equals(type))
				return (i + 1);
		}
		return 0;
	}
	
	public Pair getSize() {
		return _size;
	}
}
