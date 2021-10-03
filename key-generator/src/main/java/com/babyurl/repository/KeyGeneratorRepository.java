package com.babyurl.repository;

import java.util.List;

public interface KeyGeneratorRepository {
    boolean insertKey(String key);

    String getKey();

    List<String> getKeys(int size);
}
