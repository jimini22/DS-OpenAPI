package com.portal.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.portal.common.service.CommonCodeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WelcomeController {

	@Autowired
	private CommonCodeService commonCodeService;


	@RequestMapping("/hello")
	public ModelAndView hello() {
		String msg = "Hello, World!";
		return new ModelAndView("index", "msg", msg);
	}
	
	@ResponseBody
	@RequestMapping("/test1")
	public  String test1() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
	
		resultMap = commonCodeService.selectCommonCodeList();
	
		String resultStr = gson.toJson(resultMap);
		log.info("resultStr : " + resultStr);
		return resultStr;

	}
}
