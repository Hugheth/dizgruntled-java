package com.hugheth.dizgruntled.logic;

import java.util.ArrayList;
import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.ai.SimpleAITilePolicy;
import com.hugheth.dizgruntled.ai.TilePolicy;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class Spider extends MovingLogic implements CellLogic {

	protected Animation _animation;
	
	// Range
	protected byte _range;
	
	// State
	protected SpiderState _myState;
	
	// Policy
	protected TilePolicy _policy;

	public Spider(Level level) {
		super(level);
		
		_state = new SpiderState();
		_myState = (SpiderState) _state;
		
		_policy  = new SimpleAITilePolicy(level);
	}

	@Override
	public void init(Pair xy, TreeMap<String, String> props) {
		
		props.put("speed", "1000");
		
		super.init(xy, props);
		
		// Get properties
		_range = Byte.parseByte(props.get("range"));
		
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("spiderz/Idle" + _myState.direction.toString());
		
		// Stop moving
		_myState.moving = false;
		
		// Mark location in the cell renderer
		_level.getCellManager().addCellObject(_tile, this);
	}
	
	@Override
	public void onMoveStart(long currentFrame) {
		
		if (_myState.motion == SpiderMotion.Moving) {
			
			_level.getPressureManager().tileDepressure(_tile);
		
			// Mark new location in the cell renderer
			_level.getCellManager().addCellObject(_myState.target, this);
		}
	}

	@Override
	public void onMoveFinish(long currentFrame, Pair oldTile) {
		
		// Check for death
		if (_myState.motion == SpiderMotion.Dying) {
			
			// Detach logic
			_level.getLogicManager().detachLogic(this);
			
			// Don't do next
			return;
			
		} else if (_myState.motion == SpiderMotion.Moving) {
			
			
			// Remove old cell
			_level.getCellManager().removeCellObject(oldTile, this);
						
			// Put pressure on the tile
			_level.getPressureManager().tilePressure(_tile);
			
		} else if (_myState.motion == SpiderMotion.Spinning) {
			
			// Check criteria still applies
			if (_level.getMap().getTileTraits(_myState.target).contains("hole"))
				// TODO: Check for different type of hole
				_level.getMap().setTileType("HOLE_WEB", 0, _myState.target);
			
		}
		
		// Check whether the tile is enterable
		if (!_policy.getEnterableAt(_tile, this)) {
			// TODO: Check for watery death, etc.
			squash(currentFrame);
			return;
		}
		
		next(currentFrame);
	}

	@Override
	public void render(Graphics g, long time) {
		
		renderPosition(time, -110);
		
		// Draw the spider
		if (_animation != null)
			_animation.draw(_screen.x(), _screen.y());
	}
	
	protected void walk(long currentFrame) {
		
		if (_myState.motion != SpiderMotion.Moving)
			// Play noise
			_level.playSoundAt("spiderWalk.ogg", _screen, 40);
		
		_myState.moving = true;
		_myState.motion = SpiderMotion.Moving;
				
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("spiderz/Walk" + _myState.direction.toString());
	}
	
	protected void next(long currentFrame) {

		// Decide whether the spider should move
		Direction dir = scan();
				
		// Check moving
		if (dir != Direction.None) {
			
			_myState.direction = dir;
			
			// Add the key frame
			setKeyFrame(currentFrame);
			
			// Check for a grunt
			ArrayList<Grunt> list = _level.getCellManager().getGruntsAt(_myState.target);
			for (Grunt grunt: list) {
				// Check the grunt has already entered the tile
				if (grunt.getTile().equals(_myState.target)) {
					
					// Check if the grunt is still nearby
					if (grunt.getSpeed() == 0 || grunt.getKeyFrame() > currentFrame - 20) {
						
						// Kill the grunt
						grunt.getBehaviour().webify();
						
						spin(currentFrame);						
						return;
					}
				}
			}
		
			// Check enterable
			if (_policy.getEnterableAt(_myState.target)) {
				walk(currentFrame);
				return;
				
			} else if (_level.getMap().getTileTraits(_myState.target).contains("hole")) {
				
				// Start spinning a web
				spin(currentFrame);
				return;
				
			}
		}
		
		// Stop the spider
		if (_myState.motion != SpiderMotion.Stopped) {

			// Stop moving
			_myState.moving = false;
			
			_myState.motion = SpiderMotion.Stopped;
			// Change the animation
			_animation = ResourceLoader.getAnimationForPath("spiderz/Idle" + _myState.direction.toString());
		}
		
		// Stall for one turn
		setStaticKeyFrame(currentFrame, 1);
		
	}
	
	protected Direction scan() {
		
		// Check for fog
		if (_level.getMap().fogAt(_tile)) return Direction.None;
		
		if (scanDir(Direction.East) != Direction.None) return Direction.East;
		
		if (scanDir(Direction.West) != Direction.None) return Direction.West;
		
		return Direction.None;
	}
	
	protected Direction scanDir(Direction dir) {
		
		Pair xy = _tile.clone();
		int range = _range;
		
		do {
			// Get the latest tile
			if (dir == Direction.East)
				xy = xy.add(1, 0);
			
			else if (dir == Direction.South)
				xy = xy.add(0, 1);
			
			else if (dir == Direction.West)
				xy = xy.add(-1, 0);
			
			else
				xy = xy.add(0, -1);
			
			// Check for fog
			if (_level.getMap().fogAt(xy)) break;
			
			// Check tile
			for (Grunt grunt: _level.getCellManager().getGruntsAt(xy))
				if (grunt.getTile().equals(xy)) return dir;
			
			// Decrement
			range--;
			
		} while (range > 0);
		
		return Direction.None;		
	}
	
	protected void spin(long currentFrame) {
		// Stop moving
		_myState.moving = false;
		
		_myState.motion = SpiderMotion.Spinning;
		
		// Play noise
		_level.playSoundAt("spiderSpin.ogg", _screen, 40);
		
		// Stall for some turns
		setStaticKeyFrame(currentFrame, 80);
		
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("spiderz/Spin" + _myState.direction.toString());
		
	}
	
	public void squash(long currentFrame) {
		// Stop moving
		_myState.moving = false;
		
		_myState.motion = SpiderMotion.Dying;
			
		_priority = 0;
		_level.getLogicManager().sort();
		
		// Remove from the cell manager
		_level.getCellManager().removeCellObject(_tile, this);
		
		// Play noise
		_level.playSoundAt("exitSquash.ogg", _screen, 40);
		
		// Add the key frame
		setStaticKeyFrame(currentFrame, 120);
		
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("spiderz/Squash" + _myState.direction.toString());
	}
	
	@Override
	public State stateSave() {
		return _myState.Apply();
	}

	@Override
	public void stateRestore(State state) {
		
		super.stateRestore(state);
		
		_myState = (SpiderState) _state;
		
		// Set animation
		if (_myState.motion == SpiderMotion.Moving)
			// Load the skin for the ball in the direction
			_animation = ResourceLoader.getAnimationForPath("spiderz/Walk" + _myState.direction.toString());
		
		else if (_myState.motion == SpiderMotion.Stopped)
			_animation = ResourceLoader.getAnimationForPath("spiderz/Idle" + _myState.direction.toString());
			
		else if (_myState.motion == SpiderMotion.Spinning)
			_animation = ResourceLoader.getAnimationForPath("spiderz/Spin" + _myState.direction.toString());
		
		else if (_myState.motion == SpiderMotion.Dying)
			_animation = ResourceLoader.getAnimationForPath("spiderz/Squash" + _myState.direction.toString());
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getCellName() {
		return "spider";
	}

}

enum SpiderMotion {
	Stopped, Moving, Eating, Spinning, Dying
}

class SpiderState extends MovingLogicState {
	
	SpiderMotion motion = SpiderMotion.Stopped;
	
}