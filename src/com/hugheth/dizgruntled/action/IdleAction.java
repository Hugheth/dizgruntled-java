package com.hugheth.dizgruntled.action;

import org.newdawn.slick.Graphics;

public class IdleAction extends Action {

	public void start() {
		super.start();
		
		// State
		_grunt.attributeBase("selectable").value = true;
		_grunt.attributeBase("movable").value = true;
		// Refresh body
		_grunt.body.refresh("Idle" + _grunt.getDirection());
		
		// Make sure body is correctly placed
		_grunt.getScreen().x(_grunt.getTile().x() * 32);
		_grunt.getScreen().y(_grunt.getTile().y() * 32);
	}

	@Override
	public void update(long currentFrame) {
		
		if (_grunt.getLevel().getMap().tilesChanged())
			_grunt.getBehaviour().checkTile();
	}

	@Override
	public void render(Graphics graphics, long time) {}
}