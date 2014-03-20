package net.nooj4nlp.cmd;

import java.io.IOException;

import net.nooj4nlp.cmd.app.StaticInitialization;

import org.junit.BeforeClass;

public class NoojTest {
	
	@BeforeClass
	public static void initialize() throws IOException {
		StaticInitialization.initialize();
	}

}
