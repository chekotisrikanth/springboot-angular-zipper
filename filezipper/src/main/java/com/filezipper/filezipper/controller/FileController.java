package com.filezipper.filezipper.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filezipper.filezipper.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	FileService fileService;
	
	
	@GetMapping("/list")
    public ResponseEntity<?> files() {
		try {
			List<String> fileNames = fileService.getUserFileNames();
			return ResponseEntity.ok(fileNames);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving file names.");
        }
        
    }
	
	
	@PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
        	fileService.compressAndSave(files);
            return ResponseEntity.ok("success");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in uploading files");
        }
    }
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
		try {
        	byte[] fileContent = fileService.findFile(fileName);
        	HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName); 
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in downloading file. "+e.getMessage());
        }
	}

	    
}
