package com.hugheth.dizgruntled.logic;

import com.hugheth.dizgruntled.level.Level;
import com.hugheth.dizgruntled.level.Stateful;

public abstract class StatefulLogic extends Logic implements Stateful {

	public StatefulLogic(Level level) {
		super(level);
	}
	
	public String getName() {
		return String.valueOf(getID());
	}
}
