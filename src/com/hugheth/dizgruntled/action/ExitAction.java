package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Attribute;

public class ExitAction extends Action {

	private Attribute deselection = new Attribute(false);
	
	protected String _type;
	
	public ExitAction(String type) {
		super();
		_type = type;
	}
	
	@Override
	public void start() {
		super.start();
		
		_grunt.getLevel().playSoundAt("exit" + _type + ".ogg", _grunt.getScreen(), 60);
		
		// Add de-selcted modifier
		_grunt.addModifier("selectable", deselection, 10);
		
		// De-select
		_grunt.getBehaviour().deselect();
		
		// Refresh
		_grunt.body.refresh("exit/" + _type, true);
		
		// Make sure body is correctly placed
		_grunt.getScreen().x(_grunt.getTile().x() * 32);
		_grunt.getScreen().y(_grunt.getTile().y() * 32);
		
		// Set priority
		_grunt.addModifier("priority", new Attribute(0), 50);
		_grunt.updatePriority();
		
		// Remove from the player
		//_grunt.getPlayer().removeGrunt(_grunt);
		
		// Remove from the cell manager
		_grunt.getLevel().getCellManager().removeCellObject(_grunt.getTile(), _grunt);
	}
	
	// Transition
	public void transition() {
		
	}

	@Override
	public void end() {
		super.end();
		
		_grunt.getLevel().getLogicManager().detachLogic(_grunt);
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