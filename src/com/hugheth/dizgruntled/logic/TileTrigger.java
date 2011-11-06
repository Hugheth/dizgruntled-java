package com.hugheth.dizgruntled.logic;

import java.util.AbstractSet;
import java.util.TreeMap;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.manager.TimeManager;

public class TileTrigger extends StatefulLogic implements TriggerLogic {

	private TileTriggerState _state;
	
	protected String[] _groups;
	protected long _delay = 0;
	protected long _rebound = 0;
	protected long _response = 0;
		
	public TileTrigger(Level level) {
		super(level);
		
		_state = new TileTriggerState();
		
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
		
		// Delay between triggers
		if (props.containsKey("delay"))
			_delay = Integer.parseInt(props.get("delay")) * TimeManager.FRAME_RATE / 1000;
		// Rebound after being triggered
		if (props.containsKey("rebound")) {
			_rebound = Integer.parseInt(props.get("rebound")) * TimeManager.FRAME_RATE / 1000;
		}
		// Take time to respond to trigger
		if (props.containsKey("response")) {
			_response = Integer.parseInt(props.get("response")) * TimeManager.FRAME_RATE / 1000;
		}
		
		// Iterate through groups and add the Trigger to the LogicManager
		for (String group: _groups) {
			_level.getTriggerManager().addTriggerToGroup(this, group);
		}
		
		// Get the traits
		AbstractSet<String> traits = _level.getMap().getTileTraits(_tile);
		
		// Mark as a stateful tile
		if (traits.contains("trigger"))
			_level.getMap().markStatefulTile(1, _tile);
			
		else if (traits.contains("landTrigger"))
			_level.getMap().markStatefulTile(0, _tile);
	}

	private void trigger(boolean ignoreRebound, boolean ignoreResponse, boolean ignoreDelay) {
		// Get current time
		long curFrame = _level.getTimeManager().getCurrentFrame();
		
		// Check for delay
		if (!ignoreDelay && _delay > 0) {
			if (curFrame < _state.delayTime) return;
			_state.delayTime = curFrame + _delay;
		}
		
		// Check for response
		if (!ignoreResponse && _response > 0) {
			_state.responseFrame = curFrame + _response;
			_state.timed = true;
			return;
		}
		
		boolean okay = toggle();
			
		// Check for rebound
		if (okay && _rebound > 0 && !ignoreRebound) {
			_state.reboundFrame = curFrame + _rebound;
			_state.timed = true;
		}
	}
	
	private boolean toggle() {
		
		AbstractSet<String> traits = _level.getMap().getTileTraits(_tile);
		
		// Check for trigger
		if (traits.contains("trigger")) {
			// Trigger
			_level.getMap().tileToggle(1, _tile);
			return true;
			
		} else if (traits.contains("landTrigger")) {
			// Trigger landscape change
			_level.getMap().tileToggle(0, _tile);
			return true;
		}
		return false;
	}
	
	@Override
	public void trigger() {
		trigger(false, false, false);
	}
	
	@Override
	public void update(long currentFrame) {
					
		if (!_state.timed) return;
		
		if (currentFrame == _state.responseFrame) {
			_state.timed = false;
			trigger(false, true, true);
			return;
		}
		
		if (currentFrame == _state.reboundFrame) {
			_state.timed = false;
			trigger(true, true, true);
			return;
		}
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (TileTriggerState) state;
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

}

class TileTriggerState extends State {
	
	public boolean timed = false;
	public long delayTime = 0;
	public long reboundFrame = 0;
	public long responseFrame = 0;
}
