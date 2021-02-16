package com.portal.common.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.portal.common.model.CommonInfo;


@Repository(value="commonCodeMapper")
public interface CommonCodeMapper {
	
	List<CommonInfo> selectCommonCodeList() throws Exception;

}
