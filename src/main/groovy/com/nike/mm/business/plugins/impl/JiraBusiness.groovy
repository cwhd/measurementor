package com.nike.mm.business.plugins.impl

import org.springframework.stereotype.Service

import com.nike.mm.business.plugins.IJiraBusiness;

@Service
class JiraBusiness implements IJiraBusiness {

	@Override
	String type() {
		return "Jira";
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
