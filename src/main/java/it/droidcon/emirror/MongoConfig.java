package it.droidcon.emirror;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**
 * @author m04ph3u5
 *
 */
@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration{

    @Value("${mongo.host}")
    private String host;
    
//    @Value("${mongo.port}")
//    private int port;
//
    @Value("${mongo.dbName}")
    private String dbName;
//    
//    @Value("${mongo.username}")
//    private String username;
//    
//    @Value("${mongo.password}")
//    private String password;
//    
//    @Value("${mongo.authenticationDB}")
//    private String authenticationDB;
//    
	/* (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		return dbName;
	}
	
	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(host);
	}
	
	@Override
    protected String getMappingBasePackage() {
        return "it.droidcon.emirror.repository";
	}

	@Override
	public MongoTemplate mongoTemplate() throws Exception{
		MongoTemplate template = new MongoTemplate(mongo(), getDatabaseName());
		template.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		template.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		return template;
	}
}

