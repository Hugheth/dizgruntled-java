package com.hugheth.dizgruntled.component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.ResourceLoader;

public class GruntBodyComponent extends GruntComponent {

	protected Animation _animation;
	protected String _name = "";
	protected int offsetX = 0;
	protected int offsetY = 0;

	public void refresh(String name) {
		refresh(name, false);
	}
	
	public void refresh(String name, boolean ignoreController) {
		if (!ignoreController)
			name = _grunt.getBehaviour().getName() + "/" + name;
		name = "gruntz/" + name;

		// Update imagery
		
		if (!name.equals(_name)) {
			_name = name;
			_animation = ResourceLoader.getAnimationForPath(name);
		}
	}
	
	public void update(long currentFrame) {
		
		// Transition on animation end
		if (_animation != null && _animation.isStopped() && _grunt.getAction().isActive()) {
			_grunt.getAction().end();
			_grunt.getAction().transition();
		}
	}

	@Override
	public void render(Graphics graphics, long time) {
		if (_animation != null)
			_animation.draw(_grunt.getScreen().x(), _grunt.getScreen().y());
	}
}
