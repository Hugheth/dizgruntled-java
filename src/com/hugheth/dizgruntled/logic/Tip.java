package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;

public class Tip extends Logic implements CellLogic {

	public Tip(Level level) {
		super(level);
	}

	protected String _text;
	
	public void init(Pair xy, TreeMap<String, String> props) {
		super.initAligned(xy, props);
		
		// TODO: Get locale
		if (props.containsKey("EN"))
			_text = props.get("EN").replace(" %% ", "\n");
		
		// Add to the cell manager
		_level.getCellManager().addCellObject(_tile.add(1, 0), this);
		_level.getCellManager().addCellObject(_tile.add(-1, 0), this);
		_level.getCellManager().addCellObject(_tile.add(0, 1), this);
		_level.getCellManager().addCellObject(_tile.add(0, -1), this);
	}
	
	public void display() {
		
		_level.playSoundAt("tip.ogg", _tile, 40);
		
		_level.getUIManager().displayMessage(_text);
	}

	@Override
	public Direction getDirection() {
		return Direction.None;
	}

	@Override
	public int getSpeed() {
		return 0;
	}

	@Override
	public long getKeyFrame() {
		return 0;
	}

	@Override
	public String getCellName() {
		return "tip";
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
