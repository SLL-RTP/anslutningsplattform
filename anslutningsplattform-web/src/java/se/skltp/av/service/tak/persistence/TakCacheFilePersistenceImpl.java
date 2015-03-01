package se.skltp.av.service.tak.persistence;


import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.StringUtils;
import se.skltp.av.service.tak.m.PersistenceEntity;
import se.skltp.tak.vagvalsinfo.wsdl.v2.AnropsBehorighetsInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.TjanstekontraktInfoType;
import se.skltp.tak.vagvalsinfo.wsdl.v2.VirtualiseringsInfoType;


/**
 *
 * File based persitence.
 *
 */
public class TakCacheFilePersistenceImpl implements TakCachePersistenceServices {

	private static final Logger log = LoggerFactory.getLogger(TakCacheFilePersistenceImpl.class);

	private static final String INDEX_FILE_NAME = "takCache.index";
	private static final String ENDPOINT_FILE_ENDING = ".tec";
	final String path;


	public TakCacheFilePersistenceImpl(final String path) {
		this.path = path;
	}

	public void persistEndpoints(final List<PersistenceEntity> entitys) {
		synchronized (TakCacheFilePersistenceImpl.class) {
			final Set<String> newEntrys = new HashSet<String>();
			final Index index = new Index();
			for(PersistenceEntity p : entitys) {
				try {
					CachedEndpoint cachedEndpoint = new CachedEndpoint();
					CachedEntries cachedEntries = new CachedEntries();
					cachedEndpoint.setEndpoint(p.getEndpoint());
					cachedEndpoint.setSynched(p.getSynched());
					cachedEntries.setAnropsbehorigheter(p.getAnropsbehorighet());
					cachedEntries.setTjanstekontrakt(p.getTjanstekontrakt());
					cachedEntries.setVirtualiseringar(p.getVirtualiseringar());
					cachedEndpoint.setEntries(cachedEntries);

					final String fileName = UUID.randomUUID().toString() + ENDPOINT_FILE_ENDING;

					createTakEndpointCacheFile(cachedEndpoint, fileName);

					IndexEntry indexEntry = new IndexEntry();
					indexEntry.setEndpoint(p.getEndpoint());;
					indexEntry.setFileName(fileName);
					indexEntry.setSynced(p.getSynched());
					index.getEntrys().add(indexEntry);

					newEntrys.add(p.getEndpoint());
				} catch (Exception err) {
					log.error("Could not create endpoint cache", err);
				}
			}
			try {
				for(IndexEntry entry : getIndex().getEntrys()) {
					if(!newEntrys.contains(entry.getEndpoint())) {
						index.getEntrys().add(entry);
					} else {
						removeFile(entry.getFileName());
					}
				}
				writeIndex(index);
			} catch (Exception err) {
				log.error("Could not create index", err);
			}
		}
	}

	private void removeFile(final String file) {
		try {
			final Path entryPath = FileSystems.getDefault().getPath(path, file);
			if(Files.exists(entryPath)) {
					Files.delete(entryPath);
			}
		} catch (Exception err) {
			log.error("Could not read index file", err);
		}
	}

	private Map<String, IndexEntry> indexMap(final Index index) {
		Map<String, IndexEntry> indexMap = new HashMap<String, IndexEntry>();
		if(index != null) {
			for(final IndexEntry entry : index.getEntrys()) {
				indexMap.put(entry.getEndpoint(), entry);
			}
		}
		return indexMap;
	}

	private Index getIndex() {
		Path indexPath = FileSystems.getDefault().getPath(path, INDEX_FILE_NAME);
		try {
			if(Files.exists(indexPath)) {
				JAXBContext ctx = JAXBContext.newInstance(Index.class);
				Unmarshaller u = ctx.createUnmarshaller();
				return (Index) u.unmarshal(indexPath.toFile());
			}
		} catch(Exception err) {
			log.error("Could not read index file", err);
		}
		return null;
	}

	private void createTakEndpointCacheFile(final CachedEndpoint cachedEndpoint, final String fileName) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(CachedEndpoint.class);
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		Path path = FileSystems.getDefault().getPath(this.path, fileName);
		m.marshal(cachedEndpoint, path.toFile());
	}

	private void writeIndex(final Index index) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(Index.class);
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		Path path = FileSystems.getDefault().getPath(this.path, INDEX_FILE_NAME);
		m.marshal(index, path.toFile());
	}

	private CachedEndpoint map(final File file) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(CachedEndpoint.class);
			Unmarshaller u = ctx.createUnmarshaller();
			return (CachedEndpoint) u.unmarshal(file);
		} catch (Exception err) {
			log.error("Could not map CachedEnpoint", err);
		}
		return null;
	}

	public List<PersistenceEntity> getEndpoints() {
		final List<PersistenceEntity> entitys = new ArrayList<PersistenceEntity>();
		synchronized (TakCacheFilePersistenceImpl.class) {
			final Index index = getIndex();
			if(index != null) {
				for(final IndexEntry indexEntry : index.getEntrys()) {
					Path cacheFile = FileSystems.getDefault().getPath(path, indexEntry.getFileName());
					if(Files.exists(cacheFile)) {
						final CachedEndpoint cached = map(cacheFile.toFile());
						if(cached != null) {
							entitys.add(new PersistenceEntity(
									cached.getEndpoint(),
									cached.getSynched(),
									cached.getEntries().getVirtualiseringar(),
									cached.getEntries().getTjanstekontrakt(),
									cached.getEntries().getAnropsbehorigheter()));
						}
					}
				}
			}
		}
		return entitys;
	}

	public PersistenceEntity getEndpoint(final String endpoint) {
		synchronized (TakCacheFilePersistenceImpl.class) {
			final Index index = getIndex();
			if(index != null) {
				for(final IndexEntry indexEntry : index.getEntrys()) {
					if(endpoint.equalsIgnoreCase(indexEntry.getEndpoint())) {
						Path cacheFile = FileSystems.getDefault().getPath(path, indexEntry.getFileName());
						if(Files.exists(cacheFile)) {
							final CachedEndpoint cached = map(cacheFile.toFile());
							if(cached != null) {
								return new PersistenceEntity(
										cached.getEndpoint(),
										cached.getSynched(),
										cached.getEntries().getVirtualiseringar(),
										cached.getEntries().getTjanstekontrakt(),
										cached.getEntries().getAnropsbehorigheter()
									);
							}
						}
					}
				}
			}
		}
		return null;
	}

}
@XmlRootElement()
class Index {
	private List<IndexEntry> entrys;

	public List<IndexEntry> getEntrys() {
		if(entrys == null) {
			this.entrys = new ArrayList<IndexEntry>();
		}
		return entrys;
	}

	public void setEntrys(List<IndexEntry> entrys) {
		this.entrys = entrys;
	}

}

@XmlRootElement()
class IndexEntry {
	private String endpoint;
	private Date synced;
	private String fileName;

	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public Date getSynced() {
		return synced;
	}
	public void setSynced(Date synced) {
		this.synced = synced;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

@XmlRootElement()
class CachedEndpoint {
	private String endpoint;
	private Date synched;
	private CachedEntries entries;

	public String getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Date getSynched() {
		return synched;
	}

	public void setSynched(Date synched) {
		this.synched = synched;
	}

	public CachedEntries getEntries() {
		return entries;
	}

	public void setEntries(CachedEntries entries) {
		this.entries = entries;
	}
}


@XmlRootElement()
class CachedEntries {
	private List<VirtualiseringsInfoType> virtualiseringar;
	private List<AnropsBehorighetsInfoType> anropsbehorigheter;
	private List<TjanstekontraktInfoType> tjanstekontrakt;

	public List<VirtualiseringsInfoType> getVirtualiseringar() {
		return virtualiseringar;
	}
	public void setVirtualiseringar(List<VirtualiseringsInfoType> virtualiseringar) {
		this.virtualiseringar = virtualiseringar;
	}
	public List<AnropsBehorighetsInfoType> getAnropsbehorigheter() {
		return anropsbehorigheter;
	}
	public void setAnropsbehorigheter(
			List<AnropsBehorighetsInfoType> anropsbehorigheter) {
		this.anropsbehorigheter = anropsbehorigheter;
	}
	public List<TjanstekontraktInfoType> getTjanstekontrakt() {
		return tjanstekontrakt;
	}
	public void setTjanstekontrakt(List<TjanstekontraktInfoType> tjanstekontrakt) {
		this.tjanstekontrakt = tjanstekontrakt;
	}
}
