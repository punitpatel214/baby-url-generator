package example.systemdesign.babyurl.idgenerator.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("micronaut.application.instance")
public class ApplicationConfig {
    private String name;
    private long minimumAvailableIdCount;
    private int batchSize;
    private int cacheSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMinimumAvailableIdCount() {
        return minimumAvailableIdCount;
    }

    public void setMinimumAvailableIdCount(long minimumAvailableIdCount) {
        this.minimumAvailableIdCount = minimumAvailableIdCount;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @ConfigurationProperties("instance")
    public static class InstanceConfig {
        private String id;
    }
}
