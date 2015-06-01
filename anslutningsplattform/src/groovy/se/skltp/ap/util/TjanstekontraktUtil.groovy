package se.skltp.ap.util

import se.skltp.ap.services.dto.TjansteKontraktDTO;

class TjanstekontraktUtil {
	
	// match RIV-TA info against TAK-info where (example):
	// RIV-TA: namnrymd: crm:scheduling:GetSubjectOfCareSchedule
	// RIV-TA: majorVersion: 1
	// TAK: namnrymd: urn:riv:crm:scheduling:GetSubjectOfCareScheduleResponder:1
	//   where the last number is the major version
	static boolean isNamnrymdEqual(String rivTaNamnrymd, String rivTaMajorVersion,
			String takNamnrymd) {
			
		takNamnrymd.contains(rivTaNamnrymd) &&
			extractMajorVersionFromTakNamnrymd(takNamnrymd) == rivTaMajorVersion
	}
		
	// namnrymd: urn:riv:crm:scheduling:GetSubjectOfCareScheduleResponder:1
	static String extractMajorVersionFromTakNamnrymd(String namnrymd) {
		namnrymd.substring(namnrymd.lastIndexOf(":") + 1)
	}

}
