package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Attribute;
import com.hugheth.dizgruntled.logic.PickupLogic;

public class PickupAction extends Action {

	private PickupLogic _logic;
	private boolean _object = false;
	
	private Attribute stopped = new Attribute(false);
	
	public PickupAction(PickupLogic logic) {
		super();
		_logic = logic;
	}
	
	@Override
	public void start() {
		super.start();
		
		// Pickup logic
		_logic.pickup();
		
		// Add de-selcted modifier
		_grunt.addModifier("movable", stopped, 20);
		_grunt.addModifier("walkable", stopped, 20);

		// Refresh
		_grunt.body.refresh("pickup/start", true);
		
		// Make sure body is correctly placed
		_grunt.getScreen().x(_grunt.getTile().x() * 32);
		_grunt.getScreen().y(_grunt.getTile().y() * 32);
		
	}
	
	// Transition
	public void transition() {
		if (_active) return;
		
		if (_grunt.getTask() != null) {
			_grunt.getTask().transition();
			return;
		}
		
		if (_grunt.getBehaviour().tileAlign()) { 
			// Set idle action
			_grunt.setAction(new IdleAction());
		}
		
	}

	@Override
	public void end() {
		if (_object) {
			
			super.end();
			
			// Add de-selcted modifier
			_grunt.removeModifier("movable", stopped);
			_grunt.removeModifier("walkable", stopped);
			
		} else {
			
			_object = true;
			
			// Refresh
			_grunt.body.refresh("pickup/" + _logic.getPickupType(), true);
		}
	}
	
	@Override
	public boolean cancel() {
		
		// Add de-selcted modifier
		_grunt.removeModifier("movable", stopped);
		_grunt.removeModifier("walkable", stopped);
		
		return true;
	}

	@Override
	public void update(long currentFrame) {}

	@Override
	public void render(Graphics g, long time) {}

}