package com.nike.mm.dto

import com.nike.mm.business.plugins.IMeasureMentorBusiness


/** 
 * Used by the engine to run the  plugins.
 *
 */
class RunnableMeasureMentorBusinessAndConfigDto {
	
	/**
	 * An implementation of the application.
	 */
	IMeasureMentorBusiness measureMentorBusiness;
	
	/**
	 * A config object.
	 */
	Object config;
}
