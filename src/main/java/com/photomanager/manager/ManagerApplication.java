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
    
    //To return a Response in the form of byte[] to send a photo to front end. (can then be displayed)
	public static ResponseEntity<byte[]> sendPhoto(byte[] photoCode, MediaType mediaType){
		//Creates the header 
		HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().contentType(mediaType).body(photoCode);
	
    }
    // Over-loaded method (uses MediaType.IMAGE JPEG as default image type)
	public static ResponseEntity<byte[]> sendPhoto(byte[] photoCode){	
		return sendPhoto(photoCode, MediaType.IMAGE_JPEG);
    }
    
    //Sends file to frontend by returning in REST API (can be downloaded on the front end using download attribute)
	public static ResponseEntity<Resource> sendFile(byte[] filecode,String filename, String contentType){
		//Creates a temp file for the response
        File file = new File("file");

        InputStreamResource resource = null;
        //if the byte[] recieved is null or empty return no response. Will not download empty file
        if(file == null || filecode.length == 0){

            return ResponseEntity.noContent().build();
        }
        //File Utils writes the byte[] to the file we made. 
        try {
            FileUtils.writeByteArrayToFile(file, filecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Instantiates resource using the file with the byte [] in it.
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders(); 
        //we are saying we are getting an attachment and what to name it
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
       //Saves the length of the file so that we can delete the file but keep the length for later.
        long resumelength = file.length();
        file.delete();

        //returns the responseEntity with the header and defined length that we saved. It uses parseMediaType
        //to have more of a range of media types. This can be saved when we recieve multipart file. body is the input
        //stream that we added the file to 
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resumelength)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
    //Over-loaded method for sendFile. The text included is the default type for a .docx file. 
    public static ResponseEntity<Resource> sendFile(byte[] filecode,String filename){
        return sendFile(filecode, filename, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }

}

