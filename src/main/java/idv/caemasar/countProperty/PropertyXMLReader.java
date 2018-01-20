package idv.caemasar.countProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PropertyXMLReader {
	
	private String PROPERTYXML = "MetaModel_1_7_0.xml";
	private String PROPERTYNAME = "dt";
	private static final Logger log = Logger.getLogger(PropertyXMLReader.class);

	private Document doc;
	private Set<String> pointAttrSet = new HashSet<String>();
	private long count = 0;

	private PropertyXMLReader(String PROPERTYXML, String PROPERTYNAME) {
		this.PROPERTYXML = PROPERTYXML;
		this.PROPERTYNAME = PROPERTYNAME;
		String path = PropertyXMLReader.class.getClassLoader().getResource(PROPERTYXML).getPath();
		try {
			path = URLDecoder.decode(path, "utf-8");
			doc = readXml(path);
			init(this.PROPERTYXML, this.PROPERTYNAME);
		} catch (UnsupportedEncodingException e1) {
			log.error("utf8 encode error:", e1);
		} catch (Exception e2) {
			e2.printStackTrace();
			log.error("Initial enum data failed! By Exception:\n");
		}
	}

	private void init(String PROPERTYXML, String PROPERTYNAME) {
		Element root = doc.getRootElement();
		getNodes(root);
		log.info(this.PROPERTYNAME + "Set-->Size:::" + pointAttrSet.size());
		for (String str : pointAttrSet) {
			log.info(this.PROPERTYNAME + "Set-->List:::" + str);
		}
		log.info(this.PROPERTYNAME + "Set-->attrCount:::" + count);
	}

	@SuppressWarnings("unchecked")
	private void getNodes(Element node) {
		List<Attribute> listAttr = node.attributes();
		String name;
		String value;
		for (Attribute attr : listAttr) {
			name = attr.getName();
			if (name.equals(PROPERTYNAME)) {
				value = attr.getValue();
				pointAttrSet.add(value);
			}
			count++;

		}
		List<Element> listElement = node.elements();
		for (Element e : listElement) {
			this.getNodes(e);
		}
	}

	private Document readXml(String file) {
		Document document = null;
		SAXReader reader = new SAXReader();
		try {
			log.info("filePath:::" + file);
			document = reader.read(new File(file));
		} catch (DocumentException e) {
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(PROPERTYXML);
				log.info("filePath:::" + file + "-->error");
				log.info("filePath-->FileInputStream:::" + inputStream);
				document = reader.read(inputStream);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return document;
	}

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		log.info("请输入文件名：");
		String fileName = scanner.nextLine();
		log.info("请输入属性名：");
		String attrName = scanner.nextLine();
		PropertyXMLReader propertyXMLReader = new PropertyXMLReader(fileName, attrName);
		log.info("按任意键退出。");
		scanner.nextLine();
		System.exit(0);
	}
}
