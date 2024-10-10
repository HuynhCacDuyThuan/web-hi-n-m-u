package com.example.demo.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLParser {
	private static final Logger LOGGER = Logger.getLogger(XMLParser.class.getName());
	String xml = "https://vnexpress.net/rss/suc-khoe.rss";

	public List<NewsItem> parseXML() {
		List<NewsItem> results = new ArrayList<>();
		Document document = getDocument(xml);

		if (document != null) {
			NodeList nodeList = document.getElementsByTagName("item");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				String title = getValue(element, "title");
				String link = getValue(element, "link");
				String imageUrl = getImageUrl(element);
				results.add(new NewsItem(title, link, imageUrl));
			}
		} else {
			// Ghi log nếu document là null
			LOGGER.severe("Document is null after parsing.");
		}
		return results;
	}

	private String getImageUrl(Element item) {
		NodeList nodes = item.getElementsByTagName("enclosure");
		if (nodes.getLength() > 0) {
			Element enclosure = (Element) nodes.item(0);
			return enclosure.getAttribute("url");
		}
		return ""; // Trả về chuỗi rỗng nếu không có hình ảnh
	}

	private Document getDocument(String url) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = factory.newDocumentBuilder();
			// Sử dụng URL để lấy dữ liệu
			InputStream inputStream = new URL(url).openStream();
			document = db.parse(inputStream);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			LOGGER.log(Level.SEVERE, "Error: ", e);
		}
		return document;
	}

	private String getValue(Element item, String name) {
		NodeList nodes = item.getElementsByTagName(name);
		return getTextNodeValue(nodes.item(0));
	}

	private String getTextNodeValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}
}
