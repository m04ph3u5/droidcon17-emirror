package it.droidcon.emirror.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.droidcon.emirror.model.TokenInfo;

public class TokenInfoRepositoryImpl implements CustomTokenInfoRepository{

	@Autowired
	private MongoOperations mongoOp;
	
	@Override
	public TokenInfo updateToken(TokenInfo ti) {
		Query q = new Query();
		q.addCriteria(Criteria.where("userId").is(ti.getUserId()).andOperator(Criteria.where("providerId").is(ti.getProviderId())));
		Update u = new Update();
		u.set("refreshToken", ti.getRefreshToken());
		u.set("accessToken", ti.getAccessToken());
		u.set("expireTime", ti.getExpireTime());
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		return mongoOp.findAndModify(q, u, TokenInfo.class);
	}

}
