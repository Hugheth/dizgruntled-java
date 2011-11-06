package com.hugheth.dizgruntled.action;

import com.hugheth.dizgruntled.component.GruntComponent;

public abstract class Action extends GruntComponent {

	/**
	 * Idle priority - all actions can interrupt this
	 */
	public static final int PRIORITY_IDLE = 0;
	/**
	 * Task priority - actions that can be started by a task e.g. walking, digging, attacking etc.
	 */
	public static final int PRIORITY_TASK = 1;
	/**
	 * Task priority - actions that are as a result of conflict e.g. deflecting, rebounding etc.
	 */
	public static final int PRIORITY_CONFLICT = 2;
	/**
	 * Task priority - actions that are as a result of the grunt dying e.g. falling, drowning, blowing up etc.
	 */
	public static final int PRIORITY_DYING = 3;
	
	// Active
	protected boolean _active = false;
	
	// At the start of the animation
	public void start() {
		_active = true;
	}
	
	// As the animation finishes
	public void transition(){};

	// At the end of the animation
	public void end() {
		_active = false;
	}
	
	// If the action fails
	public boolean cancel() {
		_active = false;
		return true;
	}
	
	public boolean isActive() {
		return _active;
	}
}