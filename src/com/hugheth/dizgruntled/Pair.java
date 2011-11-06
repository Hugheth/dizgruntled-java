package com.hugheth.dizgruntled;

public class Pair implements Cloneable, Comparable<Object> {
	int _x;
	int _y;
	
	public static Pair parseString(String string) {
		try {
			
			String[] array = string.split(" ");
			return new Pair(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public Pair add(int addX, int addY) {
		return new Pair(_x + addX, _y + addY);
	}
	public Pair multiply(int factor) {
		return new Pair(_x * factor, _y * factor);
	}
	
	public Pair(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public String toString() {
		return _x + " " + _y;
	}
	
	public int x() {
		return _x;
	}
	
	public int y() {
		return _y;
	}
	
	public void x(int x) {
		_x = x;
	}
	
	public void y(int y) {
		_y = y;
	}
	
	public int distanceTo(Pair xy) {
		return (int) Math.sqrt(Math.pow(xy._x - _x, 2) + Math.pow(xy._y - _y, 2));
	}
	
	public Pair clone() {
		return new Pair(_x, _y);
	}

	public boolean equals(Object o) {
		try {
			Pair pair = (Pair) o;
			
			if (_x == pair._x && _y == pair._y) return true;
			
		} catch (Exception e) {}
		return false;
	}

	public int compareTo(Object o) {
		
		if (o instanceof Pair) {
			Pair pair = (Pair) o;
			if (pair.x() != _x || pair.y() != _y) return 1;
			
		} else if (o instanceof String) {
			return compareTo(Pair.parseString((String) o));
		}
		
		return 0;
	}	
}
