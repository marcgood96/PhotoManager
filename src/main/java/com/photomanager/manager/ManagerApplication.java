package com.photomanager.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.io.FileUtils;

@SpringBootApplication
public class ManagerApplication {
	
	public static ResponseEntity<byte[]> sendPhoto(byte[] photoCode, MediaType mediaType){
		
		HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().contentType(mediaType).body(photoCode);
	
	}
	public static ResponseEntity<byte[]> sendPhoto(byte[] photoCode){	
		return sendPhoto(photoCode, MediaType.IMAGE_JPEG);
	}
	public static ResponseEntity<Resource> sendFile(byte[] filecode,String filename, String contentType){
		
        File file = new File("file");
        InputStreamResource resource = null;
       

        if(file == null || filecode.length == 0){

            return ResponseEntity.noContent().build();
        }
        try {
            FileUtils.writeByteArrayToFile(file, filecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
      
        long resumelength = file.length();
        file.delete();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resumelength)

                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
	}

}

