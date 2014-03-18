package net.nooj4nlp.cmd;

import java.io.File;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

public final class TextImport {
	private static final String[] EMPTY_XML_NODES = new String[0];
	
	private final Language language;
	private final TextLoader textLoader;
	private final String delimiter;
	
	public TextImport(Language language,
			Encoding encoding,
			String delimiter) {
		this(language, delimiter, new TextLoader(encoding, language));
	}
	
	public TextImport(Language language,
			String delimiter,
			TextLoader textLoader) {
		this.language = language;
		this.delimiter = delimiter;
		this.textLoader = textLoader;
	}

	public Ntext fromFile(File file) {
		Ntext nText = new Ntext(language.isoName, delimiter, EMPTY_XML_NODES);

		nText.buffer = textLoader.load(file);
		
		Engine engine = new Engine(new RefObject<Language>(nText.Lan),
				"", "", "", false, null, false, null);

		String errorMessage = engine.delimitTextUnits(nText);
		if (!errorMessage.isEmpty())
		{
			throw new ImportException(file, errorMessage);
		}
		
		return nText;
	}
	
	public static class ImportException extends FileException {
		private static final long serialVersionUID = 1L;

		ImportException(File file, String errorMessage) {
			super(file, errorMessage);
		}
	}
}

//Exception
//JOptionPane.showMessageDialog(Launcher.getDesktopPane(), errorMessage, Constants.NOOJ_ERROR + " "
//		+ errorMessage, JOptionPane.INFORMATION_MESSAGE);
//// if flag for XML text nodes is set to true, delimit XML...
//if (ImportTextActionListener.xmlButtonChecked)
//{
//	errorMessage = engine.delimitXmlTextUnitsAndImportXmlTags(null, nText);
//	if (!errorMessage.equals(""))
//	{
//		JOptionPane.showMessageDialog(Launcher.getDesktopPane(), errorMessage, Constants.NOOJ_ERROR + " "
//				+ errorMessage, JOptionPane.INFORMATION_MESSAGE);
//		return;
//	}
//}