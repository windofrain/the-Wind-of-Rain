package com.szf.mvc.service;

import com.szf.mvc.annotation.SunService;

@SunService("FengServiceImp")
public class FengServieImp implements FengService{

	@Override
	public String query(String name,String age) {
		// TODO Auto-generated method stub
		return  "{\"姓名\":\"孙先生\",\"年龄\":\"25岁啦\"}";
	}

}
