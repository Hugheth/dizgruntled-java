package com.hugheth.dizgruntled.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.logic.TriggerLogic;

public class TriggerManager extends Manager {

	private TriggerManagerState _state;
	
	private HashMap<String, ArrayList<TriggerLogic>> _triggerGroups;
	private HashMap<String, Integer> _diamondGroups;

	public TriggerManager(Level level) {
		super(level);
		
		_state = new TriggerManagerState();
		
		// Create trigger groups
		_triggerGroups = new HashMap<String, ArrayList<TriggerLogic>>(64);
		// Many groups
		_state.manyGroups = new HashMap<String, Integer>(8);
		
		_diamondGroups = new HashMap<String, Integer>(8);
	}

	public void addTriggerToGroup(TriggerLogic trigger, String group) {
		// Check whether the trigger group exists
		if (!_triggerGroups.containsKey(group))
			_triggerGroups.put(group, new ArrayList<TriggerLogic>());
		_triggerGroups.get(group).add(trigger);
	}
	
	public void addManyToGroup(String group, int weight) {
		
		if (_state.manyGroups.containsKey(group))
			_state.manyGroups.put(group, _state.manyGroups.get(group) + weight);
		else
			_state.manyGroups.put(group, weight);
		
		// Ensure toggle exists
		if (!_state.manyToggle.containsKey(group))
			_state.manyToggle.put(group, false);
	}
	
	public void setCheckpointGroup(String group) {
		
		_state.checkToggle.put(group, false);
		
	}
	
	public boolean checkpointReached(String group) {
		if (_state.checkToggle.containsKey(group))
			return _state.checkToggle.get(group);
		
		return false;
	}
	
	public void addDiamondsToGroup(String group, int count) {
		
		if (_diamondGroups.containsKey(group))
			_diamondGroups.put(group, _diamondGroups.get(group) + count);
		else
			_diamondGroups.put(group, count);
	}
	
	public void triggerGroup(String group) {
		// Check that the trigger group exists
		if (!_triggerGroups.containsKey(group)) return;
				
		// Check whether it is a many group
		if (_state.manyGroups.containsKey(group)) {
			
			Boolean many = false;
			
			if (_state.manyGroups.get(group) < 1) {
				if (_state.manyToggle.get(group))
					return;
				else
					many = true;
			}

			if (_state.manyGroups.get(group) > 0 && !_state.manyToggle.get(group))
				return;
			
			_state.manyToggle.put(group, many);
		}
		
		// Check for diamond count
		if (_diamondGroups.containsKey(group)) {
			if (_level.getUIManager().getDiamonds() + _diamondGroups.get(group) < 0) {
				
				if (_state.manyToggle.get(group))
					_level.playSound("error.ogg", 80);
				return;
			}
			
			_level.getUIManager().addDiamonds(_diamondGroups.get(group));
			_level.playSound("checkpoint.wav", 60);
		}
		
		// Check for already toggled checkpoint
		if (_state.checkToggle.containsKey(group)) {
			if (_state.checkToggle.get(group)) return;
			else _state.checkToggle.put(group, true);
		}
		
		// Get all the triggers
		ArrayList<TriggerLogic> logics = _triggerGroups.get(group);
		// Iterate through
		for (TriggerLogic logic: logics)
			logic.trigger();
		
		return;
	}
	
	@Override
	public void update(long currentFrame) {
		if (currentFrame == 132) triggerGroup("init");
	}
	
	@Override
	public String getName() {
		return "trigger";
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (TriggerManagerState)state;
	}

}

class TriggerManagerState extends State {
	
	public HashMap<String, Boolean> manyToggle = new HashMap<String, Boolean>(8);
	public HashMap<String, Boolean> checkToggle = new HashMap<String, Boolean>(8);
	public HashMap<String, Integer> manyGroups = new HashMap<String, Integer>(8);
	
	public TriggerManagerState clone() throws CloneNotSupportedException {
		
		TriggerManagerState state = (TriggerManagerState) super.clone();
		
		// Clone variables
		state.manyToggle = new HashMap<String, Boolean>();
		state.manyToggle.putAll(manyToggle);
		
		state.checkToggle = new HashMap<String, Boolean>();
		state.checkToggle.putAll(checkToggle);
		
		state.manyGroups = new HashMap<String, Integer>();
		state.manyGroups.putAll(manyGroups);
		
		return state;		
	}
}
