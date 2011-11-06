package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.level.Level;

public class SFX extends Logic {

	Animation _animation;
	
	public SFX(Level level) {
		super(level);
	}

	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		// Priority
		_priority = -100;
		
		// Load the animation
		String name = "";
		try {
			name = props.get("sprite");
			
			// Fake SFX
			if (name.equals("null")) {
				_level.getLogicManager().detachLogic(this);
				return;
			}
							
			_animation = ResourceLoader.getAnimationForPath(name);
			
			if (_animation == null)
				throw new GruntException("The SFX Load could not load the sprite [" + name + "]", null);
			
		} catch (Exception e) {
			throw new GruntException("The SFX Load could not load the sprite [" + name + "]", e);
		}
	}
	
	@Override
	public void render(Graphics g, long time) {
		if (_animation == null) return;
			
		// Check for end
		if (_animation.isStopped()) {
			_level.getLogicManager().detachLogic(this);
			return;
		}
		
		_animation.draw(_screen.x(), _screen.y());
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}
