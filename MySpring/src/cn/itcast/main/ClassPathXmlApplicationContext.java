package cn.itcast.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.itcast.bean.Property;
import cn.itcast.config.Bean;
import cn.itcast.config.parse.ConfigManager;
import cn.itcast.utils.BeanUtils;

public class ClassPathXmlApplicationContext implements BeanFactory{

    private	Map<String,Bean> config;
    private Map<String,Object> context=new HashMap<>();

	public ClassPathXmlApplicationContext (String path) {
		 config=ConfigManager.getConfig(path);
		 for(Entry<String, Bean> en: config.entrySet()) {
			 String beanname=en.getKey();
			 Bean bean=en.getValue();
			 Object existBean=context.get(beanname);
			 if(existBean==null) {
				 Object beanObj=creatBean(bean);
				 context.put(beanname, beanObj);
			 }
		 }
	}
	private Object creatBean(Bean bean) {
		Object beanObj=null;
		String className=bean.getClassName();
		Class clazz=null;
		try {
			clazz=Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 beanObj=clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(bean.getProperties());
		if(bean.getProperties()!=null) {
			for(Property pro:bean.getProperties()) {
				if(pro.getValue()!=null) {
					 String value=pro.getValue();
					 String name=pro.getName();
					 Method method=BeanUtils.getWriteMethod(beanObj,name);
					 try {
						method.invoke(beanObj, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pro.getRef()!=null) {
					Object existBean=context.get(pro.getRef());
					if(existBean == null) {
						existBean=creatBean(config.get(pro.getRef()));
						context.put(pro.getRef(), existBean);
					}
					String name=pro.getName();
					 Method method=BeanUtils.getWriteMethod(beanObj,name);
					 try {
						method.invoke(beanObj, existBean);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return beanObj;
	}
	@Override
	public Object getBean(String beanName) {
		// TODO Auto-generated method stub
		Object bean=context.get(beanName);
		System.out.println(bean);
		return bean;
	}
}
