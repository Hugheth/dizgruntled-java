package com.hugheth.dizgruntled.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.Logic;
import com.hugheth.dizgruntled.logic.SFX;
import com.hugheth.dizgruntled.logic.StatefulLogic;

/**
 * The Logic Manager handles the updating and rendering of in level logicz
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.system.StatefulLogic
 * @see com.hugheth.dizgruntled.manager.system.TimeManager
 * @see com.hugheth.dizgruntled.Level.Level
 */
public class LogicManager extends Manager {
	
	private LogicManagerState _state;
	
	private ArrayList<Logic> _logics;

	private ArrayList<Logic> _toRemove;
	private ArrayList<Logic> _toAdd;
	
	private boolean _sort = false;
	
	public LogicManager(Level level) {
		super(level);
				
		_state = new LogicManagerState();
		
		// Create logic list
		_logics = new ArrayList<Logic>();
		_toAdd = new ArrayList<Logic>();
		_toRemove = new ArrayList<Logic>();
	}
	
	public void renderLogics(Graphics g, long time) {
		// Iterate through logics
		for (Logic logic: _logics) {
			logic.render(g, time);
		}
		
		if (_sort) {
			Collections.sort(_logics);
			_sort = false;
		}
		
	}
	
	public void updateLogics(long currentFrame) {
		
		// Iterate through logics
		for (Logic logic: _logics)
			logic.update(currentFrame);
		
		// Remove list
		for (Logic logic: _toRemove) {
			_logics.remove(logic);
			
			if (logic instanceof StatefulLogic)
				_state.statefulLogics.remove(((StatefulLogic) logic).getName());
		}
		
		_toRemove.clear();
		
		// Add list
		for (Logic logic: _toAdd) {
			_logics.add(logic);
			
			if (logic instanceof StatefulLogic)
				_state.statefulLogics.put(((StatefulLogic) logic).getName(), (StatefulLogic) logic);
		}
		
		_toAdd.clear();
	}
	
	// Create SFX
	public void createSFX(Pair xy, String sprite) {
		
		SFX sfx = new SFX(_level);
		// Create property tree map
		TreeMap<String, String> props = new TreeMap<String, String>();
		// Add sprite
		props.put("sprite", sprite);

		try {
			// Initialize the logic
			sfx.init(xy, props);
		
			// Attach
			attachLogic(sfx);
			
			// Sort
			sort();
			
		} catch ( Exception e) {}
	}
	
	// Add logic
	public void attachLogic(Logic logic) {
		_toAdd.add(logic);
	}
	
	// Bin logic
	public void detachLogic(Logic logic) {
		_toRemove.add(logic);
	}
	
	public void sort() {
		_sort = true;
	}

	@Override
	public String getName() {
		return "logic";
	}

	@Override
	public void update(long currentFrame) {
		updateLogics(currentFrame);
	}

	@Override
	public State stateSave() {
		
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		
		// Remove all the old stateful logics from the logic array
		_logics.removeAll(_state.statefulLogics.values());
		
		_state = (LogicManagerState) state;
		
		// Add all
		_logics.addAll(_state.statefulLogics.values());
		
		// Sort the logics
		Collections.sort(_logics);
				
		// Restore children
		for (String key: state.getChildren())
			_state.statefulLogics.get(key).stateRestore(state.getChild(key));
	}
}

class LogicManagerState extends State {
	
	public TreeMap<String, StatefulLogic> statefulLogics;
	
	public LogicManagerState() {
		statefulLogics = new TreeMap<String, StatefulLogic>();
	}
	
	@Override
	public State Apply() {
		
		LogicManagerState state = (LogicManagerState) super.Apply();
		
		// Add the stateful logics as children
		for (StatefulLogic logic: statefulLogics.values())
			state.applyChild(logic);
		
		return state;
	}
	
	@Override
	public LogicManagerState clone() throws CloneNotSupportedException {
		
		LogicManagerState state = (LogicManagerState) super.clone();
		
		// Reset logic array
		state.statefulLogics = new TreeMap<String, StatefulLogic>();
		
		// Populate stateful logic array
		state.statefulLogics.putAll(statefulLogics);
		
		return state;
	}
}
