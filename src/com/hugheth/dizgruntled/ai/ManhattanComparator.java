package com.hugheth.dizgruntled.ai;

import com.hugheth.dizgruntled.Pair;

public class ManhattanComparator implements TileComparator {

	private Pair _source;

	public void setSource(Pair source) {
		_source = source;
	}
	
	@Override
	public int compare(TileMarker t1, TileMarker t2) {
		
		Pair o1 = t1.getPair();
		Pair o2 = t2.getPair();
		
		if (Math.abs(o1.x() - _source.x()) + Math.abs(o1.y() - _source.y()) > Math.abs(o2.x() - _source.x()) + Math.abs(o2.y() - _source.y())) return 1;
		if (Math.abs(o1.x() - _source.x()) + Math.abs(o1.y() - _source.y()) < Math.abs(o2.x() - _source.x()) + Math.abs(o2.y() - _source.y())) return -1;
		return 0;
	}	
}
