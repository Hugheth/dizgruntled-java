package com.hugheth.dizgruntled.component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.ResourceLoader;

public class SelectRingComponent extends GruntComponent {

	private Animation _animation;

	public SelectRingComponent() {
		try {
			_animation = ResourceLoader.getAnimationForPath("select");
		} catch (Exception e) {
			System.out.println("IGNORE: Failed to load the select ring image.");
		}
	}
	
	@Override
	public void update(long currentFrame) {}

	@Override
	public void render(Graphics graphics, long time) {
		if (_animation != null)
			_animation.draw(_grunt.getScreen().x(), _grunt.getScreen().y());		
	}

}
