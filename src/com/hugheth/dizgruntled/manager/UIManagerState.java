package com.hugheth.dizgruntled.manager;

import com.hugheth.dizgruntled.level.State;

public class UIManagerState extends State {
	
	public int score = 0;
	public int purple = 0;
	public int blue = 0;
	public int green = 0;
	public boolean bronze = false;
	public boolean silver = false;
	public boolean gold = false;
	
	public UIManagerState clone() throws CloneNotSupportedException {
		return (UIManagerState) super.clone();				
	}
}
