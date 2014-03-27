package net.nooj4nlp.cmd.app;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nooj4nlp.cmd.io.FileIO;
import net.nooj4nlp.cmd.io.LinguisticResources;
import net.nooj4nlp.cmd.processing.Ntext2Xml;
import net.nooj4nlp.cmd.processing.NtextConverter;
import net.nooj4nlp.cmd.processing.NtextProcessor;
import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TextProcessorTest {
	private static final Language ENGLISH = new Language("en");
	
	@Mock private FileIO fileIO;
	@Mock private NtextConverter inputConverter;
	@Mock private LinguisticResources resources;
	@Mock private Ntext2Xml xmlConverter;
	@Mock private NtextProcessor processor_1;
	@Mock private NtextProcessor processor_2;
	
	private List<NtextProcessor> ntextProcessors;

	@Before
	public void setupMocks() {
		initMocks(this);
		
		when(fileIO.load(any(Path.class)))
				.thenReturn("testInput");

		when(inputConverter.convert("testInput"))
				.thenReturn(createTestNtext());
		
		doAnswer(appendToNtextBuffer(1)).when(processor_1).process(any(Ntext.class));
		doAnswer(appendToNtextBuffer(2)).when(processor_2).process(any(Ntext.class));
		ntextProcessors = Lists.newArrayList(processor_1, processor_2);
		
		doAnswer(returnNtextBuffer()).when(xmlConverter).convert(any(Ntext.class));
	}

	private Ntext createTestNtext() {
		Ntext nText = new Ntext(ENGLISH.isoName);
		nText.buffer = "";
		
		return nText;
	}

	private static Answer<Object> appendToNtextBuffer(final int id) {
		return new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Ntext nText = ((Ntext)args[0]);
				nText.buffer = nText.buffer + id;
				
				return null;
			}
		};
	}
	
	private static Answer<String> returnNtextBuffer() {
		return new Answer<String>() {
			public String answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				
				return ((Ntext)args[0]).buffer;
			}
		};
	}
	
	@Test
	public void processesText() {
		TextProcessor textProcessor = new TextProcessor(fileIO,
				inputConverter,
				resources,
				ntextProcessors,
				xmlConverter);
		
		textProcessor.processFiles(ImmutableList.of(Paths.get("dir/test.txt")));

		Path currentPath = Paths.get("").toAbsolutePath();
		verify(fileIO).write("12", currentPath.resolve(Paths.get("dir/test.xml.txt")));
	}
}
