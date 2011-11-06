package com.hugheth.dizgruntled.logic;

import java.util.AbstractSet;
import java.util.TreeMap;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;

public class AreaTrigger extends Logic implements TriggerLogic {

	protected Pair _size;
	protected String[] _groups;
	
	public AreaTrigger(Level level) {
		super(level);
		
		// Set priority
		_priority = -50;
	}
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		_groups = props.get("group").split(" ");
		
		_size = new Pair(Integer.parseInt(props.get("size").split(" ")[0]) / 32, Integer.parseInt(props.get("size").split(" ")[1]) / 32);
		
		// Iterate through groups and add the Trigger to the LogicManager
		for (String group: _groups) {
			_level.getTriggerManager().addTriggerToGroup(this, group);
		}
			
		// Get tiles in region
		for (int i = 0; i < _size.x(); i++) {
			for (int j = 0; j < _size.y(); j++) {
				
			// Get the traits
			AbstractSet<String> traits = _level.getMap().getTileTraits(_tile.add(i, j));
			
			// Mark as a stateful tile
			if (traits.contains("trigger"))
				_level.getMap().markStatefulTile(1, _tile.add(i, j));
				
			else if (traits.contains("landTrigger"))
				_level.getMap().markStatefulTile(0, _tile.add(i, j));
			}
		}
	}
	
	@Override
	public void trigger() {
				
		// Get tiles in region
		for (int i = 0; i < _size.x(); i++) {
			for (int j = 0; j < _size.y(); j++) {
				
				AbstractSet<String> traits = _level.getMap().getTileTraits(_tile.add(i, j));
				
				// Check for trigger
				if (traits.contains("trigger")) {
					// Trigger
					_level.getMap().tileToggle(1, _tile.add(i, j));
					
				} else if (traits.contains("landTrigger")) {
					
					// Trigger landscape change
					_level.getMap().tileToggle(0, _tile.add(i, j));
				}
			}
		}
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
}
