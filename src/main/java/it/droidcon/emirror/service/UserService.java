package it.droidcon.emirror.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import it.droidcon.emirror.model.TokenInfo;
import it.droidcon.emirror.model.User;
import it.droidcon.emirror.repository.TokenInfoRepository;
import it.droidcon.emirror.repository.UserRepository;
import it.droidcon.emirror.social.google.GoogleAccessToken;
import it.droidcon.emirror.social.google.GoogleUser;
import it.droidcon.emirror.social.spotify.SpotifyAccessToken;

@Service
public class UserService {

	private final String GOOGLE = "google";
	private final String SPOTIFY = "spotify";

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TokenInfoRepository tokenRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User updateGoogleUserInfoOrSubscribe(GoogleUser gu, GoogleAccessToken gat) {
		User u = userRepo.findByGoogleProviderId(gu.getUserId());

		if (u == null) {
			u = new User();
			u.setEmail(gu.getEmail());
			u.setUsername(GOOGLE + gu.getUserId());
			u.setFirstname(gu.getGivenName());
			u.setLastname(gu.getFamilyName());
			u.setImageUrl(gu.getPictureUrl());
			u.setGoogleProviderId(gu.getUserId());
			u.setPassword(passwordEncoder.encode(GOOGLE));
			userRepo.save(u);
			TokenInfo ti = new TokenInfo();
			GoogleTokenResponse token = gat.getTokenResponse();
			ti.setAccessToken(token.getAccessToken());
			ti.setExpireTime(gat.getReleaseDate() + token.getExpiresInSeconds() * 1000);
			ti.setProviderId(GOOGLE);
			ti.setProviderUserId(gu.getUserId());
			ti.setRefreshToken(token.getRefreshToken());
			ti.setUserId(u.getId());
			tokenRepo.save(ti);
		} else {
			TokenInfo ti = tokenRepo.findByProviderUserId(gu.getUserId());
			GoogleTokenResponse token = gat.getTokenResponse();
			ti.setAccessToken(token.getAccessToken());
			ti.setExpireTime(gat.getReleaseDate() + token.getExpiresInSeconds() * 1000);
			ti.setProviderId(GOOGLE);
			ti.setProviderUserId(gu.getUserId());
			if (token.getRefreshToken() != null && !token.getRefreshToken().isEmpty()) {
				ti.setRefreshToken(token.getRefreshToken());
			}
			ti.setUserId(u.getId());
			tokenRepo.updateToken(ti);
		}
		return u;
	}

	public void setSpotifyToken(User u, String spotifyUserId, SpotifyAccessToken token) {
		u.setSpotifyProviderId(spotifyUserId);
		userRepo.save(u);
		TokenInfo ti = tokenRepo.findByProviderUserId(spotifyUserId);
		if (ti == null) {
			ti = new TokenInfo();
			ti.setAccessToken(token.getAccess_token());
			ti.setExpireTime(token.getReleaseDate() + token.getExpires_in() * 1000);
			ti.setProviderId(SPOTIFY);
			ti.setProviderUserId(spotifyUserId);
			ti.setRefreshToken(token.getRefresh_token());
			ti.setUserId(u.getId());
			tokenRepo.save(ti);
		} else {
			ti.setAccessToken(token.getAccess_token());
			ti.setExpireTime(token.getReleaseDate() + token.getExpires_in() * 1000);
			ti.setProviderId(SPOTIFY);
			ti.setProviderUserId(spotifyUserId);
			ti.setRefreshToken(token.getRefresh_token());
			ti.setUserId(u.getId());
			tokenRepo.updateToken(ti);
		}
	}
}
