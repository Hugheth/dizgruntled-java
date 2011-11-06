package com.hugheth.dizgruntled.manager;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

/**
 * The Time Manager handles the progress of events through the game and re-simulates them in case of network lag 
 *
 * Time in the dizGruntled is considered in two different ways. Logical "Frames" divide the game up into steps where
 * actions are taken to change the internal logical states of stateful objects, while graphical "Quanta" are visible
 * frames during which stateful objects interpolate their movements based on their current states   
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.StatefulLogic
 * @see com.hugheth.dizgruntled.manager.SnapshotManager
 * @see com.hugheth.dizgruntled.Level.Level
 */
public class TimeManager extends Manager {
	/**
	 * The duration of a frame in milliseconds
	 */
	public static final int FRAME_DURATION = 25;
	/**
	 * The number of frames in a second
	 */
	public static final int FRAME_RATE = 40;
	
	private TimeManagerState _state;
	
	private long _realTime = 0;
	private long _startTime = 0;
	
	private long _zeroTime = 0;
	
	public TimeManager(Level level) {
		super(level);
		
		_state = new TimeManagerState();
	}
	
	public void start(long currentTime) {
		_startTime = currentTime;
		_zeroTime = currentTime;
	}
	
	public void update(long time, int delta) {

		// Set the current time
		_state.currentTime = time - _zeroTime;
		_realTime = time - _startTime;
		
		boolean again = false;
		
		// Check for logical frame
		while (_state.currentTime - _state.lastFrameTime >= FRAME_DURATION) {

			if (again)
				System.out.print(".");
			
			_state.lastFrameTime += FRAME_DURATION;
			
			// Increment current frame
			_state.currentFrame++;
			
			// Update the managers
			_level.updateManagers(_state.currentFrame);
			
			again = true;
		}

	}
	
	public void render(Graphics g, Pair offset) {

		g.translate(-offset.x(), -offset.y());
		
		// Render the tiles
		_level.getMap().renderLayers(g, offset, new Pair(27, 20));
		
		// Render the logics
		_level.getLogicManager().renderLogics(g, _state.currentTime);
		
		// Render the fog
		_level.getMap().renderFog(g, offset, new Pair(27, 20));
		
		g.translate(offset.x(), offset.y());
		
		// Render the UI
		_level.getUIManager().renderUI(g);
		
	}
	
	public long getCurrentFrame() {
		return _state.currentFrame;
	}
	public long getCurrentTime() {
		return _state.currentTime;
	}
	public long getRealTime() {
		return _realTime;
	}

	@Override
	public String getName() {
		return "time";
	}

	@Override
	public State stateSave() {
		
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		
		// Increment zero time
		_zeroTime += _state.currentTime - ((TimeManagerState)state).currentTime;
		
		_state = (TimeManagerState) state;

	}
}

class TimeManagerState extends State {

	public long currentFrame = 0;
	public long currentTime = 0;
	public long lastFrameTime = 0;
	
}