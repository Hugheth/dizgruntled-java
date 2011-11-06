package com.hugheth.dizgruntled.tools;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import com.hugheth.dizgruntled.level.TilePacker;

public class TMXConverter extends BasicGameState {

	private String _source;
	private String _dest;
	
	public TMXConverter(String source, String dest) {
		_source = source;
		_dest = dest;
	}
	
	public static void ConvertTMXFile(String source, String dest) {
		try {
			
			System.out.println("Printing packed tiles");
			
			TiledMap map = new TiledMap(source, "");
			
			// Iterate through layers
			for (int ly = 0; ly < map.getLayerCount(); ly++) {
				// Tiles
				String tiles = "";

				// Iterate through tiles
				for (int j = 0; j < map.getWidth(); j++) {
					for (int i = 0; i < map.getHeight(); i++) {
	
						// Get the tile id
						int id = map.getTileId(i, j, ly);
									
						/*
						 if (ly == 1 && id > 0)
							System.out.println(id);
						*/
						String hex;
						
						// Check for zero
						if (id > 0) {
							// Convert the hex string
							if (ly > 0)
								hex = Integer.toHexString(id - 144);
							else
								hex = Integer.toHexString(id);
						} else {
							hex = "00";
						}
						// Pad with zero if too short
						if (hex.length() == 1) hex = "0" + hex;
						
						// Add to the tile string
						tiles += ly + hex;
					}				
				}
				// Print
				
				System.out.println(TilePacker.Pack(tiles));
			}
			
		} catch (SlickException e) {
			System.out.println("An error occurred trying to convert the TMX file: " + e.getMessage());
		}
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		ConvertTMXFile(_source, _dest);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {}

	@Override
	public int getID() {
		return 0;
	}
}
