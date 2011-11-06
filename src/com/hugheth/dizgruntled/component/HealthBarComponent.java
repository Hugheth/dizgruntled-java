package com.hugheth.dizgruntled.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class HealthBarComponent extends GruntComponent {

	public final static Color HEALTHY_LIGHT = new Color(10, 200, 10);
	public final static Color HEALTHY_DARK = new Color(10, 140, 10);
	
	public final static Color WOUNDED_LIGHT = new Color(180, 170, 10);
	public final static Color WOUNDED_DARK = new Color(120, 110, 10);
	
	public final static Color FATAL_LIGHT = new Color(200, 10, 10);
	public final static Color FATAL_DARK = new Color(140, 10, 10);
	
	@Override
	public void update(long currentFrame) {}

	@Override
	public void render(Graphics graphics, long time) {
		
		// Draw bar
		graphics.setColor(new Color(32, 32, 32));
		graphics.fillRect(_grunt.getScreen().x() + 1, _grunt.getScreen().y() - 17, 30, 6);
		
		// Check health
		int health = (Integer) _grunt.attribute("health").value;
		
		if (health > 50)
			graphics.setColor(HEALTHY_LIGHT);
		else if (health > 25)
			graphics.setColor(WOUNDED_LIGHT);
		else
			graphics.setColor(FATAL_LIGHT);
		
		graphics.fillRect(_grunt.getScreen().x() + 2, _grunt.getScreen().y() - 16, health * 0.28f, 2);
		
		if (health > 50)
			graphics.setColor(HEALTHY_DARK);
		else if (health > 25)
			graphics.setColor(WOUNDED_DARK);
		else
			graphics.setColor(FATAL_DARK);
		
		graphics.fillRect(_grunt.getScreen().x() + 2, _grunt.getScreen().y() - 14, health * 0.28f, 2);
	}
}
