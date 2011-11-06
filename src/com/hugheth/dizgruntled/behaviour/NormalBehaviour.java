package com.hugheth.dizgruntled.behaviour;

import com.hugheth.dizgruntled.action.PickupAction;
import com.hugheth.dizgruntled.ai.NormalTilePolicy;
import com.hugheth.dizgruntled.ai.TilePolicy;
import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.PickupLogic;

public class NormalBehaviour extends Behaviour {

	private TilePolicy _policy;
	
	public NormalBehaviour(Grunt grunt) {
		super(grunt);
		_policy = new NormalTilePolicy(_grunt.getLevel());
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub

	}
	
	public String getName() {
		return "normal";
	}
	
	@Override
	public boolean tileAlign() {
		
		// Check for pickup
		PickupLogic pickup = _grunt.getLevel().getCellManager().getPickupAt(_grunt.getTile());
		
		if (pickup != null) {
			
			_grunt.getAction().cancel();
			_grunt.setAction(new PickupAction(pickup));
			
			return false;
		}
		
		if (!super.tileAlign()) return false;
		
		return true;
	}

	@Override
	public TilePolicy getTilePolicy() {
		return _policy;
	}
}
