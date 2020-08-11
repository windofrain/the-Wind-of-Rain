package cn.itcast.test;

import java.util.Map;
import java.util.Scanner;

import cn.itcast.bean.A;
import cn.itcast.bean.B;
import cn.itcast.config.Bean;
import cn.itcast.config.parse.ConfigManager;
import cn.itcast.main.BeanFactory;
import cn.itcast.main.ClassPathXmlApplicationContext;

public class Test {
  @org.junit.Test
  public void func1() {
    BeanFactory bf=new ClassPathXmlApplicationContext("/applicationContext.xml");
     A a=(A)bf.getBean("A");
    System.out.println(a.getName());
	  }
  }
