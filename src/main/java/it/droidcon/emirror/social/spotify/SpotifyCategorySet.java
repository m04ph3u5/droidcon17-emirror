package it.droidcon.emirror.social.spotify;

import java.util.List;

public class SpotifyCategorySet {

	private List<SpotifyCategory> items;
	private String next;

	public List<SpotifyCategory> getItems() {
		return items;
	}

	public void setItems(List<SpotifyCategory> items) {
		this.items = items;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

}
