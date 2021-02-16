package com.portal.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.common.mapper.CommonCodeMapper;
import com.portal.common.model.CommonInfo;



@Service("commonCodeService")
public class CommonCodeServiceImple implements CommonCodeService {
	private static final Logger logger = LoggerFactory.getLogger(CommonCodeServiceImple.class);
	
	@Autowired
	private CommonCodeMapper commonCodeMapper;
	
	@Override
	public Map<String,Object> selectCommonCodeList()
			throws Exception {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<CommonInfo> majorList = null;
		List<CommonInfo> codeList = null;
		
		codeList = commonCodeMapper.selectCommonCodeList();
		
		
		returnMap.put("codeList", codeList);
		
		return returnMap;
	}
	
}
