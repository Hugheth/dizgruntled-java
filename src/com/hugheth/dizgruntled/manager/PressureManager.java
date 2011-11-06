package com.hugheth.dizgruntled.manager;

import java.util.TreeMap;

import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;

/**
 * The pressure manager keeps track of how much force is being exerted on
 * tiles in the level. This is useful for knowing when to put switches down
 * 
 * @author hugheth
 * @version alpha-2
 * @since alpha-2
 * 
 * @see com.hugheth.dizgruntled.logic.system.StatefulLogic
 * @see com.hugheth.dizgruntled.manager.system.TimeManager
 * @see com.hugheth.dizgruntled.Level.Level
 */
public class PressureManager extends Manager {

	private PressureManagerState _state;
	
	public PressureManager(Level level) {
		super(level);
		
		_state = new PressureManagerState();

	}

	// Pressure tile
	public void tilePressure(Pair xy) {
		
		if (_state.pressure.containsKey(xy.toString()))
			_state.pressure.put(xy.toString(), (byte) (_state.pressure.get(xy.toString()) + 1));
		else
			_state.pressure.put(xy.toString(), (byte) 1);
	}
	public void tileDepressure(Pair xy) {
		
		// Make sure key is immutable
		xy = xy.clone();
		
		if (_state.pressure.containsKey(xy)) {
			_state.pressure.put(xy.toString(), (byte) Math.max(0, _state.pressure.get(xy.toString()) - 1));
		} else {
			_state.pressure.put(xy.toString(), (byte) 0);
		}
	}
	// Get pressure
	public byte getTilePressureAt(Pair xy) {
		
		if (_state.pressure.containsKey(xy.toString())){
			return (byte) _state.pressure.get(xy.toString());
		} else
			return (byte) 0;
	}

	@Override
	public String getName() {
		return "pressure";
	}

	@Override
	public State stateSave() {
		return _state.Apply();
	}

	@Override
	public void stateRestore(State state) {
		_state = (PressureManagerState) state;
	}

}

class PressureManagerState extends State {

	public TreeMap<String, Byte> pressure = new TreeMap<String, Byte>();	
	
	@Override
	public PressureManagerState clone() throws CloneNotSupportedException {
		
		PressureManagerState state = (PressureManagerState) super.clone();
		
		// Reset pressure
		state.pressure = new TreeMap<String, Byte>();
		state.pressure.putAll(pressure);
		
		return state;
	}
}