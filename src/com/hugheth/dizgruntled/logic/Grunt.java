package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;
import java.util.TreeSet;

import com.hugheth.dizgruntled.Attribute;
import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.action.Action;
import com.hugheth.dizgruntled.action.EntranceAction;
import com.hugheth.dizgruntled.action.IdleAction;
import com.hugheth.dizgruntled.behaviour.Behaviour;
import com.hugheth.dizgruntled.behaviour.NormalBehaviour;
import com.hugheth.dizgruntled.component.GruntBodyComponent;
import com.hugheth.dizgruntled.component.HealthBarComponent;
import com.hugheth.dizgruntled.component.SelectRingComponent;
import com.hugheth.dizgruntled.level.Direction;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.State;
import com.hugheth.dizgruntled.modifier.Modifier;
import com.hugheth.dizgruntled.player.Player;
import com.hugheth.dizgruntled.task.Task;

public class Grunt extends StatefulLogic implements CellLogic {

	private SimpleGruntState _state;
	
	public Grunt(Level level) {
		super(level);
		
		_state = new SimpleGruntState();
		
		// Ability attributes
		addAttribute("selectable", new Attribute(true));
		addAttribute("movable", new Attribute(true));
		addAttribute("walkable", new Attribute(true));
		// State attributes
		addAttribute("selected", new Attribute(false));
		addAttribute("moving", new Attribute(false));
		// Body attributes
		addAttribute("direction", new Attribute(Direction.South));
		addAttribute("speed", new Attribute(600));
		addAttribute("health", new Attribute(100));
		addAttribute("primaryFrame", new Attribute(0));
		addAttribute("cellName", new Attribute("grunt"));
	}
	
	// Grunt handlers
	protected Player _player;
	protected Action _action;
	protected Behaviour _behaviour;
	protected Task _task;
	
	// Grunt graphical components
	public final GruntBodyComponent body = new GruntBodyComponent();
	public final HealthBarComponent healthBar = new HealthBarComponent();
	public final SelectRingComponent selectRing = new SelectRingComponent();
	
	@Override
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		try {
			// Force the grunt to align to the grid
			xy = new Pair((int) Math.round(xy.x() / 32.0) * 32, (int) Math.round(xy.y() / 32.0) * 32);
			
			super.init(xy, props);
			
		} catch (Exception e) {
			throw new GruntException("The Grunt object had problems initializing", e);
		}
		
		// Set priority
		addAttribute("priority", new Attribute(_screen.y() * 100));
		
		try {
			_player = _level.getPlayer(Integer.parseInt(props.get("team")));
			_player.addGrunt(this);
			
		} catch (Exception e) {
			// Bad Grunt team
			throw new GruntException("The Grunt object at (" + xy + ") doesn't not have a valid team.", e);

		}
		
		// Add controller
		_behaviour = new NormalBehaviour(this);
		// Attach body
		attachComponent(body, 100);
		// Action
		setAction(new EntranceAction());
	}
	
	// Attribute methods
	public Attribute attribute(String name) {
		return _state.attributes.get(name).last();
	}
	public Attribute attributeBase(String name) {
		return _state.attributes.get(name).first();
	}
	public Attribute[] getAttributes() {
		return (Attribute[]) _state.attributes.values().toArray();
	}
	public void addAttribute(String name, Attribute a) {
		
		if (_state.attributes.containsKey(name)) {
			_state.attributes.get(name).add(a);
			
		} else {
			TreeSet<Attribute> set = new TreeSet<Attribute>();
			set.add(a);
			_state.attributes.put(name, set);
		}
	}
	
	// Modifiers
	public void addModifier(String name, Attribute a, int priority) {
		a.priority = priority;
		// Add
		addAttribute(name, a);
	}
	public void removeModifier(String name, Attribute a) {
		_state.attributes.get(name).remove(a);
	}
	
	public Player getPlayer() {
		return _player;
	}
	
	public void updatePriority() {
		 attributeBase("priority").value = getScreen().y() * 100;
		 _priority = (Integer) attribute("priority").value;
		_level.getLogicManager().sort();
	}
	
	public void setAction(Action action) {
		// Detach old component
		if (_action != null) {
			
			if (_action.isActive())
				_action.end();
			
			detachComponent(_action);
		}
		// Attach new
		_action = action;
		attachComponent(action, 0);
		action.start();
	}
	public Action getAction() {
		return _action;
	}
	public void setBehaviour(Behaviour c) {
		// Detach old component
		if (_behaviour != null)
			_behaviour.exit();
		// Attach new
		_behaviour = c;
		c.setup();
	}
	public Behaviour getBehaviour() {
		return _behaviour;
	}
	public void setTask(Task t) {	
		if (_task != null) {
			_task.cancel();
			detachComponent(_task);
		}
		
		_task = t;
		
		if (_task != null) {
			t.setup();
			attachComponent(_task, -10);
		}
	}
	public Task getTask() {
		return _task;
	}
	
	// Get the level
	public Level getLevel() {
		return _level;
	}
	
	@Override
	public String getCellName() {
		return (String) attribute("cellName").value;
	}
	
	// Get the direction
	public Direction getDirection() {
		return (Direction) attribute("direction").value;
	}

	@Override
	public int getSpeed() {
		if ((Boolean) attribute("moving").value)
			return (Integer) attribute("speed").value;
		return 0;
	}
	@Override
	public long getKeyFrame() {
		return (Long) attribute("primaryFrame").value;
	}
	@Override
	public State stateSave() {
		
		_state.tile = _tile.clone();
		
		return _state.Apply();
	}
	@Override
	public void stateRestore(State state) {
		
		_state = (SimpleGruntState) state;
		
		_action.cancel();
		
		if (_task != null)
			_task.cancel();
		
		_tile = _state.tile;
		
		setAction(new IdleAction());
		
	}

	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class SimpleGruntState extends State {
	
	public Pair tile;
	// Initialise data structures
	public TreeMap<String, TreeSet<Attribute>> attributes = new TreeMap<String, TreeSet<Attribute>>();
	public TreeSet<Modifier> modifiers = new TreeSet<Modifier>();
	
	public SimpleGruntState clone() throws CloneNotSupportedException {
		
		SimpleGruntState state = (SimpleGruntState) super.clone();
		
		state.tile = tile.clone();
		
		// Clear attributes
		state.attributes = new TreeMap<String, TreeSet<Attribute>>();
		for (String key: attributes.keySet()) {
			
			TreeSet<Attribute> store = new TreeSet<Attribute>();
			
			for (Attribute attr: attributes.get(key))
				store.add((Attribute) attr.clone());
			
			state.attributes.put(key, store);
		}
		
		state.modifiers = new TreeSet<Modifier>();
		state.modifiers.addAll(modifiers);
		
		return state;
	}

}