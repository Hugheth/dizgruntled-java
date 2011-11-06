package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.logic.CellLogic;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.Spider;
import com.hugheth.dizgruntled.manager.TimeManager;

public class MoveAction extends Action {

	// Last key frame time
	protected long _lastKeyTime = 0;
	protected long _lastKeyFrame = 0;
	// Aligned frame interval
	protected int _alignedFrame = 0;
	
	protected Boolean _justAligned;
	
	// Target tile
	protected Pair _target;
	
	// Last position
	protected Pair _lastKeyPosition;

	@Override
	public void start() {
		super.start();
		
		_target = new Pair(0, 0);
		_justAligned = true;
		
		// Set attributes
		_grunt.attributeBase("selectable").value = true;
		_grunt.attributeBase("movable").value = false;
		
		// Check direction
		Direction dir = _grunt.getDirection();
		int speed = (Integer) _grunt.attribute("speed").value;
		// Change animation
		_grunt.body.refresh("Walk" + dir);
		
		// Set aligned frame
		_alignedFrame = TimeManager.FRAME_RATE * speed / 1000;
		
		// Calculate temporal values
		_lastKeyPosition = _grunt.getTile().clone();
		_lastKeyFrame = _lastKeyTime = _grunt.getLevel().getTimeManager().getCurrentFrame();
		_lastKeyTime = _grunt.getLevel().getTimeManager().getCurrentTime();
		
		// Calculate target
		if (dir == Direction.North || dir == Direction.NorthEast || dir == Direction.NorthWest)
			_target.y(-1);
		
		if (dir == Direction.East || dir == Direction.NorthEast || dir == Direction.SouthEast)
			_target.x(1);
		
		if (dir == Direction.South || dir == Direction.SouthWest || dir == Direction.SouthEast)
			_target.y(1);
		
		if (dir == Direction.West || dir == Direction.NorthWest || dir == Direction.SouthWest)
			_target.x(-1);
		
		// Check for grunts to squash
		for (CellLogic cell: _grunt.getLevel().getCellManager().getSolidObjectsMovingTo(new Pair(_lastKeyPosition.x() + _target.x(), _lastKeyPosition.y() + _target.y()))) {
			
			// Check for grunt
			try {
				
				Grunt grunt = (Grunt) cell;
				if (grunt != _grunt)
					grunt.getBehaviour().exit("Squash");
				
			} catch (Exception e) {}
			
			// Check for spider
			try {
				
				Spider spider = (Spider) cell;
				spider.squash(_grunt.getLevel().getTimeManager().getCurrentFrame());
				
			} catch (Exception e) {}
			
		}
		
		// Mark new location in the cell renderer
		_grunt.getLevel().getCellManager().addCellObject(new Pair(_lastKeyPosition.x() + _target.x(), _lastKeyPosition.y() + _target.y()), _grunt);
		
		// List primary frame
		_grunt.attributeBase("primaryFrame").value = _lastKeyFrame;

	}
	
	@Override
	public void update(long currentFrame) {
		
		if (_justAligned) {
			_justAligned = false;
			// Unalign with tile
			_grunt.getBehaviour().tileUnalign();
		}
		
		// Check for aligned
		if (currentFrame != _lastKeyFrame && (currentFrame - _lastKeyFrame) % _alignedFrame == 0) {
			end();
			transition();
		}
	}
	
	@Override
	public void render(Graphics graphics, long time) {

		// Set default position
		_grunt.getScreen().x(_lastKeyPosition.x() * 32);
		_grunt.getScreen().y(_lastKeyPosition.y() * 32);
				
		// Get direction and calculate new position
		int speed = (Integer) _grunt.attribute("speed").value;
		
		if (_target.y() == -1)
			_grunt.getScreen().y((int) (_lastKeyPosition.y() * 32 - ((time - _lastKeyTime) * 32) / speed));
		
		if (_target.x() == 1)
			_grunt.getScreen().x((int) (_lastKeyPosition.x() * 32 + ((time - _lastKeyTime) * 32) / speed));
		
		if (_target.y() == 1)
			_grunt.getScreen().y((int) (_lastKeyPosition.y() * 32 + ((time - _lastKeyTime) * 32) / speed));
		
		if (_target.x() == -1)
			_grunt.getScreen().x((int) (_lastKeyPosition.x() * 32 - ((time - _lastKeyTime) * 32) / speed));
		
		// Set the priority
		_grunt.updatePriority();
	}

	// Transition
	@Override
	public void transition() {
		
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
		
		// TODO: Quick fix that needs moving as end should only be called if the action is active
		if (!_active) return;
				
		super.end();
		
		// Movable
		_grunt.attributeBase("movable").value = true;
	
		// Remove grunt from the old location
		_grunt.getLevel().getCellManager().removeCellObject(_grunt.getTile(), _grunt);
	
		// Update the grunt position
		_grunt.getTile().x(_lastKeyPosition.x() + _target.x());
		_grunt.getTile().y(_lastKeyPosition.y() + _target.y());
	}
	
	@Override
	public boolean cancel() {
		super.cancel();
		
		// Movable
		_grunt.attributeBase("movable").value = true;
		
		// Remove grunt from the target location
		_grunt.getLevel().getCellManager().removeCellObject(_grunt.getTile().add(_target.x(), _target.y()), _grunt);
		
		return true;
		
	}
}
