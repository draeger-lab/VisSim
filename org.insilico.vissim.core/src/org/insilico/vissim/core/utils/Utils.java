package org.insilico.vissim.core.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utils {
	
	/**
	 * Parses XML file and returns searched element or null
	 * 
	 * @param dest absolute path to XML file
	 * @param id id of an element
	 * @return DOM {@link Element} or null
	 */
	public Element getElement(String dest, String id) {
		Document doc = parseXML(dest);
		return doc.getElementById(id);
	}
	
	/**
	 * Parses XML file and returns searched element or null
	 * 
	 * @param dest absolute path to XML file
	 * @param id id of an element
	 * @return DOM {@link NodeList} or null
	 */
	public NodeList getElements(String dest, String id) {
		Document doc = parseXML(dest);
		return doc.getElementsByTagName(id);
	}

	/**
	 * Parses XML file and returns DOM {@link Document} or null
	 * 
	 * @param dest absolute path to file
	 * */
	private Document parseXML(String dest) {
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}
