package com.nike.mm.repository

import spock.lang.Specification

class SonarQubeUnitSpec extends Specification {

	/*
	 * http://192.168.37.90:9000/api/resources
		<resources>
			<resource>
				<id>166</id>
				<key>workspace</key>
				<name>workspace</name>
				<lname>workspace</lname>
				<scope>PRJ</scope>
				<qualifier>TRK</qualifier>
				<version>unspecified</version>
				<date>2015-05-14T19:27:10+0000</date>
				<creationDate>2015-04-22T15:18:04+0000</creationDate>
			</resource>
			<resource>
				<id>179</id>
				<key>com.teksystems:candidateName</key>
				<name>candidateName-SalesTaxes</name>
				<lname>candidateName-SalesTaxes</lname>
				<scope>PRJ</scope>
				<qualifier>TRK</qualifier>
				<version>1.0-SNAPSHOT</version>
				<date>2015-05-15T09:34:57+0000</date>
				<creationDate>2015-05-15T09:31:22+0000</creationDate>
			</resource>
		</resources>
	 */
	
	/*
	 http://192.168.37.90:9000/api/resources?resource=com.teksystems:candidateName&metrics=ncloc,coverage,complexity,class_complexity,package_tangles,package_cycles,package_tangle_index,package_edges_weight,duplicated_blocks,functions,coverage,line_coverage,tests,test_execution_time
	 */
	 
}
