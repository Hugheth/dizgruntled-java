package com.hugheth.dizgruntled.ai;

import com.hugheth.dizgruntled.Pair;

public class TileMarker {
	
	protected Pair _xy;
	protected int _weight;
	protected int _priority;
	
	public TileMarker(Pair xy, int weight, int priority) {
		_xy = xy;
		_weight = weight;
		_priority = priority;
	}
	
	public int getWeight() {
		return _weight;
	}
	
	public int getPriority() {
		return _priority;
	}
	
	public Pair getPair() {
		return _xy;
	}
	
	public String toString() {
		return _xy.toString();
	}
	
}
