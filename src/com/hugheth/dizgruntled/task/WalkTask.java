package com.hugheth.dizgruntled.task;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.action.IdleAction;
import com.hugheth.dizgruntled.action.MoveAction;
import com.hugheth.dizgruntled.ai.PathAgent;
import com.hugheth.dizgruntled.logic.Grunt;

public class WalkTask extends Task {
	
	protected Pair _target;
	protected MoveAction _action;
	protected PathAgent _path;
	protected Boolean _transition = false;
	
	public WalkTask(Grunt grunt, Pair xy) {
		super(grunt);
		_target = xy.clone();
	}

	@Override
	public void setup() {
		// Check for movable
		if ((Boolean) _grunt.attribute("movable").value)
			begin();
	}
	
	@Override
	public void begin() {
		// Make active
		_active = true;
		
		_grunt.attributeBase("moving").value = true;
		
		// Calculate path
		_path = _grunt.getPlayer().getPathfinder().requestAgent(_grunt.getTile(), _target, _grunt.getBehaviour().getTilePolicy());

		// Create walk action
		_action = new MoveAction();
		// Next tile
		nextTile();
	}
	
	@Override
	public void transition() {
		
		_grunt.attributeBase("moving").value = false;
		
		// Check for tile aligned
		if (!_grunt.getBehaviour().tileAlign()) return;
		
		_transition = true;
	}
	
	// Next tile
	protected void nextTile() {
		
		_grunt.attributeBase("moving").value = true;
		
		Boolean okay = false;
		// Destination
		Pair goTo = _path.next();
		// Check for goTo null (i.e. at end of path)
		if (goTo != null)
			// Move towards next point
			okay = _grunt.getBehaviour().moveTowards(goTo, false);
		// Move
		if (okay) {
			_grunt.setAction(_action);
		} else {
			_grunt.attributeBase("moving").value = false;
		 	_grunt.setAction(new IdleAction());
		}
	}

	@Override
	public void update(long currentFrame) {
		if (!_transition || _cancelled) return;
		
		_transition = false;
		
		if (!_active) {
			begin();
			return;
		}
		
		// Process the next tile
		nextTile();
	}

	@Override
	public void render(Graphics g, long time) {}
}
