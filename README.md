# PhotoManager
A library for java to upload and download (maybe more) photos. Built specifically for REST API use. 

Currently only one main java file. src/main/java/com/photomanager/manager/ManagerApplication.java

<table>
  <tr> <td>Old Code</td></tr>
   <tr> <td>
        
        File file = new File("file");
        InputStreamResource resource = null;
        DocumentFile myDocument = application.getResume();
        
        if(myDocument.getFile() == null || myDocument.getFile().length ==0 ){

            return ResponseEntity.noContent().build();
        }
        try {
            FileUtils.writeByteArrayToFile(file, myDocument.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders(); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+myDocument.getName());
        logger.info("CREATED FILE");
        long fileLength = file.length();
        resume.delete();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(filelength)

                .contentType(MediaType.parseMediaType(myDocument.getContentType()))
                .body(resource);
    }
    
  </td></tr>
  <tr> <td>New Code</td></tr>
  <tr>  <td> return Manager.sendFile(myDocument.getFile(),myDocument.getName(),myDocument.getContentType());</td>
  </tr>
</table>
