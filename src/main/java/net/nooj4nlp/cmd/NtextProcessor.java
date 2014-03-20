package net.nooj4nlp.cmd;

import net.nooj4nlp.engine.Ntext;

public interface NtextProcessor {
	
	/**
	 * Performs analysis on an {@link Ntext} object.
	 * 
	 * @param ntext the Ntext object to be analysed. The instance may be
	 * 				modified during the analysis.
	 */
	void process(Ntext ntext);
	
}
