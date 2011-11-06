package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.manager.TimeManager;

public abstract class MovingLogic extends StatefulLogic {

	protected MovingLogicState _state;
	
	public MovingLogic(Level level) {
		super(level);
		
		_state = new MovingLogicState();
	}
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) {
		
		super.initAligned(xy, props);
		
		// Get properties
		_state.direction = Direction.valueOf(props.get("direction"));
		_state.speed = Integer.parseInt(props.get("speed"));
		// Quantize the speed
		_state.speed = _state.speed - _state.speed % 50;
		
		// Set aligned frame
		_state.alignedFrame = TimeManager.FRAME_RATE * _state.speed / 1000;
		
		setKeyFrame(_level.getTimeManager().getCurrentFrame());
	}
	
	protected void setKeyFrame(long currentFrame) {
		// Update the key frame
		_state.keyFrame = currentFrame;
		_state.keyTime = currentFrame * TimeManager.FRAME_DURATION;
		
		// Set aligned frame
		_state.alignedFrame = TimeManager.FRAME_RATE * _state.speed / 1000;
		
		// Just aligned
		_state.justAligned = true;
		
		// Update the key position
		_state.keyPosition = _tile.clone();
		
		_state.target = _tile.clone();
		
		// Calculate the target
		if (_state.direction == Direction.East)
			_state.target = _state.target.add(1, 0);
		
		if (_state.direction == Direction.South)
			_state.target = _state.target.add(0, 1);
		
		if (_state.direction == Direction.West)
			_state.target = _state.target.add(-1, 0);
		
		if (_state.direction == Direction.North)
			_state.target = _state.target.add(0, -1);
	}
	
	protected void setStaticKeyFrame(long currentFrame, int duration) {
		
		setKeyFrame(currentFrame);
		
		_state.alignedFrame = duration; 		
	}
	
	public void renderPosition(long time, int priorityOffset) {
		
		// Set default position
		_screen.x(_state.keyPosition.x() * 32);
		_screen.y(_state.keyPosition.y() * 32);
		
		if (_state.moving) {
						
			// Get direction and calculate new position
			if (_state.direction == Direction.East)
				_screen.x((int) (_state.keyPosition.x() * 32 + ((time - _state.keyTime) * 32) / _state.speed));
			
			else if (_state.direction == Direction.South)
				_screen.y((int) (_state.keyPosition.y() * 32 + ((time - _state.keyTime) * 32) / _state.speed));
			
			else if (_state.direction == Direction.West)
				_screen.x((int) (_state.keyPosition.x() * 32 - ((time - _state.keyTime) * 32) / _state.speed));
			
			else
				_screen.y((int) (_state.keyPosition.y() * 32 - ((time - _state.keyTime) * 32) / _state.speed));
		}
		
		// Set the priority
		_priority = _screen.y() * 100 + priorityOffset;
	}
	
	@Override
	public void update(long currentFrame) {
		
		if (_state.justAligned) {
			
			_state.justAligned = false;
			
			// Start the move
			onMoveStart(currentFrame);
			
		} else if ((currentFrame - _state.keyFrame) % _state.alignedFrame == 0) {
			
			Pair xy = _tile.clone();
			
			if (_state.moving)
				// Set the tile
				_tile = _state.target.clone();
			
			// End the move
			onMoveFinish(currentFrame, xy);
			
		}

	}
	
	public Direction getDirection() {
		return _state.direction;
	}

	public int getSpeed() {
		if (_state.moving)
			return _state.speed;
		else
			return 0;
	}

	public long getKeyFrame() {
		return _state.keyFrame;
	}
	
	public abstract void onMoveStart(long currentFrame);
	public abstract void onMoveFinish(long currentFrame, Pair oldTile);
	
	@Override
	public void stateRestore(State state) {
		
		_state = (MovingLogicState) state;
		
		_tile = _state.keyPosition.clone();
	}
}
