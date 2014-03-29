package net.nooj4nlp.cmd.processing;

import net.nooj4nlp.engine.Ntext;

public interface NtextProcessor {
	
	/**
	 * Performs analysis on an {@link Ntext} object.
	 * 
	 * @param ntext the Ntext object to be analysed. The nText instance may be
	 * 				modified during the analysis.
	 */
	void process(Ntext nText);
	
}
