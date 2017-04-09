package it.droidcon.emirror.social;

public interface SocialToken {

	public abstract boolean isExpired();

	public long getReleaseDate();

	public void setReleaseDate(long releaseDate);
	
}
