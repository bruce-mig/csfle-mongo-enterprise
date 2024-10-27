package com.github.bruce_mig.mongodb_csfle.csfleService;

import com.mongodb.client.MongoClient;

public interface KeyVaultService {
    void setupKeyVaultCollection(MongoClient mongoClient);
}
