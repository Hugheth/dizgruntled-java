package com.hugheth.dizgruntled.player;

// TODO: MAKE PLAYER STATEFUL AND MAKE INPUT POLLED

import java.util.ArrayList;

import com.hugheth.dizgruntled.action.IdleAction;
import com.hugheth.dizgruntled.ai.PathArbiter;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.logic.Grunt;

public abstract class Player {
	
	// Level
	protected Level _level;
	// Grunts
	protected ArrayList<Grunt> _grunts;
	// Pathfinder
	protected PathArbiter pathFinder;
	
	public Player(Level level) {
		_level = level;
		// Path finder
		pathFinder = new PathArbiter(this);
		// New array
		_grunts = new ArrayList<Grunt>();
	}
	
	public Level getLevel() {
		return _level;
	}
	
	public void addGrunt(Grunt grunt) {
		if (!_grunts.contains(grunt))
			_grunts.add(grunt);
	}
	
	public void removeGrunt(Grunt grunt) {
		if (!_grunts.contains(grunt)) return;
		
		_grunts.remove(grunt);
		
		// Check size
		if (_grunts.size() == 0)
			System.out.println("FAILED");
	}
	
	public void stopGrunts() {
		for (Grunt grunt: _grunts) {
			
			grunt.getAction().cancel();
			
			if (grunt.getTask() != null)
				grunt.getTask().cancel();
			
			grunt.setAction(new IdleAction());
		}
	}
	
	public boolean hasGrunt(Grunt grunt) {
		return _grunts.contains(grunt);
	}
	
	public abstract void alignedGrunt(Grunt grunt);
	
	public PathArbiter getPathfinder() {
		return pathFinder;
	}
}
