package com.hugheth.dizgruntled.component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class OffsetAnimation extends Animation {
	
	protected int _offsetX;
	protected int _offsetY;
	
	public OffsetAnimation(SpriteSheet sheet, int rate, int offsetX, int offsetY) {
		super(sheet, rate);
		_offsetX = offsetX;
		_offsetY = offsetY;
	}
	
	@Override
	public void draw(float x, float y) {
		super.draw(x + _offsetX, y + _offsetY);
	}
	
}
