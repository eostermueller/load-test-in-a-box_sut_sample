package com.github.eostermueller.tjp2.parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;

import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;


public class EclipseEmfPooledSaxParserWorker implements Ncorv {
	private XMLParserPool parserPool = new XMLParserPoolImpl();

	private static final String XML_INPUT_FILE = "/NCORV.xml";
	private String xml;
	public BigDecimal bdHR7983;
	public BigDecimal bdHR8587;
	public BigDecimal bdHR8995;
	Map<String, Boolean> parserFeatures = new HashMap<String, Boolean>();
	Map<String, Object> parserProperties = new HashMap<String, Object>();
	
	@SuppressWarnings("resource")
	public void setup() {
		InputStream xmlStream = EclipseEmfPooledSaxParserWorker.class.getResourceAsStream(XML_INPUT_FILE);
		this.xml = new Scanner(xmlStream,"UTF-8").useDelimiter("\\A").next();
		parserFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE); //$NON-NLS-1$
		parserFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
	}
	public void parse() {

		this.bdHR7983 = new BigDecimal(0);
		this.bdHR8587  = new BigDecimal(0);
		this.bdHR8995 = new BigDecimal(0);
		
		SAXParser      saxParser = null;
        try {

  	        saxParser = parserPool.get(parserFeatures, parserProperties, Boolean.FALSE);
            NcorvSaxHandler handler   = new NcorvSaxHandler(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
            saxParser.parse( bais, handler);
            

        } catch (Throwable err) {
            err.printStackTrace ();
        } finally {
        	parserPool.release(saxParser, parserFeatures, parserProperties, Boolean.FALSE);
        }
	}
	@Override
	public BigDecimal getHR7983() {
		return bdHR7983;
	}
	@Override
	public BigDecimal getHR8587() {
		return bdHR8587;
	}
	@Override
	public BigDecimal getHR8995() {
		return bdHR8995;
	}
	@Override
	public void setHR7983(BigDecimal val) {
		bdHR7983  = val;
		
	}
	@Override
	public void setHR8587(BigDecimal val) {
		bdHR8587 = val;
		
	}
	@Override
	public void setHR8995(BigDecimal val) {
		this.bdHR8995 = val;
	}


}
