package com.hugheth.dizgruntled.level;

import java.util.Set;
import java.util.TreeMap;

/**
 * State is a class that Stateful objects use to save their
 * logical-time dependent state when it is saved and restored.
 * The apply function is called to return a snapshot of
 * the state that is a logical clone of the state at the
 * time that the apply function is called.  
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.level.Stateful
 *
 */
public abstract class State implements Cloneable {
	
	private boolean _applied = false;
	
	protected TreeMap<String, State> _children;
	
	public State Apply() {
		try {
			State state = (State) this.clone();
			// Clear children
			state._children = new TreeMap<String, State>(); 
			state._applied = true;
			return state;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	};
	
	public final boolean isApplied() {
		return _applied;
	}
	
	/**
	 * Add a stateful child's applied state to this applied state. Can only
	 * be called on a state that has been applied i.e. fixed.
	 *   
	 * @param child The stateful object that is a child of the stateful object that uses this state object
	 */
	public void applyChild(Stateful child) {
		
		State state = child.stateSave();
		
		if (_children == null) _children = new TreeMap<String, State>();
		
		_children.put(child.getName(), state);		
	}
	
	public Set<String> getChildren() {
		
		if (_children == null) return null;
		
		return _children.keySet();
	}
	
	public State getChild(String key) {
		
		if (_children == null || !_children.containsKey(key)) return null;
		
		try {
			return _children.get(key).clone();
		} catch (Exception e) {
			return null;
		}
	}
	
	public State clone() throws CloneNotSupportedException {
		
		State state = (State) super.clone();
				
		if (_children != null) {
		
			// Create the child array
			state._children = new TreeMap<String, State>();
			
			// Clone child states
			for (String key: _children.keySet()) {
				
				if (_children.get(key) == null)
					state._children.put(key, null);
				else
					state._children.put(key, _children.get(key));
			}
		}
		
		return state;
	}
	
}
