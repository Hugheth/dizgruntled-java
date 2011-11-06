package com.hugheth.dizgruntled.logic;

import java.util.AbstractSet;
import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class RollingBall extends MovingLogic implements CellLogic {

	protected Animation _animation;
	
	// Skin
	String _skin;
	
	private RollingBallState _myState;
			
	public RollingBall(Level level) {
		super(level);
		
		_state = new RollingBallState();
		_myState = (RollingBallState) _state;
	}

	@Override
	public void init(Pair xy, TreeMap<String, String> props) {
		super.init(xy, props);
	
		_skin = props.get("skin");
		
		// Load the skin for the ball in the direction
		_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + _myState.direction.toString());

	}
	@Override
	public void update(long currentFrame) {
				
		// Check for moving
		if (!_myState.moving) {
			// Check for destroy
			if(_animation.isStopped()) {
				_level.getLogicManager().detachLogic(this);
			}
			
			return;
		}
		
		super.update(currentFrame);
		
		// Check for squash grunts / spiders
		for (CellLogic cell: _level.getCellManager().getCollisionsWith(this, 16, currentFrame) ) {
			
			// Check for grunt
			try {
				Grunt grunt = (Grunt) cell;
				grunt.getBehaviour().exit("Squash");
			} catch (Exception e) {}
			
			// Check for spider
			try {
				Spider spider = (Spider) cell;
				spider.squash(currentFrame);
			} catch (Exception e) {}
		}
	}
		
	protected void smash(long currentFrame) {
		
		_myState.moving = false;
		_myState.motion = RollingBallMotion.Smashing;
		
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + "Smash");
	}
	protected void sink(long currentFrame) {
		
		_myState.moving = false;
		_myState.motion = RollingBallMotion.Sinking;
		
		// Change the animation
		_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + "Sink");
	}
	

	@Override
	public void render(Graphics g, long time) {
		
		// Render the position of the ball
		renderPosition(time, 20);
		
		// Draw the ball
		_animation.draw(_screen.x(), _screen.y());
	}

	@Override
	public String getCellName() {
		return "ball";
	}
	
	@Override
	public void onMoveStart(long currentFrame) {
		
		// Depressure old tile
		_level.getPressureManager().tileDepressure(_tile);
	}

	@Override
	public void onMoveFinish(long currentFrame, Pair oldTile) {

		// Get tile traits
		AbstractSet<String> traits = _level.getMap().getTileTraits(_tile);
		
		// Check for death
		if (traits.contains("solid"))
			smash(currentFrame);
		
		else if (traits.contains("water") || traits.contains("hole")) 
			sink(currentFrame);
		
		if (_level.getCellManager().getCellObjects("zombie", _tile).size() > 0)
			smash(currentFrame);
		
		// Check for arrows
		if (traits.contains("upArrow"))
			setDirection(Direction.North);
		
		else if (traits.contains("rightArrow"))
			setDirection(Direction.East);
		
		else if (traits.contains("downArrow"))
			setDirection(Direction.South);
		
		else if (traits.contains("leftArrow"))
			setDirection(Direction.West);
		
		if (_myState.moving)
			// Put pressure on the tile
			_level.getPressureManager().tilePressure(_tile);
		
		// Set a new key frame
		setKeyFrame(currentFrame);
		
	}
	
	protected void setDirection(Direction dir) {
		_myState.direction = dir;
		
		// Load the skin for the ball in the direction
		_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + _myState.direction.toString());
	}

	@Override
	public State stateSave() {
		
		return _myState.Apply();
	}

	@Override
	public void stateRestore(State state) {
		
		super.stateRestore(state);
		
		_myState = (RollingBallState) _state;
		
		// Set animation
		if (_myState.motion == RollingBallMotion.Rolling)
			// Load the skin for the ball in the direction
			_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + _myState.direction.toString());
		
		else if (_myState.motion == RollingBallMotion.Sinking)
			_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + "Sink");
			
		else if (_myState.motion == RollingBallMotion.Smashing)
			_animation = ResourceLoader.getAnimationForPath("ballz/" + _skin + "Smash");
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
}

enum RollingBallMotion {
	Rolling, Sinking, Smashing
}

class RollingBallState extends MovingLogicState {
	
	public RollingBallMotion motion = RollingBallMotion.Rolling;
	
}
