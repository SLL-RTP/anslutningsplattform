package se.skltp.av.service.tak.persistence;

import java.util.List;

import se.skltp.av.service.tak.m.PersistenceEntity;

public interface TakCachePersistenceServices {
	
	/**
	 * Persist provided entitys
	 * @param entitys
	 */
	public void persistEndpoints(final List<PersistenceEntity> entitys);
	
	/**
	 * Get all persisted entitys
	 * @return
	 */
	public List<PersistenceEntity> getEndpoints();
	
	/**
	 * Get specific persisted entity
	 * @param endpoint
	 * @return
	 */
	public PersistenceEntity getEndpoint(final String endpoint);
}
