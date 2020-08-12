package com.szf.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szf.mvc.annotation.SunAutoWired;
import com.szf.mvc.annotation.SunController;
import com.szf.mvc.annotation.SunRequestMapping;
import com.szf.mvc.annotation.SunRequestParam;
import com.szf.mvc.service.FengService;

@SunController
@SunRequestMapping(value="/Zhi")
public class FengController {
	@SunAutoWired("FengServiceImp")
	 private FengService service;
	@SunRequestMapping(value="/query")
	public String query(HttpServletRequest request,HttpServletResponse response
			,@SunRequestParam("name")String name,@SunRequestParam("age") String age) {
		return service.query(name,age);
	}
}
