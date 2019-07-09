package org.magic.api.scripts.impl;

import org.magic.api.interfaces.abstracts.AbstractJSR223MTGScript;


public class PythonScript extends AbstractJSR223MTGScript {

	@Override
	public String getExtension() {
		return "py";
	}

	@Override
	public String getName() {
		return "Python";
	}

	@Override
	public String getEngineName() {
		return "python";
	}
}
