package net.nooj4nlp.cmd.app;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import net.nooj4nlp.engine.Dic;
import net.nooj4nlp.gui.main.Launcher;

public class StaticInitialization {
	private static final String LOG_FILE = "log.txt";

	public static void initialize() throws IOException {
		initializeLogFile();
	}
	
	public static void initializeLogFile() throws IOException {
		Dic.LogFileName = LOG_FILE;
		
		File file = new File(Dic.LogFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		Date today = new Date();
		Dic.writeLogInit("NooJ "
				+ Launcher.nooJVersion
				+ ", "
				+ DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, java.util.Locale.ENGLISH).format(
						today));
	}
}
