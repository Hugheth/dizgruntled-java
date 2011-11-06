package com.hugheth.dizgruntled.logic;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;

/**
 * A Cell Object is an object that's located at a specific tile and can be used   
 * with the Cell Manager to interact with other Cell Objects
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 */
public interface CellLogic {

	public Direction getDirection();
	public int getSpeed();
	public long getKeyFrame();
	
	public Pair getTile();
	
	public String getCellName();
}
