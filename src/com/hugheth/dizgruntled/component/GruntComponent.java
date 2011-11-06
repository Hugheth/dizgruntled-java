package com.hugheth.dizgruntled.component;

import com.hugheth.dizgruntled.logic.Grunt;
import com.hugheth.dizgruntled.logic.Logic;

public abstract class GruntComponent extends Component {
	
	protected Grunt _grunt;
	
	public void setAttachment(Logic e, int p) {
		super.setAttachment(e, p);
		_grunt = (Grunt) e;
	}
}
