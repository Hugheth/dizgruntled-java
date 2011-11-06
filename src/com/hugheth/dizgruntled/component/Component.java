package com.hugheth.dizgruntled.component;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.logic.Logic;

public abstract class Component implements Comparable<Component> {

	private static long uid = 0;

	protected long _id;
	protected long _priority = 0;
	protected Logic _parent;

	public long getID() {
		return _id;
	}

	public long getPriority() {
		return _priority;
	}

	public void setAttachment(Logic e, int p) {
		_parent = e;
		_priority = p;
	}
	
	public Component() {
		_id = uid++;
	}
	
	public int compareTo(Component c) {
		if (_priority > c._priority) return 1;
		if (_priority < c._priority) return -1;
		return 0;
	}

	public abstract void update(long currentFrame);
	public abstract void render(Graphics g, long time);
}