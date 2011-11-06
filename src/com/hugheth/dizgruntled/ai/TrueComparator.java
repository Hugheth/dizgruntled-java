package com.hugheth.dizgruntled.ai;

import com.hugheth.dizgruntled.Pair;

public class TrueComparator implements TileComparator {

	@Override
	public int compare(TileMarker tile1, TileMarker tile2) {
		
		if (tile1.getWeight() > tile2.getWeight()) return 1;
		if (tile1.getWeight() < tile2.getWeight()) return -1;
		if (tile1.getPriority() > tile2.getPriority()) return 1;
		if (tile1.getPriority() < tile2.getPriority()) return -1;
		return 0;
	}

	@Override
	public void setSource(Pair xy) {}

}
