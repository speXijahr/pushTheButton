package cz.etn.ptb.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "cz.etn.ptb.repo")
public class MongoConfig extends AbstractMongoClientConfiguration {
    private static final String MONGO_DB_NAME = "ptb";

    protected String getDatabaseName() {
        return MONGO_DB_NAME;
    }

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Override
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString + MONGO_DB_NAME))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("cz.etn.ptb");
    }
}
