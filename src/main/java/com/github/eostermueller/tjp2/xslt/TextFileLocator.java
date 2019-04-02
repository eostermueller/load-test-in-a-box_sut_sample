package com.github.eostermueller.tjp2.xslt;

import javax.xml.transform.stream.StreamSource;

public interface TextFileLocator {
	public StreamSource getTextFileForThisFolder(String key);
}
