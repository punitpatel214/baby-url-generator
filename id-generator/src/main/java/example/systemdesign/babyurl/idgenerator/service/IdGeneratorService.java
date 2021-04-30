package example.systemdesign.babyurl.idgenerator.service;

import example.systemdesign.babyurl.idgenerator.config.ApplicationConfig;
import example.systemdesign.babyurl.idgenerator.dao.IdDao;
import example.systemdesign.babyurl.idgenerator.domain.IdGenerator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.CompletableFuture.runAsync;

@Singleton
public class IdGeneratorService {

    private final IdDao idDao;
    private final IdGenerator idGenerator;
    private final ApplicationConfig applicationConfig;
    private final IdCache idCache;

    public IdGeneratorService(IdDao idDao, IdGenerator idGenerator, ApplicationConfig applicationConfig) {
        this.idDao = idDao;
        this.idGenerator = idGenerator;
        this.applicationConfig = applicationConfig;
        this.idCache = new IdCache(applicationConfig.getCacheSize());
    }

    @PostConstruct
    public void init() {
        idCache.loadCache();
    }

    private List<String> loadData(int cacheSize) {
        ensureMinimumIdsAvailable();
        return idDao.getIds(cacheSize);
    }

    private void ensureMinimumIdsAvailable() {
        long idCount = idDao.getReadyToUseIdCount();
        long minimumAvailableIdCount = applicationConfig.getMinimumAvailableIdCount();
        if (idCount < minimumAvailableIdCount) {
            idDao.generateAndInsertIds(idGenerator, minimumAvailableIdCount);
        }
    }

    public String getId() {
        return idCache.getId();
    }

    private final class IdCache {
        private final ConcurrentLinkedQueue<String> cacheIdQueue;
        private final AtomicInteger cacheIdSize;
        private final int cacheSize;
        private final int minimumCacheSize;

        private IdCache(int cacheSize) {
            this.cacheSize = cacheSize;
            this.minimumCacheSize = cacheSize/2;
            this.cacheIdQueue = new ConcurrentLinkedQueue<>();
            this.cacheIdSize = new AtomicInteger(0);
        }

        public void loadCache() {
            List<String> ids = loadData(cacheSize);
            cacheIdQueue.addAll(ids);
            cacheIdSize.set(cacheIdQueue.size());
        }

        public String getId() {
            String id = cacheIdQueue.poll();
            if (cacheIdSize.decrementAndGet() < minimumCacheSize) {
                runAsync(this::loadCache);
            }
            return id;
        }
    }

}
