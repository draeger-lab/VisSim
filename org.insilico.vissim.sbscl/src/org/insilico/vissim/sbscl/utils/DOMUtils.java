package org.insilico.vissim.sbscl.utils;

import java.util.AbstractList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMUtils {

	/**
	 * Parses XML file and returns searched element or null
	 * 
	 * @param dest absolute path to XML file
	 * @param id   id of an element
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
	 * @param id   id of an element
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
	 */
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

	/**
	 * Returns a list of unit definitions
	 * 
	 * @return {@link NodeList} or null
	 */
	public NodeList getListOfUnitDefinitions(String dest) {
		return getElements(dest, "listOfUnitDefinitions");
	}

	/**
	 * Returns a map of all unit definitions
	 * */
	public HashMap<String, String> rearrangeUnitDefinitions(NodeList uDefinitions) {
		List<Node> asList = new XmlUtil().asList(uDefinitions);
		HashMap<String, String> definitions = new HashMap<String, String>();
		for (Node node : asList) {
			NamedNodeMap attributes = node.getAttributes();
			// TODO: get needed attribute
			definitions.put(attributes.getNamedItem("id").toString(), attributes.getNamedItem("name").toString());
		}
		return definitions;
	}
	
	/**
	 * Returns a map of all unit definitions
	 * */
	public HashMap<String, String> getUnitDefinitions(String dest) {
		return rearrangeUnitDefinitions(getListOfUnitDefinitions(dest));
	}

	/**
	 * Helper class for iterable {@link NodeList} initialization
	 * */
	public final class XmlUtil {
		private XmlUtil() {
			// prevent empty initialization
		}

		/**
		 * Returns iterable version of {@link NodeList}
		 * */
		public List<Node> asList(NodeList list) {
			return list.getLength() == 0 ? Collections.<Node>emptyList() : new IterableNodeList(list);
		}

		/**
		 * Wrapper class for itarable {@link NodeList}
		 * */
		final class IterableNodeList extends AbstractList<Node> implements RandomAccess {
			private final NodeList list;

			IterableNodeList(NodeList list) {
				this.list = list;
			}

			/**
			 * Get item with provided index
			 * */
			public Node get(int index) {
				return list.item(index);
			}

			/**
			 * Get size of a list
			 * */
			public int size() {
				return list.getLength();
			}
		}
	}
}
