package top.iseason.metaworldeducation.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisCache implements org.apache.ibatis.cache.Cache {

    private static final Cache<Object, Object> cache = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .maximumSize(1200)
            .softValues()
            .build();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final String id;

    public MybatisCache(final String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }


    @Override
    public void putObject(Object key, Object value) {
        if (key == null || value == null) return;
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Object removeObject(Object key) {
        cache.invalidate(key);
        return null;
    }

    /**
     * Clears this cache instance.
     */
    @Override
    public void clear() {
        cache.cleanUp();
    }

    /**
     * Optional. This method is not called by the core.
     *
     * @return The number of elements stored in the cache (not its capacity).
     */
    @Override
    public int getSize() {
        return (int) cache.estimatedSize();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

}