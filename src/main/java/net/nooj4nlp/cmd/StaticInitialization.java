package net.nooj4nlp.cmd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.nooj4nlp.engine.Dic;
import net.nooj4nlp.gui.main.Launcher;

public final class StaticInitialization {
	public static void initialize(Path logFile) throws IOException {
		initializeLogFile(logFile.toAbsolutePath());
	}
	
	private static void initializeLogFile(Path logFile) throws IOException {
		Dic.LogFileName = logFile.toString();
		
		File file = logFile.toFile();
		if (!file.exists()) {
			file.createNewFile();
		}
		
		Date today = new Date();
		Dic.writeLogInit("NooJ "
				+ Launcher.nooJVersion
				+ ", "
				+ DateFormat.getDateTimeInstance(DateFormat.SHORT,
						DateFormat.MEDIUM,
						Locale.ENGLISH)
					.format(today));
	}
}
