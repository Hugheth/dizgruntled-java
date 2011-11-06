package com.hugheth.dizgruntled.ai;

import java.util.Comparator;

import com.hugheth.dizgruntled.Pair;

public interface TileComparator extends Comparator<TileMarker> {

	public void setSource(Pair xy);
}
