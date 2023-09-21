package com.filezipper.filezipper.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class FileService {

	@Autowired
    private GridFsTemplate gridFsTemplate;
	
	@Autowired
    private GridFsOperations gridFsOperations;

	@Autowired
	JwtTokenService jwtTokenService;
	
	
	public void compressAndSave(List<MultipartFile> files) throws IOException {
		File zipFile = compressFiles(files);
		saveFile(zipFile);
		zipFile.delete();
	}
    
	private File compressFiles(List<MultipartFile> files) throws IOException {
		File tempDirectory = new File("temp");
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }
        
        String zipFileName = "temp/compressed_files_" + System.currentTimeMillis() + ".zip";
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);

            InputStream fileInputStream = file.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                zipOut.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            zipOut.closeEntry();
        }

        zipOut.close();
        fos.close();

        File zipFile = new File(zipFileName);
        
        return zipFile;
	}
    private String saveFile(File zipFile) throws IOException {
    	String userName = jwtTokenService.getLoggedInUserName();
    	FileInputStream zipInputStream = new FileInputStream(zipFile);
    	DBObject metaData = new BasicDBObject(); 
        metaData.put("userName", userName);
        ObjectId id = gridFsTemplate.store(zipInputStream, zipFile.getName(), "application/zip", metaData); 
        return id.toString();
    }
    
    public List<String> getUserFileNames() throws IllegalStateException, IOException { 
    	String userName = jwtTokenService.getLoggedInUserName();
        GridFSFindIterable files = gridFsOperations.find(new Query(Criteria.where("metadata.userName").is(userName))); 
        List<String> fileNames = new ArrayList<>();

        for (GridFSFile file : files) {
            fileNames.add(file.getFilename());
        }
        return fileNames; 
    }
    
    public byte[] findFile(String fileName) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is(fileName)));
        if (gridFSFile != null) {
            try (InputStream inputStream = gridFsOperations.getResource(gridFSFile).getInputStream()) {
                return StreamUtils.copyToByteArray(inputStream);
            }
        } else {
            throw new FileNotFoundException("File not found: " + fileName);
        }

    }
}

