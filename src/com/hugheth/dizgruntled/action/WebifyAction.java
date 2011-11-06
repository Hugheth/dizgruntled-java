package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Attribute;

public class WebifyAction extends Action {

private Attribute deselection = new Attribute(false);
	
	@Override
	public void start() {
		super.start();
		
		// Add de-selcted modifier
		_grunt.addModifier("selectable", deselection, 10);
		
		// De-select
		_grunt.getBehaviour().deselect();
		
		// Refresh
		_grunt.body.refresh("Webify", true);
		
		// Make sure body is correctly placed
		_grunt.getScreen().x(_grunt.getTile().x() * 32);
		_grunt.getScreen().y(_grunt.getTile().y() * 32);
		
		// Remove from the player
		//_grunt.getPlayer().removeGrunt(_grunt);
		
		// Replace cell manager
		_grunt.getLevel().getCellManager().removeCellObject(_grunt.getTile(), _grunt);
		
		// Set name
		_grunt.addModifier("cellName", new Attribute("zombie"), 10);
		
		_grunt.getLevel().getCellManager().addCellObject(_grunt.getTile(), _grunt);
		
	}
	
	// Transition
	public void transition() {
		
	}

	@Override
	public void end() {
		
	}
	
	@Override
	public boolean cancel() {
		return false;
	}

	@Override
	public void update(long currentFrame) {}

	@Override
	public void render(Graphics g, long time) {}

}
