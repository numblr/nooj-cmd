package net.nooj4nlp.cmd.io;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Encoding {
	public static enum FileType {
		UNICODE_TEXT,
		RAW_TEXT,
		RTF,
		WORD,
		HTML,
		PDF;
	}
	
	private final String encoding;
	private final FileType type;
	
	public Encoding(String encoding, FileType type) {
		this.encoding = encoding;
		this.type = checkNotNull(type);
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public FileType getFileType() {
		return type;
	}
	
	public int getFileTypeOrdinal() {
		return type.ordinal() + 1;
	}
	
	public String getFileTypeName() {
		return type.name();
	}
}