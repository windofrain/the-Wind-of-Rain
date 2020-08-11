package cn.itcast.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {
  public static Method getWriteMethod(Object beanObj,String name) {
	  Method method=null;
	  BeanInfo info=null;
	  	try {
			 info=Introspector.getBeanInfo(beanObj.getClass());
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  	PropertyDescriptor[] pds=info.getPropertyDescriptors();
	  	if(pds!=null) {
	  		for(PropertyDescriptor pd:pds) {
	  			if(name.equals(pd.getName())) {
	  				method=pd.getWriteMethod();
	  			}
	  		}
	  		
	  	}
	  	if(method==null)throw new RuntimeException("检查"+name+"是否有set方法");
	  	return method;
  }
}
