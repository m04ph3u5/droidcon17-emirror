package it.droidcon.emirror.social.spotify;

public class SpotifyCategory {

	/**
	 * string A link to the Web API endpoint returning full details of the
	 * category.
	 */
	private String href;
	/**
	 * string The Spotify category ID of the category.
	 */
	private String id;
	/**
	 * The name of the category.
	 */
	private String name;

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

}
