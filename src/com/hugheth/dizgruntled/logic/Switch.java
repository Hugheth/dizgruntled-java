package com.hugheth.dizgruntled.logic;

import java.util.AbstractSet;
import java.util.TreeMap;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class Switch extends StatefulLogic {

	protected SwitchState _state;
	
	/**
	 * Determines the grouping of the switch
	 */
	protected String[] _groups;
	
	protected boolean _many = false;
	
	public Switch(Level level) {
		super(level);
		
		_state = new SwitchState();
		
		// Set priority
		_priority = -100;
	}
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		AbstractSet<String> traits = _level.getMap().getTileTraits(_tile);
		
		// Check for initially pressed
		if (traits.contains("lowSwitch"))
			_state.pressed = true;
		
		try {
			_groups = props.get("group").split(" ");
		} catch (Exception e) {
			throw new GruntException("The Switch at (" + _tile + ") does not have a valid group.", e);
		}
		
		for (String group: _groups) {
			
			// Check for many and register with TriggerManager
			if (!_state.pressed && traits.contains("manySwitch"))
					_level.getTriggerManager().addManyToGroup(group, 1);

			// Check for diamond value
			if (traits.contains("diamond1")) _level.getTriggerManager().addDiamondsToGroup(group, -1);
			if (traits.contains("diamond5")) _level.getTriggerManager().addDiamondsToGroup(group, -5);
			if (traits.contains("diamond20")) _level.getTriggerManager().addDiamondsToGroup(group, -20);
		
			// Check for checkpoint
			if (traits.contains("checkpointSwitch")) _level.getTriggerManager().setCheckpointGroup(group);
		}
		
		// Mark as a stateful tile
		_level.getMap().markStatefulTile(1, _tile);
	}
	
	@Override
	public void update(long currentFrame) {
		
		AbstractSet<String> traits = _level.getMap().getTileTraits(_tile);
		
		// Check for depressed
		if (_level.getPressureManager().getTilePressureAt(_tile) > 0) {

			if (_state.pressed) return;
						
			// Check that the tile really is a switch
			if (!traits.contains("switch")) return;
			
			// Check for checkpoint reached
			for (String group: _groups)
				if (_level.getTriggerManager().checkpointReached(group)) return;
						
			// Toggle tile
			if (!_level.getMap().tileToggle(1, _tile)) return; 
					
			// Check for many switch
			if (traits.contains("manySwitch")) {

				for (String group: _groups)
					_level.getTriggerManager().addManyToGroup(group, -1);
			}
			
			_state.pressed = true;
			toggle();
			
		} else if (_state.pressed) {
			
			// Check that the tile really is a low switch
			if (!traits.contains("lowSwitch")) return;
			
			// Check for checkpoint reached
			for (String group: _groups)
				if (_level.getTriggerManager().checkpointReached(group)) return;
			
			// Toggle tile
			if (!_level.getMap().tileToggle(1, _tile)) return;
			
			_state.pressed = false;
			
			// Check for many switch
			if (traits.contains("manySwitch")) {
				for (String group: _groups)
					_level.getTriggerManager().addManyToGroup(group, 1);
				toggle();
			}
			
			if (traits.contains("holdSwitch"))
				toggle();
			
		}
	}
		
	protected void toggle() {
		// Iterate through the groups and call the trigger
		for (String group: _groups)
			_level.getTriggerManager().triggerGroup(group);
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (SwitchState) state;
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
}

class SwitchState extends State {
	
	public boolean pressed = false;
	
	public SwitchState clone() throws CloneNotSupportedException {
		return (SwitchState) super.clone();
	}
}
