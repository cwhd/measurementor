package com.nike.mm.business.plugins.impl

import java.util.Date;

import org.springframework.stereotype.Service

import com.nike.mm.business.plugins.IJenkinsBusiness;

@Service
class JenkinsBusiness implements IJenkinsBusiness {

	@Override
	String type() {
		return "Jenkins";
	}
	
	@Override
	boolean validateConfig(Object config) {
		return config.url ? true:false;
	}
	
	@Override
	void updateData(Object configInfo) {
		throw new RuntimeException("unimplemented")
	}
}
