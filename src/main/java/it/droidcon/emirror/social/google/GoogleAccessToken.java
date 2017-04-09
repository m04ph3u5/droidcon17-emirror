package it.droidcon.emirror.social.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import it.droidcon.emirror.social.SocialToken;

public class GoogleAccessToken implements SocialToken{

	private  long releaseDate;
	
	private GoogleTokenResponse tokenResponse;
	
  public GoogleAccessToken() {
	}
	
  public GoogleAccessToken(GoogleTokenResponse tokenResponse){
  	this.tokenResponse = tokenResponse;
  	releaseDate = System.currentTimeMillis();
  }
  
	@Override
	public boolean isExpired() {
		Long expireInSeconds = tokenResponse.getExpiresInSeconds();
		if(expireInSeconds==null){
			return false;
		}
		return releaseDate+(expireInSeconds*1000)>System.currentTimeMillis();

	}

	@Override
	public long getReleaseDate() {
		return releaseDate;
	}

	@Override
	public void setReleaseDate(long releaseDate) {
		this.releaseDate = releaseDate;
	}

	public GoogleTokenResponse getTokenResponse() {
		return tokenResponse;
	}

	public void setTokenResponse(GoogleTokenResponse tokenResponse) {
		this.tokenResponse = tokenResponse;
	}

}
