package it.droidcon.emirror.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import it.droidcon.emirror.CustomSessionListener;
import it.droidcon.emirror.model.AuthCode;
import it.droidcon.emirror.model.SessionId;
import it.droidcon.emirror.model.User;
import it.droidcon.emirror.service.UserService;
import it.droidcon.emirror.social.google.GoogleAccessToken;
import it.droidcon.emirror.social.google.GoogleAuthenticationService;
import it.droidcon.emirror.social.google.GoogleUser;
import it.droidcon.emirror.social.spotify.SpotifyAccessToken;
import it.droidcon.emirror.social.spotify.SpotifyAuthenticationService;
import it.droidcon.emirror.social.spotify.SpotifyUser;

@Controller
public class WebController {
	
	private final String APP_REDIRECT="emirror://login.spotify";

	@Autowired
	private SpotifyAuthenticationService spotify;

	@Autowired
	private GoogleAuthenticationService google;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomSessionListener listener;
	
	private Logger logger = LoggerFactory.getLogger(WebController.class);

	@RequestMapping(value = "/login/google", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody SessionId googleLogin(@RequestBody AuthCode code, HttpServletRequest request, HttpServletResponse response) {
		try {
			GoogleAccessToken gat = google.exchangeCodeForCredentials(code);
			GoogleUser gu = google.getUserProfile(gat);

			User u = userService.updateGoogleUserInfoOrSubscribe(gu, gat);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(u.getUsername(), "google", u.getAuthorities());
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication auth;
			try {
				auth = authenticationManager.authenticate(token);
			} catch (AuthenticationException e) {
				throw e;
			}
			
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(auth);
			                                                                // exists
			// if user has a http session you need to save context in session for
			// subsequent requests
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			return new SessionId(session.getId());
		} catch (IOException e) {
			response.setStatus(401);
			logger.error("Unauthorized: "+e.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/login/spotify", method = RequestMethod.GET)
	public void spotifyLogin(WebRequest request, HttpServletRequest httpRequest, HttpServletResponse response, 
			@RequestParam(value="jsessionid", required=true) String jsessionId) throws IOException {
		
		spotify.login(request, response, jsessionId);
	}

	@RequestMapping(value = "/callback/spotify", method = RequestMethod.GET)
	public @ResponseBody void spotifyCallback(WebRequest request, HttpServletResponse response,
	    @RequestParam(value = "code", required = false) String code,
	    @RequestParam(value = "state", required = false) String state,
	    @RequestParam(value = "error", required = false) String error) {

		HttpSession s = listener.getSession(state);
		SecurityContext ctx = (SecurityContext) s.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication auth = ctx.getAuthentication();
		User u = (User)(auth.getPrincipal());
		try {
			SpotifyAccessToken token = spotify.exchangeForToken(code, state, error, request);
			SpotifyUser userProfile = spotify.retrieveUserProfile(token);
			userService.setSpotifyToken(u, userProfile.getId(), token);
			response.sendRedirect(APP_REDIRECT);
		} catch (Exception e) {
			logger.error("Unauthorized: "+e.getMessage());
			response.setStatus(401);
		}
	}
}
