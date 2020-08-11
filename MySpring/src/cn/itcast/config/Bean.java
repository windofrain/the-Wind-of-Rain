package cn.itcast.config;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.bean.Property;

public class Bean {
   private String name;
   private String className;
   private List<Property> properties=new ArrayList<>();
/**
 * @return the name
 */
public String getName() {
	return name;
}
/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}
/**
 * @return the className
 */
public String getClassName() {
	return className;
}
/**
 * @param className the className to set
 */
public void setClassName(String className) {
	this.className = className;
}
/**
 * @return the properties
 */
public List<Property> getProperties() {
	return properties;
}
/**
 * @param properties the properties to set
 */
public void setProperties(List<Property> properties) {
	this.properties = properties;
}
}
