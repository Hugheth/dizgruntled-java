package com.hugheth.dizgruntled.ai;

import java.util.TreeMap;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.player.Player;

public class PathArbiter {
	
	private Player _player;
	// Policies
	private TreeMap<String, TilePolicy> _policies;
	// Maps
	private TreeMap<String, ContourMap> _maps;
	
	// Current paths
	//private ArrayList<PathAgent> _paths;

	public PathArbiter(Player player) {
		_player = player;
		
		_policies = new TreeMap<String, TilePolicy>();
		_maps = new TreeMap<String, ContourMap>();
	}

	// Create a path
	public PathAgent requestAgent(Pair start, Pair end, TilePolicy policy) {
		
		// Check whether the tile policy exists
		if (_policies.containsKey(policy.getName()))
			policy = _policies.get(policy.getName());
		else
			_policies.put(policy.getName(), policy);

		String mapName = end + " " + policy.getName();
		
		ContourMap map;
		
		if (_maps.containsKey(mapName))
			map = _maps.get(mapName);
		else {
			map = new ContourMap(_player);
			map.init(policy, end);
			map.link(1);
			_maps.put(mapName, map);
		}
		
		PathAgent p = new PathAgent(this, map);
		p.init(start);

		return p;
	}
	
	public void delinkMap(String mapName) {
		if (_maps.containsKey(mapName)) {
			
			// Unlink the agent from the map and remove if no more agents are using the map
			if (!_maps.get(mapName).link(-1))
				_maps.remove(mapName);
		}
	}
	
	// Get the level
	public Player getPlayer() {
		return _player;
	}
	
	
}
