package com.hugheth.dizgruntled.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.logic.CellLogic;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.PickupLogic;

/**
 * The Cell Manager provides a platform for logics that require interaction with 
 * other logics to interact with each other. Logics implement the CellObject interface
 * which gives them a name and a motion relative to the given tile where they are
 * indexed at.
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.StatefulLogic
 * @see com.hugheth.dizgruntled.manager.SnapshotManager
 * @see com.hugheth.dizgruntled.Level.Level
 */
public class CellManager extends Manager {

	private CellManagerState _state;
	
	private ArrayList<String> _solids;
	private ArrayList<String> _collidables;
	private HashMap<String, ArrayList<CellLogic>> _toAdd;
	private HashMap<String, ArrayList<CellLogic>> _toRemove;
	
	public CellManager(Level level) {
		super(level);
		
		_state = new CellManagerState();
		
		_solids = new ArrayList<String>();
		_collidables = new ArrayList<String>();
		_state.cells = new HashMap<String, ArrayList<CellLogic>>(10);
		_toAdd = new HashMap<String, ArrayList<CellLogic>>(5);
		_toRemove = new HashMap<String, ArrayList<CellLogic>>(5);
		
		_solids.add("grunt");
		_solids.add("spider");
		_solids.add("zombie");
		
		_collidables.add("grunt");
		_collidables.add("spider");
	}
	
	@Override
	public void update(long currentFrame) {
		// Remove old cell objects
		for (String key: _toRemove.keySet()) {
			
			if (!_state.cells.containsKey(key)) {
				System.out.println("IGNORE: The cell key entry [" + key + "] was tried to be removed but doesn't exist");
				return;
			}
			
			if (_toRemove.get(key).size() == _state.cells.get(key).size()) {
				_state.cells.remove(key);
				
			} else {
				ArrayList<CellLogic> target = _state.cells.get(key);
				ArrayList<CellLogic> list = _toRemove.get(key);
				
				for (CellLogic object: list) {
					target.remove(object);
				}
			}
		}

		// Add new cell objects
		for (String key: _toAdd.keySet()) {
			
			if (_state.cells.containsKey(key))
				_state.cells.get(key).addAll(_toAdd.get(key));
			else
				_state.cells.put(key, _toAdd.get(key));
		}
		
		// Clear the add map
		_toAdd.clear();
		
		// Clear the add map
		_toRemove.clear();
	}
	
	public void addCellObject(Pair xy, CellLogic object) {
		
		String key = xy + " " + object.getCellName(); 
		
		if (_toAdd.containsKey(key)) {
			_toAdd.get(key).add(object);
		} else {
			ArrayList<CellLogic> list = new ArrayList<CellLogic>();
			list.add(object);
			_toAdd.put(key, list);
		}
	}
	
	public void removeCellObject(Pair xy, CellLogic object) {
		
		String key = xy + " " + object.getCellName();
		
		// Check that the object trying to remove is 
		if (!_state.cells.containsKey(key)) return;
		if (!_state.cells.get(key).contains(object)) return;
		
		if (_toRemove.containsKey(key)) {
			_toRemove.get(key).add(object);
		} else {
			ArrayList<CellLogic> list = new ArrayList<CellLogic>();
			list.add(object);
			_toRemove.put(key, list);
		}
	}
	
	public ArrayList<CellLogic> getCellObjects(String name, Pair xy) {
		
		String key = xy + " " + name;
		
		if (!_state.cells.containsKey(key)) return new ArrayList<CellLogic>();

		return _state.cells.get(key);
	}
	
	public ArrayList<Grunt> getGruntsAt(Pair xy) {
		try {
			ArrayList<Grunt> grunts = new ArrayList<Grunt>();
			
			for (CellLogic cell: getCellObjects("grunt", xy)) {
				grunts.add((Grunt) cell);
			}
			
			return grunts;
			
		} catch (Exception e) {
			System.out.println("IGNORE: Non-grunt CellObjects found using Grunt key at (" + xy + ")");
			return new ArrayList<Grunt>();
		}
	}
	
	public PickupLogic getPickupAt(Pair xy) {
		try {
			return (PickupLogic) getCellObjects("pickup", xy).get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void addSolidName(String name) {
		_solids.add(name);
	}
	
	public ArrayList<CellLogic> getSolidObjectsAt(Pair xy) {
		ArrayList<CellLogic> list = new ArrayList<CellLogic>(); 
			
		for (String name: _solids) {
			
			String key = xy + " " + name;
			
			if (_state.cells.containsKey(key))
				list.addAll(_state.cells.get(key));
		}
		
		return list;
			
	}
	
	public ArrayList<CellLogic> getSolidObjectsMovingTo(Pair xy) {
		
		ArrayList<CellLogic> list = getSolidObjectsAt(xy); 
		
		Iterator<CellLogic> it = list.iterator();
		
		while (it.hasNext()) {
			
			CellLogic obj = it.next();
			// Check object is moving into tile
			if (obj.getSpeed() != 0 && !obj.getTile().equals(xy))
				it.remove();		
		}
		
		return list;
	}
	
	public ArrayList<CellLogic> getCollisionsWith(CellLogic cell, int radius, long currentFrame) {
		
		// Calculate current position of cell object
		Pair xy = cell.getTile();
		
		Pair uv = xy.clone();
		Direction dir = cell.getDirection();
		
		// Calculate new position of the cell object
		if (dir == Direction.North || dir == Direction.NorthEast || dir == Direction.NorthWest)
			uv = uv.add(0, -1);
		
		if (dir == Direction.East || dir == Direction.NorthEast || dir == Direction.SouthEast)
			uv = uv.add(1, 0);
		
		if (dir == Direction.South || dir == Direction.SouthWest || dir == Direction.SouthEast)
			uv = uv.add(0, 1);
		
		if (dir == Direction.West || dir == Direction.NorthWest || dir == Direction.SouthWest)
			uv = uv.add(-1, 0);
		
		// Add CellObjects to list
		ArrayList<CellLogic> list = new ArrayList<CellLogic>();
		
		// Add cell objects
		for (String name: _collidables) {
			list.addAll(getCellObjects(name, xy));
			
			// Get cell objects in next tile
			ArrayList<CellLogic> news = getCellObjects(name, uv);
			
			// Check that they don't already exist in list
			for (CellLogic obj: news)
				if (!list.contains(obj)) list.add(obj);
		}
		
		// Get the position of the object
		Pair pos = getExactXY(cell, currentFrame);
		
		// Compare with others
		ArrayList<CellLogic> found = new ArrayList<CellLogic>();
		
		for (CellLogic test: list) {
			if (pos.distanceTo(getExactXY(test, currentFrame)) <= radius) found.add(test);
		}
		
		return found;
	}
	
	public Pair getExactXY(CellLogic cell, long currentFrame) {
		
		Pair xy = cell.getTile().multiply(32);
		
		Direction dir = cell.getDirection();
		
		if (cell.getSpeed() == 0) return xy;
		
		int distance = (int) (((currentFrame - cell.getKeyFrame())) / (TimeManager.FRAME_RATE * cell.getSpeed() / 1000f) * 32);
		
		// Calculate position of the cell object
		if (dir == Direction.North || dir == Direction.NorthEast || dir == Direction.NorthWest)
			xy = xy.add(0, -distance);
		
		if (dir == Direction.East || dir == Direction.NorthEast || dir == Direction.SouthEast)
			xy = xy.add(distance, 0);
		
		if (dir == Direction.South || dir == Direction.SouthWest || dir == Direction.SouthEast)
			xy = xy.add(0, distance);
		
		if (dir == Direction.West || dir == Direction.NorthWest || dir == Direction.SouthWest)
			xy = xy.add(-distance, 0);
		
		return xy;
	}

	@Override
	public String getName() {
		return "cell";
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (CellManagerState) state;
	}

}

class CellManagerState extends State {
	
	public HashMap<String, ArrayList<CellLogic>> cells;
	
	public CellManagerState clone() throws CloneNotSupportedException {
		
		CellManagerState state = (CellManagerState) super.clone();
		
		// Reset cell array
		state.cells = new HashMap<String, ArrayList<CellLogic>>();
		state.cells.putAll(cells);
		
		return state;
	}
}
