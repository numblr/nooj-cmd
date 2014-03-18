package net.nooj4nlp.cmd;

public final class Encoding {
	public static enum EncodingType {
		UNICODE,
		RAW_TEXT,
		RTF,
		WORD,
		HTML,
		PDF,
		DEFAULT;
	}
	
	private final String code;
	private final EncodingType type;
	
	public Encoding (String code, EncodingType type) {
		this.code = code;
		this.type = type;
	}
	
	public String getCode() {
		return code;
	}
	
	public EncodingType getType() {
		return type;
	}
	
	public int getTypeOrdinal() {
		return type.ordinal();
	}
	
	public String getTypeName() {
		return type.name();
	}
}
	
//	private void encoding() {
//		// determine type of encoding, selected code and encoding name
//		int encodingType;
//		String encodingCode = null;
//		String encodingName = "";
//		if (dialog.getRdbtnAsciiUnicode().isSelected()) {
//			encodingType = 1;
//			encodingCode = null;
//			encodingName = "Default";
//		}
//		// if other raw text formats are selected, get selected encoding from
//		// the list
//		else if (TextCorpusDialog.getRdbtnOtherRawText().isSelected()) {
//			encodingType = 2;
//			String fmt = TextCorpusDialog.getListFFormats().getSelectedValue()
//					.toString();
//
//			try {
//				Charset enc = Charset
//						.forName(fmt.substring(0, fmt.indexOf('[')));
//				encodingCode = enc.name();
//				encodingName = enc.displayName();
//			} catch (Exception e1) {
//				// TODO: show code number instead of encodingType in error
//				// message!
//				JOptionPane
//						.showMessageDialog(Launcher.getDesktopPane(),
//								e1.toString(), Constants.CANNOT_HANDLE_ENCODING
//										+ encodingType,
//								JOptionPane.INFORMATION_MESSAGE);
//				encodingCode = null;
//				encodingName = "Default";
//			}
//		} else if (dialog.getRdbtnRichTextFormat().isSelected()) {
//			encodingType = 3;
//			encodingName = "RTF";
//		} else if (dialog.getRdbtndoc().isSelected()) {
//			encodingType = 4;
//			encodingName = "WORD";
//		} else if (dialog.getRdbtnHtmlPage().isSelected()) {
//			encodingType = 5;
//			encodingName = "HTML";
//		} else if (dialog.getRdbtnPdfDocument().isSelected()) {
//			encodingType = 6;
//			encodingName = "PDF";
//		} else {
//			encodingType = 7;
//			encodingCode = null;
//			encodingName = "Default";
//		}
