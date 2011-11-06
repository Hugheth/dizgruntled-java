package com.hugheth.dizgruntled.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.hugheth.dizgruntled.Pair;

public class PathAgent {
	
	protected PathArbiter _arbiter;
	protected ArrayList<int[]> route;
	protected Pair _end;
	protected Pair _xy;
	private ContourMap _map;
	
	public PathAgent(PathArbiter aribter, ContourMap map) {
		_arbiter = aribter;
		_map = map;
	}
	
	public void init(Pair start) {
		_xy = start;
		_map.expandAll(start);
	}
	
	public Pair next() {
		PriorityQueue<TileMarker> moves = _map.getUsefulMovesAt(_xy);
			
		while (!moves.isEmpty()) {
			// Get the best move
			TileMarker move = moves.poll();
			
			_xy = move.getPair();
			
			if (_map.getPolicy().getEnterableAt(_xy))
				return _xy.clone();
		}
		
		// Delink from the map
		_arbiter.delinkMap(_map.getName());
		
		return null;
	}
}
