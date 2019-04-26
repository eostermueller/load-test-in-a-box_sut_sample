package com.github.eostermueller.tjp2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.eostermueller.tjp2.xslt.XsltProcessor;
import com.github.eostermueller.tjp2.xslt.XsltRepo;

/**
 * Need to turn the System.out.println() into assertions.
 * ....but for now this does some basic validation that xslt works.
 * @author erikostermueller
 *
 */
public class TestXslt {

	XsltProcessor xsltProcessor = null;
	
	@Before
	public void setup() {
		xsltProcessor = new XsltProcessor();
		System.out.println(xsltProcessor.getHumanReadableFileList());
	}
	@Test
	public void readFiles() {
		for(XsltRepo repo : this.xsltProcessor.getRepos().getRepos()) {
			System.out.println(" file [" + convertStreamToString( repo.getXslt().getFileContents() ) + "]" );
		}
		
	}
	@Test
	public void testPooled() {
		String rc = this.xsltProcessor.pooledTransformerXslt();
		System.out.println(rc);
	}
	@Test
	public void testUnPooled() {
		String rc = this.xsltProcessor.unPooledTransformerXslt();
		System.out.println(rc);
	}
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
