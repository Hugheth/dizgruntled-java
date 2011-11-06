package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.level.Level;

public class EyeCandy extends Logic {

	public EyeCandy(Level level) {
		super(level);
	}
	
	private Animation _animation;
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		// Priority
		_priority = _screen.y() * 100;
		// Add sprite
		_animation = ResourceLoader.getAnimationForPath("candyz/" + props.get("sprite"));
		// Jump to a random frame
		_animation.setCurrentFrame((int) Math.floor(Math.random() * _animation.getFrameCount()));
	}
	
	@Override
	public void render(Graphics g, long time) {
		_animation.draw(_screen.x(), _screen.y());
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
}
