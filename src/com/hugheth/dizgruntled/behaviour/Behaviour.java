package com.hugheth.dizgruntled.behaviour;

import java.util.AbstractSet;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.action.MoveAction;
import com.hugheth.dizgruntled.action.ExitAction;
import com.hugheth.dizgruntled.action.WebifyAction;
import com.hugheth.dizgruntled.ai.TilePolicy;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.player.Player;
import com.hugheth.dizgruntled.task.WalkTask;

/**
 * The Behaviour of a grunt determines how it reacts
 * to external input such as human clicking or being
 * attacked. It also handles characteristics of the
 * grunt such as how it can use its current tool. 
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 */
public abstract class Behaviour {
	protected Grunt _grunt;

	public Behaviour(Grunt grunt) {
		_grunt = grunt;
	}
	
	// Mouse click with this grunt at a tile
	public boolean targetClick(Pair xy) {
		
		// Check destination is enterable
		if (!getTilePolicy().getEnterableAt(xy)) return false;
		
		// Check for movable
		if ((Boolean) _grunt.attribute("movable").value
				|| (Boolean) _grunt.attribute("walkable").value) {
		
			// Start walking
			_grunt.setTask(new WalkTask(_grunt, xy));
		}
		
		return true;
	}
	
	// Mouse hover with this grunt over a tile
	public void targetHover(int x, int y) {
		
	}
	
	// Return the tile policy used in pathfinding
	public abstract TilePolicy getTilePolicy();
	
	// Select the grunt
	public boolean select(Player p) {
		if (!(Boolean) _grunt.attribute("selectable").value) return false;
		// Show health bar
		_grunt.attachComponent(_grunt.healthBar, 80);
		// Check for the right player
		if (p == _grunt.getPlayer()) {
			if (!(Boolean) _grunt.attribute("selected").value) {
				// Attach ring
				_grunt.attachComponent(_grunt.selectRing, 20);
				// Select
				_grunt.attribute("selected").value = true;
			}
			// Play sound
			_grunt.getLevel().playSound("select.wav", 30);

			return true;
		} else {
			return false;
		}
	}
	
	public boolean deselect() {
		return deselect(_grunt.getPlayer());
	}
	
	// De-select the grunt
	public boolean deselect(Player p) {
		// Check for the right player
		if (p == _grunt.getPlayer()) {
			// Check for selected
			if ((Boolean) _grunt.attribute("selected").value) {
				// Remove ring
				_grunt.detachComponent(_grunt.selectRing);
				// Hide health bar
				_grunt.detachComponent(_grunt.healthBar);
				// De-select
				_grunt.attribute("selected").value = false;
			}
		}
		return false;
	}
	
	// Move the grunt in a direction
	public boolean move(Direction direction) {
		return move(direction, true);
	}
	
	// Move the grunt in a direction
	public boolean move(Direction direction, boolean replaceAction) {
		// Check movable
		if ((Boolean) _grunt.attribute("movable").value) {
			_grunt.attribute("direction").value = direction;
			if (replaceAction)
				_grunt.setAction(new MoveAction());
			return true;
		}
		return false;
	}

	// Move towards a target, with preference to diagonal movement
	public boolean moveTowards(Pair xy, boolean replaceAction) {
		if (_grunt.getTile().x() < xy.x()) {
			if (_grunt.getTile().y() < xy.y())
				return move(Direction.SouthEast, replaceAction);
			
			else if (_grunt.getTile().y() > xy.y())
				return move(Direction.NorthEast, replaceAction);
			
			else
				return move(Direction.East, replaceAction);
			
		} else if (_grunt.getTile().x() > xy.x()) {
			if (_grunt.getTile().y() < xy.y())
				return move(Direction.SouthWest, replaceAction);
			
			else if (_grunt.getTile().y() > xy.y())
				return move(Direction.NorthWest, replaceAction);
			
			else
				return move(Direction.West, replaceAction);			
		} else {
			if (_grunt.getTile().y() < xy.y())
				return move(Direction.South, replaceAction);
			
			else if (_grunt.getTile().y() > xy.y())
				return move(Direction.North, replaceAction);
			
			else
				return false;
		}
	}
	
	/**
	 * Should be called whenever the grunt is newly aligned or
	 * the tile beneath it changes, when the action is in the
	 * transitional phase. Check to see whether the grunt
	 * should respond to what is beneath it.
	 * 
	 * @return If false, tileAlign has found changes to the grunt's behaviour and one should not attempt to change the grunt's current Action or Task
	 */
	public boolean tileAlign() {
		
		// Align with player
		_grunt.getPlayer().alignedGrunt(_grunt);
		
		// Put pressure on tile
		_grunt.getLevel().getPressureManager().tilePressure(_grunt.getTile());
		
		boolean okay = checkTile();
		
		if (!okay)
			_grunt.setTask(null);
		
		return okay;
	}
	/**
	 * Should be called whenever the grunt stops aligning with
	 * the tile beneath it 
	 */
	public void tileUnalign() {		
		// Remove pressure from tile
		_grunt.getLevel().getPressureManager().tileDepressure(_grunt.getTile());
	}
	
	public boolean checkTile() {
		// Check tile
		AbstractSet<String> traits = _grunt.getLevel().getMap().getTileTraits(_grunt.getTile());
		
		if (_grunt.getLevel().getMap().fogAt(_grunt.getTile())) deselect();
		
		// Check for arrows
		if (traits.contains("leftArrow"))
			move(Direction.West);
		else if (traits.contains("rightArrow"))
			move(Direction.East);
		else if (traits.contains("downArrow"))
			move(Direction.South);
		else if (traits.contains("upArrow"))
			move(Direction.North);
		else if (traits.contains("crossArrow"))
			move(_grunt.getDirection());
		
		else if (traits.contains("hole"))
			exit("Hole");
		
		else if (traits.contains("solid") || traits.contains("nogo"))
			exit("Flyup");
		
		else
			return true;
		
		return false;
	}
	
	public void exit(String type) {
		
		if (!_grunt.getAction().cancel()) return;
		
		if (_grunt.getTask() != null)
			_grunt.getTask().cancel();
		
		_grunt.setAction(new ExitAction(type));
		
	}
	
	public void webify() {
		
		if (!_grunt.getAction().cancel()) return;
		
		if (_grunt.getTask() != null)
			_grunt.getTask().cancel();
		
		_grunt.setAction(new WebifyAction());
		
	}
	
	public abstract String getName();
	
	public abstract void setup();
	public abstract void exit();
}