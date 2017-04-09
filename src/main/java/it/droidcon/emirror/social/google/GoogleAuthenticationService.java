package it.droidcon.emirror.social.google;

import java.io.FileReader;
import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import it.droidcon.emirror.model.AuthCode;

public class GoogleAuthenticationService {

	private String clientId;
	private String clientSecret;
	private String redirectUri;
	/** Global instance of the HTTP transport. */
	private HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();

	public GoogleAuthenticationService(final String clientId, final String clientSecret, final String redirectUri) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		if (redirectUri == null) {
			this.redirectUri = "";
		} else {
			this.redirectUri = redirectUri;
		}

	}

	public GoogleAccessToken exchangeCodeForCredentials(AuthCode code) throws IOException {

		String CLIENT_SECRET_FILE = "/googleSecret.json";
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
		    new FileReader(CLIENT_SECRET_FILE));

		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
		    JacksonFactory.getDefaultInstance(), "https://www.googleapis.com/oauth2/v4/token",
		    clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), code.getAuthCode(),
		    redirectUri).execute();

		GoogleAccessToken token = new GoogleAccessToken(tokenResponse);
		return token;
	}

	public GoogleUser getUserProfile(GoogleAccessToken token) throws IOException {
		GoogleTokenResponse tokenResponse = token.getTokenResponse();
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();

		GoogleUser gu = new GoogleUser();
		String userId = payload.getSubject(); // Use this value as a key to identify
		                                      // a user.
		gu.setUserId(userId);
		String email = payload.getEmail();
		gu.setEmail(email);
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		gu.setEmailVerified(emailVerified);
		String name = (String) payload.get("name");
		gu.setName(name);
		String pictureUrl = (String) payload.get("picture");
		gu.setPictureUrl(pictureUrl);
		String locale = (String) payload.get("locale");
		gu.setLocale(locale);
		String familyName = (String) payload.get("family_name");
		gu.setFamilyName(familyName);
		String givenName = (String) payload.get("given_name");
		gu.setGivenName(givenName);

		return gu;

	}

}
