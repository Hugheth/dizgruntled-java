package com.hugheth.dizgruntled.ai;

import java.util.AbstractSet;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.logic.CellLogic;
import com.hugheth.dizgruntled.player.Player;

public class SimpleAITilePolicy implements TilePolicy {
	
	private Level _level;
	
	public SimpleAITilePolicy(Level level) {
		_level = level;
	}
	
	// Get passable
	public boolean getPassableAt(Pair xy) {
		// Get tile traits
		AbstractSet<String> traits = _level.getMap().getTileTraits(xy);
		// Check for no go
		if (traits.contains("solid") || traits.contains("nopass")) return false;
		return true;
	}
	// Get passable
	public boolean getEnterableAt(Pair xy) {
		return getEnterableAt(xy, null);
	}
	
	public boolean getEnterableAt(Pair xy, CellLogic ignore) {
		// Get tile traits
		AbstractSet<String> traits = _level.getMap().getTileTraits(xy);
		// Check for no go
		if (traits.contains("solid") || traits.contains("water") || traits.contains("nogo") || traits.contains("hill") || traits.contains("arrow") || traits.contains("pain") || traits.contains("death") || traits.contains("hole")) return false;
		
		// Check for obstacles
		for (CellLogic cell: _level.getCellManager().getSolidObjectsMovingTo(xy)) {
			if (cell != ignore)
				return false;
		}
		
		return true;
	}

	@Override
	public boolean getJumpableAt(Pair xy) {
		return false;
	}

	@Override
	public String getName() {
		return "normal";
	}

	@Override
	public boolean getWalkableAt(Pair xy, Player player) {
		// TODO Auto-generated method stub
		return false;
	}
}
