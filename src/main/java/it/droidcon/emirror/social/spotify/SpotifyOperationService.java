package it.droidcon.emirror.social.spotify;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyOperationService {
	
	private final String retrieveCategories="https://api.spotify.com/v1/browse/categories";

	public SpotifyCategorySet getCategories(SpotifyAccessToken token){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAccess_token());
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		
		ResponseEntity<SpotifyCategorySet> response = restTemplate.exchange(retrieveCategories, HttpMethod.GET, entity, SpotifyCategorySet.class);
		return response.getBody();
	}
}
