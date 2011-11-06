package com.hugheth.dizgruntled;

public class Attribute implements Comparable<Attribute>, Cloneable {
	
	public Object value;
	public int priority = 0;

	public Attribute(Object obj) {
		value = obj;
	}

	@Override
	public int compareTo(Attribute a) {
		if (priority > a.priority) return 1;
		if (priority < a.priority) return -1;
		return 0;
	}
	
	@Override
	public Attribute clone() throws CloneNotSupportedException {
		return (Attribute) super.clone();
	}
}