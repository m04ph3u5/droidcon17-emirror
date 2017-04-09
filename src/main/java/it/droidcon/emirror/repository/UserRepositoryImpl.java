package it.droidcon.emirror.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

public class UserRepositoryImpl implements CustomUserRepository{

	@Autowired
	private MongoOperations mongoOp;
	

}
