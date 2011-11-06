package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.ResourceLoader;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class Diamond extends StatefulLogic implements CellLogic, PickupLogic {

	public Diamond(Level level) {
		super(level);
	}

	private String _tint;
	private Animation _animation;

	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		_tint = props.get("tint");
		
		// Add sprite
		_animation = ResourceLoader.getAnimationForPath("diamondz/" + _tint);
		
		// Set the priority
		_priority = _screen.y() * 100 + 40;
		
		// Add to cell manager
		_level.getCellManager().addCellObject(_tile, this);
	}
	
	@Override
	public void render(Graphics g, long time) {
		_animation.draw(_screen.x(), _screen.y());
	}
	@Override
	public Direction getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getKeyFrame() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCellName() {
		return "pickup";
	}
	
	public String getPickupType() {
		return _tint + "Diamond";
	}

	@Override
	public void pickup() {
		
		boolean trophy = false;
		
		// Add diamonds
		if (_tint.equals("Green")) {
			_level.getUIManager().addDiamonds(1);
			_level.getUIManager().getState().green++;
		}
		else if (_tint.equals("Blue")) {
			_level.getUIManager().addDiamonds(5);
			_level.getUIManager().getState().blue++;
		}
		else if (_tint.equals("Purple")) {
			_level.getUIManager().addDiamonds(20);
			_level.getUIManager().getState().purple++;
		}
		
		// Trophy so sparkle
		else {
			trophy = true;
			_level.getLogicManager().createSFX(_tile.multiply(32), "sparkle");
			
			if (_tint.equals("Bronze"))
				_level.getUIManager().getState().bronze = true;
			
			else if (_tint.equals("Silver"))
				_level.getUIManager().getState().silver = true;
			
			else
				_level.getUIManager().getState().gold = true;
		}
		
		if (trophy)
			_level.playSoundAt("powerup.wav", _tile.multiply(32), 50);

		_level.playSoundAt("treasure.wav", _tile.multiply(32), 40);
		
		
		_level.getCellManager().removeCellObject(_tile, this);
		_level.getLogicManager().detachLogic(this);
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State stateSave() {
		return null;
	}

	@Override
	public void stateRestore(State state) {}

}
