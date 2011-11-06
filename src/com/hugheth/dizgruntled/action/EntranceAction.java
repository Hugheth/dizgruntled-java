package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Attribute;

public class EntranceAction extends Action {

	private Attribute deselection = new Attribute(false);
	
	@Override
	public void start() {
		super.start();
		
		_grunt.attributeBase("selectable").value = false;
		// Add de-selcted modifier
		_grunt.addModifier("selected", deselection, 10);
		// Refresh
		_grunt.body.refresh("entrance1", true);
	}
	
	// Transition
	public void transition() {
		// Check aligned
		if (_grunt.getBehaviour().tileAlign()) {
			// Set idle action
			_grunt.setAction(new IdleAction());
		}
	}

	@Override
	public boolean cancel() {
		end();
		return true;
	}
	
	@Override
	public void end() {
		super.end();
		
		// Remove de-selection
		_grunt.removeModifier("selected", deselection);
		
		// Enlist in the cell manager
		_grunt.getLevel().getCellManager().addCellObject(_grunt.getTile(), _grunt);
	}

	@Override
	public void update(long currentFrame) {}

	@Override
	public void render(Graphics g, long time) {}
}