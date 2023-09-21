package com.filezipper.filezipper.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

	
	@Id
    private Long _id;
	
	private String username;
	private String password;
	private List<String> authorities;
	
	@DBRef
	private List<UserZipFile> userFiles;
	
	
	public User() {
		super();
	}


	public User(Long _id, String username, String password, List<String> authorities, List<UserZipFile> userFiles) {
		super();
		this._id = _id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.userFiles = userFiles;
	}
	
	
	
	
	
	
}	
