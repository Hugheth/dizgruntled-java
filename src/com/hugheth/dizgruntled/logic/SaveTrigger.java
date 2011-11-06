package com.hugheth.dizgruntled.logic;

import java.util.TreeMap;

import com.hugheth.dizgruntled.GruntException;
import com.hugheth.dizgruntled.Pair;
import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.player.HumanPlayer;

public class SaveTrigger extends Logic implements TriggerLogic {

	public SaveTrigger(Level level) {
		super(level);
	}

	private int _slot = 0;
	private boolean _silent = false;
	private String[] _groups;
	private boolean _saving = false;
	
	@Override
	public EditLogic getEditLogic() {
		// TODO Auto-generated method stub
		return null;
	}

	// Initialise
	public void init(Pair xy, TreeMap<String, String> props) throws GruntException {
		super.init(xy, props);
		
		if (props.containsKey("slot"))
			_slot = Integer.parseInt(props.get("slot"));
		
		if (props.containsKey("group"))
			_groups = props.get("group").split(" ");
		else
			_groups = new String[] {"init"};
		
		if (props.containsKey("silent"))
			_silent = Boolean.parseBoolean(props.get("silent"));
		
		// Iterate through groups and add the Trigger to the LogicManager
		for (String group: _groups) {
			_level.getTriggerManager().addTriggerToGroup(this, group);
		}
	}
	
	@Override
	public void trigger() {
		// Flash
		if (!_silent) {
			_level.playSound("camera.ogg", 40);
			_level.getUIManager().flash();
		}			
		
		_saving = true;

		// TODO: Human player not player 0
		_level.getPlayer(0).stopGrunts();
		((HumanPlayer)_level.getPlayer(0)).deselectGrunts();
	}
	
	@Override
	public void update(long currentFrame) {
		if (_saving) {
			_saving = false;
			
			// Save
			_level.getSnapshotManager().save(_slot);
		}
	}

}
