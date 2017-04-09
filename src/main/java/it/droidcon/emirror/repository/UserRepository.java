package it.droidcon.emirror.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.droidcon.emirror.model.User;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository{
	

	public User findById(String id);
	public User findByGoogleProviderId(String id);
	public User findBySpotifyProviderId(String id);
	public User findByUsername(String username);
	
}
