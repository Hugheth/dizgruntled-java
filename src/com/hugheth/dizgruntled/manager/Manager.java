package com.hugheth.dizgruntled.manager;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.Stateful;

/**
 * The Stateful interface is implemented by objects that change
 * their state during the course of the level, and so must be
 * able to serialize their current state in case backtracking
 * occurs.
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.system.StatefulLogic
 * @see com.hugheth.dizgruntled.manager.system.TimeManager
 * @see com.hugheth.dizgruntled.Level.Level
 */
public abstract class Manager implements Stateful {
	
	protected Level _level;
	
	public Manager(Level level) {
		// Set level
		_level = level;
		// Add the manager to the level
		_level.addManager(this);
	}
	
	public abstract String getName();
	
	public void update(long currentFrame) {};
}
