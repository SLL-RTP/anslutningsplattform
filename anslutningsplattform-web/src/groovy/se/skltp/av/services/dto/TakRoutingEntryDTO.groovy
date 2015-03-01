package se.skltp.av.services.dto

import groovy.transform.ToString

@ToString(includeNames=true, includeFields = true)
class TakRoutingEntryDTO {
    String id
    String name
	String url
	
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append(getClass().getSimpleName())
		sb.append(" [id: ")
		sb.append(id)
		sb.append(", name: ")
		sb.append(name)
		sb.append(", url: ")
		sb.append(url)
		sb.append("]")
	}
}
