package com.babyurl.urlshortener.repositiry.cassandra;

import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;

public enum ShortenURLTableMetaData {
    KEY("key", DataTypes.TEXT),
    URL("url", DataTypes.TEXT),
    CREATE_TIME("create_time", DataTypes.TIMESTAMP),
    EXPIRY_TIME("expiry_time", DataTypes.TIMESTAMP);

    public static final String TABLE_NAME = "ShortenURL";
    public final String columnName;
    public final DataType dataType;

    ShortenURLTableMetaData(String columnName, DataType dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

}
