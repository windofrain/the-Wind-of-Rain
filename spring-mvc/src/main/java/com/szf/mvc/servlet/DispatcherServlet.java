package com.szf.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szf.mvc.annotation.SunAutoWired;
import com.szf.mvc.annotation.SunController;
import com.szf.mvc.annotation.SunRequestMapping;
import com.szf.mvc.annotation.SunRequestParam;
import com.szf.mvc.annotation.SunService;
import com.szf.mvc.controller.FengController;

public class DispatcherServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//保存项目下所有类名
	List<String> classNames=new ArrayList<String>();
	//IOC容器
	Map<String,Object> beans=new HashMap<>();
	//
	Map<String,Method> handers=new HashMap<>();

	public void init(ServletConfig coonfig) {
		//把所有的bean扫描--------扫描所有的class文件
		scanPackage("com.szf");
		//反射创建对象
		doInstance();
		//加入IOC容器
		doIoc();
		//解析URL地址
		buildUrlMapping();	
	}
	
	public void scanPackage(String BasePackage) {
		URL url=this.getClass().getClassLoader().getResource("/"+BasePackage.replaceAll("\\.","/"));
		String fileStr=url.getFile();
		File file=new File(fileStr);
		String[] fileList=file.list();
		for(String path:fileList) {
			File filePath=new File(fileStr+path);
			if(filePath.isDirectory()) {
				scanPackage(BasePackage+"."+path);
			}else {
				classNames.add(BasePackage+"."+filePath.getName());
			}
		}
	}
	
	public void doInstance() {
		if(classNames.size()<=0) {
			System.out.println("包扫描失败");
			return;
		}
		for(String className:classNames) {
			String cn=className.replace(".class", "");
			Class<?> clazz=null;
			try {
				clazz=Class.forName(cn);
				if(clazz.isAnnotationPresent(SunController.class)) {
						Object instance=clazz.newInstance();
						SunRequestMapping  requestmapping=clazz.getAnnotation(SunRequestMapping.class);
						String rmValue=requestmapping.value();
						beans.put(rmValue,instance); 
				}else if(clazz.isAnnotationPresent(SunService.class)) {
					SunService service=clazz.getAnnotation(SunService.class);
					Object instance1=clazz.newInstance();
					beans.put(service.value(), instance1);
				}
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doIoc() {
		if(beans.entrySet().size()<0) {
			System.out.println("没有一个实例化类");
		}
		for(Map.Entry<String, Object> entry:beans.entrySet()) {
			Object instance=entry.getValue();
			Class<?> clazz=instance.getClass();
			if(clazz.isAnnotationPresent(SunController.class)) {
			   Field[] fields=clazz.getDeclaredFields();
			   for(Field field:fields) {
				   if(field.isAnnotationPresent(SunAutoWired.class)) {
					  SunAutoWired auto= field.getAnnotation(SunAutoWired.class);
					  String key=auto.value();
					  field.setAccessible(true);
					  try {
						field.set(instance, beans.get(key));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				   }else {
					   continue;
				   }
			   }
			}else {
				continue;
			}
		}
	}
	
	public void buildUrlMapping() {
		if(beans.entrySet().size()<=0) {
			System.out.println("没有类的实例化。。。。。");
			return;
		}
		for(Map.Entry<String, Object> entry:beans.entrySet()) {
			Object instance=entry.getValue();
			Class<?> clazz=instance.getClass();
		   if(clazz.isAnnotationPresent(SunController.class)) {	
			   SunRequestMapping requestmapping=clazz.getAnnotation(SunRequestMapping.class);
			   String classPath=requestmapping.value();
			   Method[] methods=clazz.getDeclaredMethods();
			   for(Method method:methods) {
				  if(method.isAnnotationPresent(SunRequestMapping.class)) {
				   SunRequestMapping methodMapping=method.getAnnotation(SunRequestMapping.class);
				   String methodPath=methodMapping.value();
				   handers.put(classPath+methodPath,method);
				  }else {
					   continue;
				   }
			   }
		   }else {
			   continue;
		   }
		}
	}
	public static Object[] hand(HttpServletRequest request, HttpServletResponse response,Method method) {
		Class<?>[] paramClazzs=method.getParameterTypes();
		Object[] args=new Object[paramClazzs.length];
		int args_i=0;
		int index=0;
		for(Class<?> paramClazz:paramClazzs) {
			if(ServletRequest.class.isAssignableFrom(paramClazz)) {
				args[args_i++]=request;
			}
			if(ServletResponse.class.isAssignableFrom(paramClazz)) {
				args[args_i++]=response;
			}
			Annotation[] paramAns=method.getParameterAnnotations()[index];
			if(paramAns.length>0) {
				for(Annotation paramAn:paramAns) {
					if(SunRequestParam.class.isAssignableFrom(paramAn.getClass())){
						SunRequestParam rp=(SunRequestParam)paramAn;
						args[args_i++]=request.getParameter(rp.value());
					}
				}
			}
			index++;
		}
		return args;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri=req.getRequestURI();
		String context=req.getContextPath();
		String path=uri.replace(context, "");
		Method method=(Method)handers.get(path);
		FengController controller=(FengController)beans.get("/"+path.split("/")[1]);
		Object[] args=hand(req,resp,method);
		try {
			String result=(String) method.invoke(controller,args);
			resp.setContentType("text/json;charset=UTF-8");
	        resp.setCharacterEncoding("UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.println(result);
	        out.flush();
	        out.close();
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
