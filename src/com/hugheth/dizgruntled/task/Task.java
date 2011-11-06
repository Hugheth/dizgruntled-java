package com.hugheth.dizgruntled.task;

import com.hugheth.dizgruntled.component.GruntComponent;
import com.hugheth.dizgruntled.logic.Grunt;

public abstract class Task extends GruntComponent {
	
	protected Grunt _grunt;
	protected boolean _active = false;
	protected boolean _begun = false;
	protected boolean _cancelled = false;
	
	public Task(Grunt grunt) {
		_grunt = grunt;
	}
	
	public void cancel() {
		_cancelled = true;
	}
	
	// When the task is first queued
	public abstract void setup();
	// When the task becomes active
	public abstract void begin();
	
	// Transition between stages of the task
	public abstract void transition();
}
