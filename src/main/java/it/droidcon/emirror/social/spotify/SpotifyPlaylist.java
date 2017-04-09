package it.droidcon.emirror.social.spotify;

public class SpotifyPlaylist {

	private boolean collaborative;
	private String href;
	private String id;
	private String name;
	private SpotifyPlaylistTracks tracks;
	private String uri;
	private String userId;

	public boolean isCollaborative() {
		return collaborative;
	}

	public void setCollaborative(boolean collaborative) {
		this.collaborative = collaborative;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpotifyPlaylistTracks getTracks() {
		return tracks;
	}

	public void setTracks(SpotifyPlaylistTracks tracks) {
		this.tracks = tracks;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
