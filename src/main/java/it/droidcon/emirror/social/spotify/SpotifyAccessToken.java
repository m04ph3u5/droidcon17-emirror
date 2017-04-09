package it.droidcon.emirror.social.spotify;

import it.droidcon.emirror.social.SocialToken;

public class SpotifyAccessToken implements SocialToken{

	private String access_token;
	private String token_type;
	private String scope;
	private long expires_in;
	private String refresh_token;
	private long releaseDate;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	@Override
	public boolean isExpired(){
		return releaseDate+(expires_in*1000)>System.currentTimeMillis();
	}
	
	@Override
	public long getReleaseDate() {
		return releaseDate;
	}

	@Override
	public void setReleaseDate(long releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("SPOTIFY CREDENTIALS\nAccessToken: ");
		sb.append(access_token);
		sb.append(", TokenType: ");
		sb.append(token_type);
		sb.append(", Scope: ");
		sb.append(scope);
		sb.append(", ExpiresIn: ");
		sb.append(expires_in);
		sb.append(", RefreshToken: ");
		sb.append(refresh_token);
		return sb.toString();
	}
}
