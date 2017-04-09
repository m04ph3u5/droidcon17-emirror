package it.droidcon.emirror.social.spotify;

public class Track {

	private String uri;
	private String artist;
	private String image;
	private String title;

	public Track(SpotifyTrack spotifyTrack) {
		this.uri = spotifyTrack.getUri();
		this.artist = spotifyTrack.getArtists().get(0).getName();
		this.image = spotifyTrack.getImages().get(0).getUrl();
		this.title = spotifyTrack.getName();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
