package com.hugheth.dizgruntled.ai;

// TODO: MAKE TILE POLICIES SINGLETON FOR LEVEL

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.logic.CellLogic;
import com.hugheth.dizgruntled.player.Player;

/**
 * The Tile Policy is used by the Contour Map to decide whether
 * a specific tile is enterable, passable or jumpable. For example,
 * spring gruntz need their own separate policy which enables them
 * to jump across holes as part of their possible path.
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 */
public interface TilePolicy {

	public boolean getEnterableAt(Pair xy);
	public boolean getPassableAt(Pair xy);
	public boolean getJumpableAt(Pair xy);
	
	public boolean getWalkableAt(Pair xy, Player player);
	public boolean getEnterableAt(Pair xy, CellLogic ignore);
	
	/**
	 * All policies must return a name so they can be enumerated
	 * and a group of grunts all moving at once can share policies
	 * if they have the same movement. Comparing getName is
	 * equivalent to an equality test between policies.
	 * 
	 * @return The name of the Policy
	 */
	public String getName();
	
}