package cn.itcast.config.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.itcast.bean.Property;
import cn.itcast.config.Bean;

public class ConfigManager {
  public static Map<String, Bean> getConfig(String path){
	  Map<String,Bean> map=new HashMap<>();
	  SAXReader reader=new SAXReader();
	  InputStream is = ConfigManager.class.getResourceAsStream(path);
	  Document doc=null;
	  try {
		 doc =reader.read(is);
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 String xpath="//bean";
	 List list=doc.selectNodes(xpath);
	 if(list!=null) {
		 Iterator it=list.iterator();
		 while(it.hasNext()) {
			 Bean bean=new Bean();
			 Element e=(Element)it.next();
			 String name=e.attributeValue("name");
			 String className=e.attributeValue("class");
			 bean.setName(name);
			 bean.setClassName(className);
			 List<Element> childen=e.elements("property");
			 if(childen!=null) {
				 for(Element child: childen) {
					 Property prop=new Property();
					 String pName=child.attributeValue("name");
					 String pValue=child.attributeValue("value");
					 String pRef=child.attributeValue("ref");
					 prop.setName(pName);
					 prop.setValue(pValue);
					 prop.setRef(pRef);
					 bean.getProperties().add(prop); 
				 }
			 }
			 map.put(name, bean);
		 }
	 }
	  return map;
  }
}
