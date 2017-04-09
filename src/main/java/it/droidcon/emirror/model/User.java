package it.droidcon.emirror.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1744770055099169780L;

	@Id
	private String id;
	private String firstname;
	private String lastname;
	private String password;
	private List<Role> roles;
	private String email;
	private String username;
	private String imageUrl;
	private String googleProviderId;
	private String spotifyProviderId;

	public User() {
		roles = new ArrayList<Role>();
		roles.add(new Role("ROLE_USER"));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGoogleProviderId() {
		return googleProviderId;
	}

	public void setGoogleProviderId(String googleProviderId) {
		this.googleProviderId = googleProviderId;
	}

	public String getSpotifyProviderId() {
		return spotifyProviderId;
	}

	public void setSpotifyProviderId(String spotifyProviderId) {
		this.spotifyProviderId = spotifyProviderId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
