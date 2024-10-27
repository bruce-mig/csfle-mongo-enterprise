package com.github.bruce_mig.mongodb_csfle.configuration;

import com.mongodb.MongoNamespace;
import lombok.Getter;

/**
 * This class contains the metadata about the encrypted collections.
 */
@Getter
public class EncryptedEntity {
    public MongoNamespace namespace;
    public Class<?> entityClass;
    public String dekName;

    public EncryptedEntity(String database,
                           String collection,
                           Class<?> entityClass,
                           String dekName) {
        this.namespace = new MongoNamespace(database, collection);
        this.entityClass = entityClass;
        this.dekName = dekName;
    }

}
