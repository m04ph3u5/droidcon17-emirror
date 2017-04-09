package it.droidcon.emirror;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.droidcon.emirror.social.google.GoogleAuthenticationService;
import it.droidcon.emirror.social.spotify.SpotifyAuthenticationService;

@Configuration
public class BeanConfiguration {
	
	@Value("${google.clientId}")
	private String googleClientId;
	
	@Value("${google.clientSecret}")
	private String googleClientSecret;
	
	@Value("${server.baseUrl}")
	private String serverBaseUrl;

	@Value("${spotify.redirectUri}")
	private String spotifyRedirectUri;

	@Value("${spotify.clientId}")
	private String spotifyClientId;

	@Value("${spotify.clientSecret}")
	private String spotifyClientSecret;

	@Value("${spotify.scope}")
	private String spotifyScope;

	private GoogleAuthenticationService googleService;
	private SpotifyAuthenticationService spotifyService;
	
	@Bean
	public GoogleAuthenticationService getGoogleAuthService(){
		if(googleService==null){
			googleService = new GoogleAuthenticationService(googleClientId, googleClientSecret, null);
		}
		return googleService;
	}
	
	@Bean SpotifyAuthenticationService getSpotifyAuthService(){
		if(spotifyService==null){
			spotifyService = new SpotifyAuthenticationService(spotifyClientId, spotifyClientSecret, serverBaseUrl+spotifyRedirectUri, spotifyScope);
		}
		return spotifyService;
	}
}
