package com.me4502.FractalMaker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.me4502.MAPL.util.config.YAMLConfiguration;
import com.me4502.MAPL.util.yaml.YAMLProcessor;

public class FractalConfig extends YAMLConfiguration {

	public FractalConfig(YAMLProcessor config) {
		super(config);
	}

	@Override
	public void load() {

		try {
			config.load();
		} catch (IOException e) {
			Logger.getLogger("FractalMaker").log(Level.SEVERE, "Error loading FractalMaker configuration", e);
		}

		config.setWriteDefaults(true);

		super.load();
	}

	@Override
	public void save() {

		super.save();
	}
}