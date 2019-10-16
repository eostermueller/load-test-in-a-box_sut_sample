package com.github.eostermueller.tjp2.xslt;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.xml.sax.InputSource;

import com.github.eostermueller.snail4j.workload.annotations.ProcessingUnit;
import com.github.eostermueller.snail4j.workload.annotations.UserInterfaceDescription;


/**
 * 
 * @author erikostermueller
 *
 */
public class XsltProcessor {
	KeyedPooledObjectFactory keyedPooledObjectFactory = null;
	KeyedObjectPool<String,Transformer> keyedPool = null;
	private static String XSLT_FILES = 
			"           xsl.root/01/personnel.xml;\n" + 
			"           xsl.root/01/simple.xsl;         \n" + 
			"           xsl.root/02/book.xml;           \n" + 
			"           xsl.root/02/to-html.xsl;        \n" + 
			"           xsl.root/03/sales.xml;          \n" + 
			"           xsl.root/03/to-html.xsl;        \n" + 
			"           xsl.root/04/sales.xml;          \n" + 
			"           xsl.root/04/to-svg.xsl;         \n" + 
			"           xsl.root/05/foo.xml;            \n" + 
			"           xsl.root/05/foo.xsl;            \n" + 
			"           xsl.root/06/birds.xml;          \n" + 
			"           xsl.root/06/birds.xsl;\n"; 

	public XsltRepos getRepos() {
		return repos;
	}
	XsltRepos repos = null;
	public String getHumanReadableFileList() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
        for(XsltRepo repo : repos.getRepos() ) {
        	sb.append("Repo [" + repo.getPath() + "] xslt [" + repo.getXslt().getFullName() + "]\n");
        	for(XmlFile xml : repo.getXmlFiles()) {
            	sb.append("   file [" + xml.getFullName() + "]\n");
        	}
        }
        return sb.toString();
	}
	public XsltProcessor() {
		repos = new XsltRepos(XSLT_FILES);
        this.keyedPooledObjectFactory = new KeyedTransformerFactory(this.repos);
        
        //To configure min/max and other pool parameters, 
        //create GenericKeyedObjectPoolConfig and pass as 2nd parm here:
        this.keyedPool = new GenericKeyedObjectPool<String,Transformer>(this.keyedPooledObjectFactory);
	}
	
	@ProcessingUnit(
			useCase = "xsltTransform", 
			value = {@UserInterfaceDescription("Reinstantiate Transformer every time")}
			)
	public String unPooledTransformerXslt() {
		StringBuilder sb = new StringBuilder();
        for(XsltRepo repo : repos.getRepos() ) {
            TransformerFactory factory = SAXTransformerFactory.newInstance();
            Transformer transformer = null;
            try {
    			transformer = factory.newTransformer(new StreamSource( repo.getXslt().getFileContents() ));
            	for(XmlFile xml : repo.getXmlFiles()) {
            		SAXSource saxSource = new SAXSource(new InputSource( xml.getFileContents() ));
                    StringWriter writer = new StringWriter();
                    
        			transformer.transform(saxSource, new StreamResult(writer));
        			sb.append("#Repo:" + repo.getPath() + " XMl file:" + xml.getFullName() + " XSL file: " + repo.getXslt().getFullName() + "\n");
        			sb.append(writer.toString());
        			sb.append("\n");
            	}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				sb.append(sw.toString());
			}
            
        }
        return sb.toString();
	}
	@ProcessingUnit(
			useCase = "xsltTransform", 
			value = {@UserInterfaceDescription("Reuse same Transformers from pool")}
			)
	public String pooledTransformerXslt() {
		StringBuilder sb = new StringBuilder();
        for(XsltRepo repo : repos.getRepos() ) {

        	for(XmlFile xml : repo.getXmlFiles()) {
                //SAXSource saxSource = new SAXSource(new InputSource( new StringReader(xml.getTextFromFile()) ));
        		SAXSource saxSource = new SAXSource(new InputSource( xml.getFileContents() ));
                StringWriter writer = new StringWriter();
                
                Transformer transformer = null;
    			String poolKey = repo.getXslt().getFullName();
				try {
					transformer = keyedPool.borrowObject( poolKey );
	    			transformer.transform(saxSource, new StreamResult(writer));
	    			sb.append("#Repo:" + repo.getPath() + " XMl file:" + xml.getFullName() + " XSL file: " + repo.getXslt().getFullName() + "\n");
	    			sb.append(writer.toString());
	    			sb.append("\n");
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					sb.append(sw.toString());
				} finally {
					try {
						keyedPool.returnObject(poolKey, transformer);
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						sb.append(sw.toString());
					}
				}
        	}
        }
        return sb.toString();
	}
}
