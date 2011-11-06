package com.hugheth.dizgruntled.manager;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

import java.util.TreeMap;

/**
 * The Snapshot Manager handles the storage and retrieval of history states in case of network lag or replay  
 *
 * This class deals with a couple of different phenomena:
 * 1) Entire game snapshots. These store the entire state of the game at intervals and can be used to restore the
 * 	  game if any player lags out or a player wants to join a current game.
 * 2) A circular buffer of stateful object state history. This is used to rewind the game in the common case that
 *    commands are received from other players that took place in the past.      
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.StatefulLogic
 */
public class SnapshotManager extends Manager {

	TreeMap<Long, State> _snapshots;
	
	public SnapshotManager(Level level) {
		super(level);
		
		_snapshots = new TreeMap<Long, State>();
	}
	
	public void save(long id) {
		
		// Save the level state
		_snapshots.put(id, _level.stateSave());
		
	}
	
	public void load(long id) {
		
		if (!_snapshots.containsKey(id)) return;
		
		// Restore the level state
		try {
			_level.stateRestore((State) _snapshots.get(id).clone());
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("ABORT: Couldn't reload the snapshot.");
		}
		
	}
	
	@Override
	public State stateSave() {
		return null;
	}

	@Override
	public void stateRestore(State state) {}

	@Override
	public String getName() {
		return "snapshot";
	}
	
}
