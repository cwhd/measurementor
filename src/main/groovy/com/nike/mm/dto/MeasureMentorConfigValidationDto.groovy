package com.nike.mm.dto

class MeasureMentorConfigValidationDto {
	
	String configId;
	
	String previousJobId;
	
	String jobid;
	
	List<RunnableMeasureMentorBusinessAndConfigDto> runnableMmbs;
	
	List<String> errors;
	
	MeasureMentorConfigValidationDto() {
		this.runnableMmbs 	= [];
		this.errors			= [];
	}
	
	String getMessageAsString() {
		String rstring = "";
		for (String error: this.errors) {
			rstring += (error + "\n");	
		}
		return rstring;
	}
}
