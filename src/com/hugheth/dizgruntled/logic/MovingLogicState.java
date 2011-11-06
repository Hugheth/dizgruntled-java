package com.hugheth.dizgruntled.logic;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.State;

public class MovingLogicState extends State {

	// Current direction
	public Direction direction;
	// Moving
	public boolean moving = true;
	// Current speed
	public int speed;
	
	// Last key frame time
	public long keyTime = 0;
	public long keyFrame = 0;
	// Aligned frame 
	public int alignedFrame = 0;
	
	// Last position
	public Pair keyPosition;
	// Next position
	public Pair target;
	
	// Previously aligned
	protected boolean justAligned = false;
	
	
	public MovingLogicState clone() throws CloneNotSupportedException {
		
		MovingLogicState state = (MovingLogicState) super.clone();
		
		state.keyPosition = keyPosition.clone();
		
		state.target = target.clone();
		
		return state;
		
	}
}
