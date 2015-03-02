package se.skltp.av.service.tak.persistence;

import java.util.Collections;
import java.util.List;

import se.skltp.av.service.tak.m.PersistenceEntity;

public class TakCacheNoPersistenceImpl implements TakCachePersistenceServices {

	@Override
	public void persistEndpoints(List<PersistenceEntity> entitys) {
	}

	@Override
	public List<PersistenceEntity> getEndpoints() {
		return Collections.emptyList();
	}
	
	public PersistenceEntity getEndpoint(final String endpoint) {
		return null;
	}

}
