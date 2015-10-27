package se.skltp.ap.util

import java.util.regex.Pattern;

class TjanstekontraktUtil {

	//this is at least a very common pattern
	static Pattern takNamnrymdPattern = ~'^urn:riv:(.*)Responder:(\\d)$'
	
	// match RIV-TA info against TAK-info where (example):
	// RIV-TA: namnrymd: crm:scheduling:GetSubjectOfCareSchedule
	// RIV-TA: majorVersion: 1
	// TAK: namnrymd: urn:riv:crm:scheduling:GetSubjectOfCareScheduleResponder:1
	//   where the last number is the major version
	static boolean isNamnrymdEqual(String rivTaNamnrymd, String rivTaMajorVersion, String takNamnrymd) {
		def matcher = takNamnrymdPattern.matcher(takNamnrymd)
		if (matcher.matches()) {
			rivTaNamnrymd == matcher.group(1) && rivTaMajorVersion == matcher.group(2)
		} else { //resort to simplified comparison
			takNamnrymd.contains(rivTaNamnrymd) &&
					extractMajorVersionFromTakNamnrymd(takNamnrymd) == rivTaMajorVersion
		}
	}
		
	// namnrymd: urn:riv:crm:scheduling:GetSubjectOfCareScheduleResponder:1
	static String extractMajorVersionFromTakNamnrymd(String namnrymd) {
		namnrymd.substring(namnrymd.lastIndexOf(":") + 1)
	}

}
