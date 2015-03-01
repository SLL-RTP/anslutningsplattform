package se.skltp.av.service.tak;

import java.util.List;

import se.skltp.av.service.tak.m.PersistenceEntity;
import se.skltp.av.service.tak.persistence.TakCachePersistenceServices;

/**
 * Callback to be used when syncing TAK-cahe.
 *
 */
public interface TakSyncCacheCallback {
	/**
	 * Operation to be performer after that sync has been completed.
	 * @param persitencesEntitys
	 */
	public void onSyncComplete(final List<PersistenceEntity> persitencesEntitys);
	
	/**
	 * Operation to be performed after each successfull endpoint sync.
	 * @param endpoint
	 */
	public void onSyncSuccess(final String endpoint);
	
	/**
	 * Operation to be performed if endpoint sync fails.
	 * @param endpoint
	 * @param err
	 */
	public void onSyncError(final String endpoint, final Exception err);
}
