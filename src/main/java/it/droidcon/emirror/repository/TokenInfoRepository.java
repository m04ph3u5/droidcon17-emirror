package it.droidcon.emirror.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.droidcon.emirror.model.TokenInfo;

public interface TokenInfoRepository extends MongoRepository<TokenInfo, String>, CustomTokenInfoRepository{

	public TokenInfo findByProviderUserId(String providerUserId);

}
