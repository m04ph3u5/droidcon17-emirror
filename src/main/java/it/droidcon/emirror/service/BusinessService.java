package it.droidcon.emirror.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.droidcon.emirror.model.Emotion;
import it.droidcon.emirror.model.EmotionType;
import it.droidcon.emirror.model.TokenInfo;
import it.droidcon.emirror.model.User;
import it.droidcon.emirror.repository.TokenInfoRepository;
import it.droidcon.emirror.social.spotify.SpotifyPlaylist;
import it.droidcon.emirror.social.spotify.SpotifyTrack;
import it.droidcon.emirror.social.spotify.Track;

@Service
public class BusinessService {
	
	private Map<EmotionType, SpotifyPlaylist> playlistMap;
	
	@Autowired
	private TokenInfoRepository tokenInfoRepo;
	
	@PostConstruct
	private void init(){
		playlistMap = new HashMap<>();
		SpotifyPlaylist p = new SpotifyPlaylist();
		p.setCollaborative(false);
		p.setHref("https://api.spotify.com/v1/users/spotify/playlists/37i9dQZF1DWXLeA8Omikj7");
		p.setId("37i9dQZF1DWXLeA8Omikj7");
		p.setName("Brain Food");
		p.setUserId("spotify");
		p.setUri("spotify:user:spotify:playlist:37i9dQZF1DWXLeA8Omikj7");
		playlistMap.put(EmotionType.frustrato, p);
		
		SpotifyPlaylist p1 = new SpotifyPlaylist();
		p1.setCollaborative(false);
		p1.setHref("https://api.spotify.com/v1/users/spotify/playlists/1B9o7mER9kfxbmsRH9ko4z");
		p1.setId("1B9o7mER9kfxbmsRH9ko4z");
		p1.setName("Feelin good");
		p1.setUserId("spotify");
		p1.setUri("spotify:user:spotify:playlist:37i9dQZF1DWXLeA8Omikj7");
		playlistMap.put(EmotionType.irritato, p1);
	}
	

	public EmotionType retrieveEmotionType(Emotion e){

		List<Double> values = new ArrayList<Double>(8);
		values.add(0, e.getAnger());
		values.add(1, e.getContempt());
		values.add(2, e.getDisgust());
		values.add(3, e.getFear());
		values.add(4, e.getHappiness());
		values.add(5, e.getNeutral());
		values.add(6, e.getSadness());
		values.add(7, e.getSurprise());
		
		Double d = Collections.max(values);
		int i = values.indexOf(d);
		
		List<Double> valuesCopy = new ArrayList<Double>(7);
		for(int k=0; k<8; k++){
			if(k!=i){
				valuesCopy.add(values.get(k));
			}
		}
		
		Double dd = Collections.max(valuesCopy);
		int j = values.indexOf(dd);
		
		if(i==0 && j==1){
			return EmotionType.frustrato;
		}
		if(i==0 && j==2){
			return EmotionType.irritato;
		}
		if(i==0 && j==3){
			return EmotionType.ansioso;
		}
		if(i==0 && j==6){
			return EmotionType.distrutto;
		}
		if(i==1 && j==2){
			return EmotionType.annoiato;
		}
		if(i==1 && j==3){
			return EmotionType.terrorizzato;
		}
		if(i==1 && j==6){
			return EmotionType.insicuro;
		}
		if(i==1 && j==7){
			return EmotionType.scioccato;
		}
		if(i==2 && j==3){
			return EmotionType.respinto;
		}
		if(i==2 && j==6){
			return EmotionType.umiliato;
		}
		if(i==2 && j==7){
			return EmotionType.risentito;
		}
		if(i==3 && j==4){
			return EmotionType.vulnerabile;
		}
		if(i==3 && j==6){
			return EmotionType.depresso;
		}
		if(i==3 && j==7){
			return EmotionType.preoccupato;
		}
		if(i==4 && j==6){
			return EmotionType.confuso;
		}
		if(i==4 && j==7){
			return EmotionType.eccitato;
		}
		if(i==6 && j==7){
			return EmotionType.sensibile;
		}
		if(i==5 && j==0){
			return EmotionType.arrabbiato;
		}
		if(i==5 && j==1){
			return EmotionType.disprezzo;
		}
		if(i==5 && j==2){
			return EmotionType.disgustato;
		}
		if(i==5 && j==3){
			return EmotionType.impaurito;
		}
		if(i==5 && j==4){
			return EmotionType.felice;
		}
		if(i==5 && j==6){
			return EmotionType.triste;
		}
		if(i==5 && j==7){
			return EmotionType.sorpreso;
		}
		
		return EmotionType.neutrale;
	}
	
	public SpotifyPlaylist getPlaylistForMood(EmotionType e){
		if(playlistMap.containsKey(e)){
			return playlistMap.get(e);
		} else {
			SpotifyPlaylist p = new SpotifyPlaylist();
			p.setCollaborative(false);
			p.setHref("https://api.spotify.com/v1/users/spotify/playlists/37i9dQZF1DWYBO1MoTDhZI");
			p.setId("37i9dQZF1DWYBO1MoTDhZI");
			p.setName("Good vibes");
			p.setUserId("spotify");
			p.setUri("spotify:user:spotify:playlist:37i9dQZF1DWYBO1MoTDhZI");
			return p;
		}
	}


	public List<Track> getTrackList(SpotifyPlaylist list, User u) {
		TokenInfo token = tokenInfoRepo.findByProviderUserId(u.getSpotifyProviderId());
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAccessToken());
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		String url = "https://api.spotify.com/v1/users/"+list.getUserId()+"/playlists/"+list.getId()+"/tracks?market=IT";
		ResponseEntity<SpotifyTrack[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, SpotifyTrack[].class);
		SpotifyTrack[] tt = response.getBody();
		if(tt==null || tt.length==0){
			return Collections.emptyList();
		}
		List<Track> tracks = new ArrayList<Track>(tt.length);
		for(int i=0; i<tt.length; i++){
			tracks.add(new Track(tt[i]));
		}
		return tracks;
	}
}
