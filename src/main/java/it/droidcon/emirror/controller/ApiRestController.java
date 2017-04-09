package it.droidcon.emirror.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.droidcon.emirror.CustomSessionListener;
import it.droidcon.emirror.model.AuthCode;
import it.droidcon.emirror.model.Emotion;
import it.droidcon.emirror.model.EmotionType;
import it.droidcon.emirror.model.TokenInfo;
import it.droidcon.emirror.model.User;
import it.droidcon.emirror.repository.TokenInfoRepository;
import it.droidcon.emirror.service.BusinessService;
import it.droidcon.emirror.social.spotify.SpotifyPlaylist;
import it.droidcon.emirror.social.spotify.Track;

@RestController
@RequestMapping("/api/v1")
public class ApiRestController {

	@Autowired
	private CustomSessionListener listener;
	
	@Autowired
	private BusinessService service;
	
	@Autowired
	private TokenInfoRepository tokenInfoRepo;
	
	@RequestMapping(value = "/emotion", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public List<Track> postUserEmotions(@RequestBody Emotion emotion,
			@RequestParam(value="jsessionid", required=true) String jsessionId){

		HttpSession s = listener.getSession(jsessionId);
		SecurityContext ctx = (SecurityContext) s.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication auth = ctx.getAuthentication();
		User u = (User)(auth.getPrincipal());
		
		EmotionType e = service.retrieveEmotionType(emotion);
		SpotifyPlaylist list = service.getPlaylistForMood(e);
		
		return service.getTrackList(list, u);
	}
	
	@RequestMapping(value = "/code", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public AuthCode getSpotifyAccessToken(@RequestParam(value="jsessionid", required=true) String jsessionId){
		HttpSession s = listener.getSession(jsessionId);
		SecurityContext ctx = (SecurityContext) s.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication auth = ctx.getAuthentication();
		User u = (User)(auth.getPrincipal());
		TokenInfo token = tokenInfoRepo.findByProviderUserId(u.getSpotifyProviderId());
		return new AuthCode(token.getAccessToken());
	}
}
