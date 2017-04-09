package it.droidcon.emirror.social.spotify;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

public class SpotifyAuthenticationService {

	private Logger logger = LoggerFactory.getLogger(SpotifyAuthenticationService.class);

	private String spotifyRedirectUri;
	private String spotifyClientId;
	private String spotifyClientSecret;
	private String scope;

	private final String spotifyBaseUrl="https://accounts.spotify.com";
	
	private final String spotifyApi="https://api.spotify.com";
	
	private final String retrieveUserUrl=spotifyApi+"/v1/me";

	private final String spotifyAuthorizeUrl=spotifyBaseUrl+"/authorize";

	private final String spotifyExchangeUrl="/api/token";
	
	private RestTemplate restTemplate;
	
	public SpotifyAuthenticationService(String spotifyClientId, String spotifyClientSecret, String spotifyRedirectUri,
	    String spotifyScope) {
		this.spotifyClientId = spotifyClientId;
		this.spotifyClientSecret = spotifyClientSecret;
		this.spotifyRedirectUri = spotifyRedirectUri;
		this.scope = spotifyScope;
	}

	@PostConstruct
	private void init(){
		restTemplate = new RestTemplate();
	}

	public void login(WebRequest request, HttpServletResponse response, String jsessionId) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(spotifyAuthorizeUrl);
		sb.append("?client_id=");
		sb.append(spotifyClientId);
		sb.append("&response_type=code&redirect_uri=");
		sb.append(spotifyRedirectUri);
		sb.append("&state=");
		sb.append(jsessionId);
		sb.append("&scope=");
		sb.append(scope);
		response.sendRedirect(sb.toString());
	}

	public SpotifyAccessToken exchangeForToken(String code, String state, String error, WebRequest request) throws Exception {
		if (error != null) {
			logger.error(error);
			throw new Exception("Unauthorized");
		}
//		String oldState = (String) request.getAttribute("state", WebRequest.SCOPE_SESSION);
//		if (oldState == null || !oldState.equals(state)) {
//			throw new Exception("Unauthorized");
//		}
		
		String authHeader = getAuthorizationHeader();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", authHeader);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "authorization_code");
		map.add("code", code);
		map.add("redirect_uri", spotifyRedirectUri);
		
		HttpEntity<MultiValueMap<String, String>> post = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<SpotifyAccessToken> response = restTemplate.postForEntity( spotifyBaseUrl+spotifyExchangeUrl, post , SpotifyAccessToken.class );
		SpotifyAccessToken token = response.getBody();
		token.setReleaseDate(System.currentTimeMillis());
		return token;
	}

	public SpotifyUser retrieveUserProfile(SpotifyAccessToken token) {
		if(token.isExpired()){
			token = refreshCredentials(token.getRefresh_token());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAccess_token());
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		ResponseEntity<SpotifyUser> response = restTemplate.exchange(retrieveUserUrl, HttpMethod.GET, entity, SpotifyUser.class);
		return response.getBody();
	}

	public SpotifyAccessToken refreshCredentials(String refreshToken) {
		String authHeader = getAuthorizationHeader();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", authHeader);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "refresh_token");
		map.add("refresh_token", refreshToken);
		
		HttpEntity<MultiValueMap<String, String>> post = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		ResponseEntity<SpotifyAccessToken> response = restTemplate.postForEntity( spotifyBaseUrl+spotifyExchangeUrl, post , SpotifyAccessToken.class );
		SpotifyAccessToken token = response.getBody();
		token.setReleaseDate(System.currentTimeMillis());
		return token;
	}
	
	private String getAuthorizationHeader(){
		String auth = spotifyClientId + ":" + spotifyClientSecret;
    byte[] encodedAuth = Base64.getEncoder().encode( 
       auth.getBytes(Charset.forName("US-ASCII")) );
    String authHeader = "Basic " + new String( encodedAuth );
    return authHeader;
	}
}
