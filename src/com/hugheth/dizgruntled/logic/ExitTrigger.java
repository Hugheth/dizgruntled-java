package com.hugheth.dizgruntled.logic;

import java.util.AbstractSet;
import java.util.TreeMap;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class ExitTrigger extends StatefulLogic implements TriggerLogic {
	
	protected String[] _groups;
		
	public ExitTrigger(Level level) {
		super(level);
				
		// Set priority
		_priority = -50;
	}
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		try {
			_groups = props.get("group").split(" ");
		} catch (Exception e) {
			throw new GruntException("The TileTrigger at (" + _tile + ") does not have a valid group.", e);
		}
		
		// Iterate through groups and add the Trigger to the LogicManager
		for (String group: _groups)
			_level.getTriggerManager().addTriggerToGroup(this, group);
		
	}
	
	@Override
	public void trigger() {
		_level.getUIManager().finish();
	}

	@Override
	public State stateSave() {
		return null;
	}

	@Override
	public void stateRestore(State state) { }

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}