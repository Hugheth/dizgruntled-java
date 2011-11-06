package com.hugheth.dizgruntled.level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.json.JSONObject;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.ResourceLoader;

/**
 * The TileBehaviour class is an enumeration of different tile types with their associated traits
 * and triggers
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.level.TileSet
 */
public class TileBehaviour {
	
	private static HashMap<String, TreeSet<String>> _traits;
	
	private static HashMap<String, String> _toggle;
	
	// Setup
	public static void setup() throws GruntException {
		try {
			// Create the caches
			_traits = new HashMap<String, TreeSet<String>>();
			_toggle = new HashMap<String, String>();
			
			// Get the traits
			JSONObject traits = ResourceLoader.getJSONForPath("tilez/traits");
			// Get the triggers
			JSONObject triggers = ResourceLoader.getJSONForPath("tilez/toggle");
			
			
			// Parse traits into a more efficient data structure
			@SuppressWarnings("unchecked")
			Iterator<String> it = traits.keys();
			// Iterate through the tile types
			while (it.hasNext()) {
				
				String key = it.next();
				
				// Get traits
				String[] myTraits = traits.getString(key).split(" ");
				// Check array
				_traits.put(key, new TreeSet<String>(Arrays.asList(myTraits)));
				
			}
			@SuppressWarnings("unchecked")
			Iterator<String> it2 = triggers.keys();
			// Parse triggers into a more efficient data structure
			while (it2.hasNext()) {
				String key = it2.next();
				// Check array
				_toggle.put(key, triggers.getString(key));
			}
			
		} catch (Exception e) {
			throw new GruntException("Couldn't parse the tile behaviour configuration files", e);
		}
	}
	// Get traits
	public static TreeSet<String> getTraits(String tileType) {
		if (_traits.containsKey(tileType)) {
			TreeSet<String> set = new TreeSet<String>();
			set.addAll(_traits.get(tileType));
		 	return set;
		} else {
			return new TreeSet<String>();
		}
	}
	
	public static String getToggle(String tileType) {
		if (_toggle.containsKey(tileType))
			return _toggle.get(tileType);
		else
			return null;
	}
}
