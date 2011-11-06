package com.hugheth.dizgruntled;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import com.hugheth.dizgruntled.component.OffsetAnimation;


/**
 * The Resource Manager is responsible for loading all local and remote data needed for the game   
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.level.ResourceException
 */
public class ResourceLoader {
	
	/**
	 * The cache stores JSONObjects of all the json files that have been loaded into
	 * the manager, with the resource path name as its key
	 */
	private static TreeMap<String, JSONObject> _cache;
	
	private static TreeMap<String, JSONObject> _graphicsConfig;

	/**
	 *  Setup the resource manager
	 */
	public static void setup() {
		_cache = new TreeMap<String, JSONObject>();
	}
	
	public static void loadGraphicsConfig() throws GruntException {
		
		_graphicsConfig = new TreeMap<String, JSONObject>();
		
		String[] configs = getJSONDirectory("graphicz/config");
		// Load the graphics config
		for (String i: configs) {
			try {
				JSONObject config = new JSONObject(new JSONTokener(new FileReader(i)));
		
				@SuppressWarnings("unchecked")
				Iterator<String> it = config.keys();
				// Iterate through rules
				while (it.hasNext()) {
					String key = it.next();
					// Add rule
					_graphicsConfig.put(key, config.getJSONObject(key));
				}
			} catch (Exception e) {
				throw new GruntException("Couldn't load the graphics configuration successfully for file [" + i + "]", e);
			}
		}
	}
		
	public static void preload() {
		// Preload graphics
		// TODO
	}
	
	public static JSONObject getGraphicsConfigForPath(String path) {
		return _graphicsConfig.get(path);
	}
	
	public static Animation getAnimationForPath(String path) {
		// Get the config
		JSONObject config = getGraphicsConfigForPath(path);

		// Get rate
		int rate = 75;
		try {
			rate = (int) config.getLong("rate");
		} catch (Exception e) {}
		// Get the frame dimensions
		int width = 32;
		int height = 32;
		try {
			width = (int) config.getLong("width");
		} catch (Exception e) {}
		try {
			height = (int) config.getLong("height");
		} catch (Exception e) {}
		// Load the tilesheet
		SpriteSheet sheet;
		try {
			sheet = new SpriteSheet("graphicz/" + path + ".png", width, height);

			// Create the animation
			Animation result = new OffsetAnimation(sheet, rate, config.optInt("offsetX"), config.optInt("offsetY"));
			// End
			int end = result.getFrameCount();
			// Get frame count
			try {
				end = (int) config.getLong("end");
			} catch (Exception e) {}
			// Stop early
			result.stopAt(end);
			// Durations
			try {
				JSONArray dur = config.getJSONArray("durations");
				for (int i = 0; i < end; i++) {
					result.setDuration(i, (int) dur.getLong(i));
				}
			} catch (Exception e) {}
			// Looping
			try {
				result.setLooping(config.getBoolean("loop"));
			} catch (Exception e) {}
			// Bounce
			try {
				result.setPingPong(config.getBoolean("bounce"));
			} catch (Exception e) {}

			return result;
		} catch (Exception e) {
			System.out.println("IGNORE: Failed to load the animation for [" + path + "]");
			return null;
		}
	}
	
	public static JSONObject getJSONForPath(String path) throws GruntException {
		
		// Check to see if a cached copy exists
		if (!_cache.containsKey("path")) {
			
			try {
				
				// Load the file into cache	
				_cache.put(path, new JSONObject(new JSONTokener(new FileReader(path + ".json"))));
				
			} catch (JSONException e) {
				throw new GruntException("A JSON error occured trying to read the JSON for the path [" + path + "]", e);
			} catch (Exception e) {
				throw new GruntException("An I/O error occured trying to read the JSON for the path [" + path + "]", e);
			}
		}
		// Return the JSON
		return _cache.get(path);
	}
	
	public static String[] getJSONDirectory(String directory) {
		// Get files in the directory
		File[] configs = new File(directory).listFiles(new FileListFilter("json"));
		String[] result = new String[configs.length];
		try {
			for (int i = 0; i < configs.length; i++) {
				result[i] = configs[i].getPath();
			}
		} catch (Exception e) {}
		return result;
	}
}


class FileListFilter implements FilenameFilter {
	private String start = null;
	private String end; 

	public FileListFilter(String endsWith) {
		end = endsWith;
	}
	public FileListFilter(String startsWith, String endsWith) {
		start = startsWith;
		end = endsWith;
	}
	public boolean accept(File directory, String filename) {
		boolean fileOK = true;
		if (start != null)
			fileOK &= filename.startsWith(start);
		if (end != null)
			fileOK &= filename.endsWith(end);
		return fileOK;
	}
}
