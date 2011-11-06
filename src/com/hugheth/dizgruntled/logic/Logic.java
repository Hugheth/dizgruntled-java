package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;
import java.util.TreeSet;

import org.newdawn.slick.Graphics;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.component.Component;
import com.hugheth.dizgruntled.level.Level;

public abstract class Logic extends Component {
	
	public static Logic fromType(Level level, String type) {
		try {
			@SuppressWarnings("unchecked")
			Class<Logic> cls = (Class<Logic>) Class.forName("com.hugheth.dizgruntled.logic." + type);
			return (Logic) cls.getConstructors()[0].newInstance(level);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected Level _level;
	
	protected Pair _screen;
	protected Pair _tile;

	// Components
	private TreeSet<Component> components;
	// Temporary components
	private TreeSet<Component> toAdd;
	private TreeSet<Component> toRemove;

	public Logic(Level level) {
		super();
		_level = level;
		components = new TreeSet<Component>();
		toAdd = new TreeSet<Component>();
		toRemove = new TreeSet<Component>();
	}

	// Component management
	public void attachComponent(Component component, int priority) {
		component.setAttachment(this, priority);
		toAdd.add(component);
	}
	
	public void detachComponent(Component component) {
		toRemove.add(component);
	}
	
	public abstract EditLogic getEditLogic();
	
	// Initialise
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		// Set positions
		_screen = xy.clone();
		_tile = new Pair(Math.round(xy.x() / 32.0f), Math.round(xy.y() / 32.0f));
	}

	public void initAligned(Pair xy, TreeMap<String, String> props) {
		// Force the grunt to align to the grid
		_tile = new Pair((int) Math.round(xy.x() / 32.0), (int) Math.round(xy.y() / 32.0));
		_screen = new Pair(_tile.x() * 32, _tile.y() * 32);
	}

	// Getters and setters
	public Pair getScreen() {
		return _screen;
	}
	
	public Pair getTile() {
		return _tile;
	}

	public void update(long currentFrame) {
		for (Component c : components)
			c.update(currentFrame);
		// Remove old elements
		for (Component c : toRemove)
			components.remove(c);
		// Add new elements
		for (Component c : toAdd)
			components.add(c);
		// Clean up
		toAdd.clear();
		toRemove.clear();
	}

	public void render(Graphics graphics, long time) {
		for (Component component : components)
			component.render(graphics, time);
	}
}
