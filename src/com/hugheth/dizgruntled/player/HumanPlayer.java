package com.hugheth.dizgruntled.player;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.Tip;

public class HumanPlayer extends Player {
	
	public static final int STATE_FREE = 0;
	public static final int STATE_PLACING_NEW_GRUNT = 1;
	// TODO: That sort of thing
	
	// Dragging
	protected Boolean _dragging = false;
	
	// State
	protected int state = STATE_FREE;
	
	public HumanPlayer(Level level, Input input) {
		super(level);
	}
	
	// On left click
	public void leftClick(int x, int y) {
		
		// Free
		if (state != STATE_FREE) return;
		
		// Check for fog
		if (_level.getMap().fogAt(new Pair(x / 32, y / 32))) return;
		
		// Check for grunt
		ArrayList<Grunt> grunts = _level.getCellManager().getGruntsAt(new Pair(x / 32, y / 32));
		
		if (grunts.size() > 0) {
			
			if (grunts.get(0).getBehaviour().select(this)) {
				
				// De-select others
				if (!_level.getInput().isKeyDown(Input.KEY_LCONTROL)) {
					for (Grunt me : _grunts) {
						if (me != grunts.get(0))
							me.getBehaviour().deselect(this);
					}
				}
			}
			return;
		}
	}
	
	public void rightClick(int x, int y) {

		// Get real x and y
		x = x / 32;
		y = y / 32;
		
		boolean done = false;
		
		// Check for fog
		if (_level.getMap().fogAt(new Pair(x, y))) return;
		
		for (Grunt grunt : _grunts) {
			if ((Boolean) grunt.attribute("selected").value) {
				
				if (!grunt.getBehaviour().targetClick(new Pair(x, y))) {
					_level.getLogicManager().createSFX(new Pair(x * 32, y * 32), "badTarget");
					break;
				}
				done = true;
				// TODO: Allow multiple grunt pathfinding
				break;
			}
		}
		if (done) _level.getLogicManager().createSFX(new Pair(x * 32, y * 32), "target");
		
		// Play sound
		_level.playSound("move.wav", 40);
	}

	public void areaClick(int x, int y, int w, int h) {
		x = x /32;
		y = y / 32;
		w = w / 32;
		h = h / 32;
		
		if (w < 2 || h < 2) return;
		
		// Free
		if (state != STATE_FREE) return;
		
		if (!_level.getInput().isKeyDown(Input.KEY_LCONTROL)) {
			// Deselect grunts first
			for (Grunt me : _grunts)
				me.getBehaviour().deselect(this);
		}
		
		// Get tiles in region
		for (int i = 0; i < w; i ++) {
			for (int j = 0; j < h; j ++) {
				
				// Check for grunt
				ArrayList<Grunt> grunts = _level.getCellManager().getGruntsAt(new Pair(x + i, y + j));
				
				// Reselect
				if (grunts.size() > 0) {
					
					// Check for fog
					if (_level.getMap().fogAt(new Pair(x + i, y + j))) continue;
					
					grunts.get(0).getBehaviour().select(this);
				}				
			}
		}
	}

	public void deselectGrunts() {
		for (Grunt grunt: _grunts)
			grunt.getBehaviour().deselect(this);
	}
	
	@Override
	public void alignedGrunt(Grunt grunt) {
		
		Tip tip = null;
		
		// Check for tip
		try {
			tip = (Tip) _level.getCellManager().getCellObjects("tip", grunt.getTile()).get(0);
		} catch (Exception e) {}
		
		if (tip == null) return;
		
		// Display the message
		tip.display();
		
	}
}