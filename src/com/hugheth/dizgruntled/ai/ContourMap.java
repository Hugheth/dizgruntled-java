package com.hugheth.dizgruntled.ai;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.player.Player;

/**
 * The Contour Map is used when pathfinding to allow multiple gruntz
 * to easily find their way to their destination without having to
 * perform continuous pathfinding routines.
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 */
public class ContourMap {
	
	protected Player _player;
	protected TilePolicy _policy;
	protected HashMap<String, TileMarker> _map;
	protected PriorityQueue<TileMarker> _queue;
	protected int _links = 0;
	
	protected TileComparator _comparator;
	
	protected Pair _target;
	
	public ContourMap(Player player) {
		_player = player;
	}
	
	public void init(TilePolicy policy, Pair target) {

		_policy = policy;
		_target = target;
				
		// Create the comparator
		_comparator = new ManhattanComparator();
		
		reset();
		
	}
	
	protected void reset() {
		// Reset the map
		_map = new HashMap<String, TileMarker>();
		// Reset the queue
		_queue = new PriorityQueue<TileMarker>(20, _comparator);
		// Add the target tile to the queue
		_queue.add(new TileMarker(_target, 0, 0));
	}
	
	/**
	 * Returns the list of moves from a current tile that are in the
	 * grunt's interest to make. The moves are returned in order of
	 * most favourable
	 * 
	 * @param xy The tile to find moves that take the grunt closer to the target
	 * @return The list of moves
	 */
	public PriorityQueue<TileMarker> getUsefulMovesAt(Pair xy) {
		
		// Ensure the tile is in the map
		if (!expand(xy)) return new PriorityQueue<TileMarker>();
		
		// Get the weight at the current tile
		int weight = _map.get(xy.toString()).getWeight();
		
		PriorityQueue<TileMarker> moves = new PriorityQueue<TileMarker>(8, new TrueComparator());
		
		// Add adjacent tiles to the priority queue
		for (TileMarker newTile: getAdjacentTiles(_map.get(xy.toString())))
			addUsefulTile(moves, newTile.getPair(), weight);
				
		return moves;
	}
	
	private PriorityQueue<TileMarker> addUsefulTile(PriorityQueue<TileMarker> queue, Pair xy, int prevWeight) {
				
		// Add the tile to the queue if it is in the map and has a weight less than the maximum weight
		if (_map.containsKey(xy.toString()) && _map.get(xy.toString()).getWeight() < prevWeight)
			queue.add(_map.get(xy.toString()));
		
		return queue;
		
	}
	
	/**
	 * Get the topologically adjacent tiles to xy based on
	 * the tile policy used by the map
	 * 
	 * @param xy The tile to test
	 * @return The list of adjacent tiles
	 */
	protected ArrayList<TileMarker> getAdjacentTiles(TileMarker tile) {
		ArrayList<TileMarker> list = new ArrayList<TileMarker>();
			
		Pair pair = tile.getPair();
		int priority = tile.getPriority() + 1;

		// Add 4 diagonally adjacent tiles
		if (_policy.getPassableAt(pair.add(1, 0)) && _policy.getPassableAt(pair.add(0, 1)))
			list.add(new TileMarker(pair.add(1, 1), tile.getWeight() + 1, priority));
		
		if (_policy.getPassableAt(pair.add(1, 0)) && _policy.getPassableAt(pair.add(0, -1)))
			list.add(new TileMarker(pair.add(1, -1), tile.getWeight() + 1, priority));
		
		if (_policy.getPassableAt(pair.add(-1, 0)) && _policy.getPassableAt(pair.add(0, 1)))
			list.add(new TileMarker(pair.add(-1, 1), tile.getWeight() + 1, priority));
		
		if (_policy.getPassableAt(pair.add(-1, 0)) && _policy.getPassableAt(pair.add(0, -1)))
			list.add(new TileMarker(pair.add(-1, -1), tile.getWeight() + 1, priority));
		
		priority = tile.getPriority();
		
		// Add 4 directly adjacent tiles
		list.add(new TileMarker(pair.add(1, 0), tile.getWeight() + 1, priority));
		list.add(new TileMarker(pair.add(-1, 0), tile.getWeight() + 1, priority));
		list.add(new TileMarker(pair.add(0, 1), tile.getWeight() + 1, priority));
		list.add(new TileMarker(pair.add(0, -1), tile.getWeight() + 1, priority));
		
		return list;
	}
	
	/**
	 * Ensures that there exists a shortest path in the map that allows the source to
	 * travel to the destination
	 * 
	 * @param source The tile where the contour map should backtrack to
	 * @return Whether the tile specified is in the contour map and can reach the target
	 */
	protected boolean expand(Pair source) {
		
		// Set the comparator source
		_comparator.setSource(source);
		
		// Check whether the source already exists, in which case a path exists to the target
		if (_map.containsKey(source.toString())) return true;
		
		// Arbitrary timeout point to prevent infinite iteration
		int max = 500;
		
		Pair size = _player.getLevel().getMap().getSize();
		
		/*
		 * Here we iterate through the queue, taking the top element and
		 * calculating all the possible moves that can be taken, adding
		 * these to the queue and the tile specified to the map if it is a
		 * valid destination 
		 */
		while (max > 0) {
			
			// Check for available trails
			if (_queue.isEmpty()) return false;
			
			// Take the first tile in the queue
			TileMarker tile = _queue.poll();
			
			// Helper variables
			Pair pair = tile.getPair();
			
			// Check the tile is on the map
			if (pair.x() < 0 || pair.y() < 0 || pair.x() >= size.x() || pair.y() >= size.y()) continue;
		
			// Check that the tile hasn't already been passed
			if (_map.containsKey(tile.toString())) {
				
				// Check if we can reach it with a higher priority
				if (_map.get(tile.toString()).getPriority() < tile.getPriority())
					// If so, update the map straight as we have already passed through this tile before 
					_map.put(tile.toString(), tile);
				
				 continue;
			}
			
			// Add to the map
			_map.put(tile.toString(), tile);
			
			// Check that the tile is enterable
			if (!_policy.getWalkableAt(tile.getPair(), _player)) continue;
			
			// Check that the tile is the finish
			if (source.equals(tile.getPair())) return true;
			
			// Addadjacent tiles to next
			for (TileMarker newTile: getAdjacentTiles(tile))
				_queue.add(newTile);
			
			// Decrement
			max--;
		}
		
		System.out.println(max);
		
		return false;
	}
	
	/**
	 * Ensures that all the shortest paths that go from the source to
	 * the destination are in the map
	 * 
	 * @param source The source tile to backtrack to
	 * @return source Returns true if there is at least one path form the source to the target
	 */
	protected boolean expandAll(Pair source) {
		
		// Check that the source has already been found
		if (!_map.containsKey(source.toString())) {
			
			// If not, try finding it with expand first, else return not found
			if (!expand(source)) return false;
		}
			
		// Expand ensures source is now in map
		
		/* 
		 * The extra paths we are trying to find must be of equal or less length
		 * than the current path. For an arbitrary tile, this tile can only be part
		 * of a shortest path if:
		 * 
		 * 		a) It is not further away from the target than the source
		 * 		b) It is not further away from the source than the target 
		 * 
		 * We now proceed to fill the space on the map that satisfy these criteria,
		 * hence finding all possible shortest paths from the source to the target 
		 */
		
		// Weight to fulfil a) and b)
		double squareWeight = Math.pow(_map.get(source.toString()).getWeight(), 2);
		
		/*
		 *  The true order ensures that once we have checked a tile then it cannot
		 *  reduce in weight at all during the rest of the pass
		 */
		
		PriorityQueue<TileMarker> list = new PriorityQueue<TileMarker>(20, new TrueComparator());
			
		// Add all the tiles adjacent to the tiles currently held in the map
		for (TileMarker curTile: _map.values()) {
			
			if (!_policy.getEnterableAt(curTile.getPair())) continue;
			
			for (TileMarker newTile: getAdjacentTiles(curTile))
				list.add(newTile);
		}
		
		// Arbitrary timeout value
		int max = 5000;
		
		Pair size = _player.getLevel().getMap().getSize();
		
		while (max > 0) {
			
			// Check for available trails
			if (list.isEmpty()) break;
			
			TileMarker tile = list.poll();
	
			// Helper variables
			Pair pair = tile.getPair();
						
			// Check whether I already exist
			if (_map.containsKey(tile.toString())) {
				
				// If an alternative path has a lower diagonal count then update and re-pass
				if (_map.get(tile.toString()).getWeight() < tile.getWeight() || _map.get(tile.toString()).getPriority() <= tile.getPriority())
					continue;
			}
			
			// Check the tile is on the map
			if (pair.x() < 0 || pair.y() < 0 || pair.x() >= size.x() || pair.y() >= size.y()) continue;
					
			/* 
			 * Update the map with my weight now in case I don't reach the criteria. This
			 * will save me being re-assessed when my neighbours are iterating
			 */
			_map.put(tile.toString(), tile);
			
			// a) Check the Manhattan distance to the target
			if (Math.pow(pair.x() - _target.x(), 2) + Math.pow(pair.y() - _target.y(), 2) > squareWeight) continue;
			// b) Check the Manhattan distance to the source
			if (Math.pow(pair.x() - source.x(), 2) + Math.pow(pair.y() - source.y(), 2) > squareWeight) continue;
			
			// Check that the tile is enterable
			if (!_policy.getEnterableAt(pair)) continue;
			
			// Add neighbours
			for (TileMarker newTile: getAdjacentTiles(tile))
				list.add(newTile);
						
			// Decrement
			max--;
		}
		
		return true;
	}
	
	public AbstractMap<String, TileMarker> getMap() {
		return _map;
	}
	
	public TilePolicy getPolicy() {
		return _policy;
	}
	
	public boolean link(int num) {
		_links += num;
		// Check for unlink
		return _links > 0;
	}
	
	public String getName() {
		return _target + " " + _policy.getName();
	}
}
