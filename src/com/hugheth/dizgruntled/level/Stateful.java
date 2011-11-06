package com.hugheth.dizgruntled.level;

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
public interface Stateful {

	public State stateSave();
	public void stateRestore(State state);
	public String getName(); 
	
}
