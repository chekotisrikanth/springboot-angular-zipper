package com.filezipper.filezipper.model;

import java.sql.Blob;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserZipFile {

	@Id
    private Long _id;
	
	private String zipFileName;
	
	private Blob zipcontent;

	public UserZipFile(Long _id, String zipFileName, Blob zipcontent) {
		super();
		this._id = _id;
		this.zipFileName = zipFileName;
		this.zipcontent = zipcontent;
	}

	public UserZipFile() {
		super();
	}
	
	
}
