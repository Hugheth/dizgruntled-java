package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

public class WindLogic extends StatefulLogic implements TriggerLogic {

	protected Pair _size;
	protected String[] _groups;
	
	private WindLogicState _state;
	
	public WindLogic(Level level) {
		super(level);
		
		_state = new WindLogicState();
	}
	
	public void init(Pair xy, TreeMap<String, String> props) {
		super.initAligned(xy, props);
		
		// Size
		_size = Pair.parseString(props.get("size"));

		_state.windy = Boolean.parseBoolean(props.get("windy"));
		
		// Check for windy
		if (_state.windy) _level.getMap().addWind(_tile, _size);

		if (props.containsKey("group")) { 
			// Get the groups
			_groups = props.get("group").split(" ");
			
			// Iterate through groups and add the Logic to the LogicManager
			for (String group: _groups) {
				_level.getTriggerManager().addTriggerToGroup(this, group);
			}
		}
	}
	
	@Override
	public void trigger() {
		
		_state.windy = !_state.windy;
		
		_level.playSoundAt("wind.ogg", _tile, 3.2f);
		
		// Check for windy
		if (_state.windy)
			_level.getMap().addWind(_tile, _size);
		else
			_level.getMap().removeWind(_tile, _size);
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (WindLogicState) state;
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}

class WindLogicState extends State {
	
	public boolean windy = false;	
}