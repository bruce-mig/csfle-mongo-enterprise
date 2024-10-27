package com.github.bruce_mig.mongodb_csfle.csfleServiceImpl;

import com.github.bruce_mig.mongodb_csfle.configuration.EncryptedCollectionsConfiguration;
import com.github.bruce_mig.mongodb_csfle.configuration.EncryptedEntity;
import com.github.bruce_mig.mongodb_csfle.csfleService.SchemaService;
import com.mongodb.MongoNamespace;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.json.JsonWriterSettings;
import org.springframework.data.mongodb.core.MongoJsonSchemaCreator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class SchemaServiceImpl implements SchemaService {

    private Map<MongoNamespace, BsonDocument> schemasMap;

    @Override
    public Map<MongoNamespace, BsonDocument> generateSchemasMap(MongoJsonSchemaCreator schemaCreator) {
        log.info("=> Generating schema map.");
        List<EncryptedEntity> encryptedEntities = EncryptedCollectionsConfiguration.encryptedEntities;
        return schemasMap = encryptedEntities.stream()
                                             .collect(toMap(EncryptedEntity::getNamespace,
                                                            e -> generateSchema(schemaCreator, e.getEntityClass())));
    }

    @Override
    public Map<MongoNamespace, BsonDocument> getSchemasMap() {
        return schemasMap;
    }

    private BsonDocument generateSchema(MongoJsonSchemaCreator schemaCreator, Class<?> entityClass) {
        BsonDocument schema = schemaCreator.filter(MongoJsonSchemaCreator.encryptedOnly())
                                           .createSchemaFor(entityClass)
                                           .schemaDocument()
                                           .toBsonDocument();
        log.info("=> JSON Schema for {}:\n{}", entityClass.getSimpleName(),
                    schema.toJson(JsonWriterSettings.builder().indent(true).build()));
        return schema;
    }

}
