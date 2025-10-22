package com.filehub.client.filemanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filehub.client.FMConstants;
import com.filehub.client.filemanagement.model.ApiResponse;
import com.filehub.client.filemanagement.model.FileData;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import static com.filehub.client.FMConstants.*;

@Component
public class Util {

    public ArrayList<FileData> getFileList(ResponseEntity<ApiResponse> responseEntity) {
        ArrayList<FileData> al = new ArrayList<>();
        try {
            var response = responseEntity.getBody();
            if (response != null && response.getStatus() == 200)
                al = (ArrayList<FileData>) response.getData();
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
        return al;
    }

    public ResponseEntity<?> downloadFile(ResponseEntity<byte[]> response){
        try
        {
            HttpHeaders headers = response.getHeaders();
            MediaType contentType = headers.getContentType();

            if (MediaType.APPLICATION_JSON.includes(contentType)) {
                //Handle error
                String json = new String(response.getBody(), StandardCharsets.UTF_8);
                ApiResponse<?> error = new ObjectMapper().readValue(json, ApiResponse.class);
                return ResponseEntity.ok(new ApiResponse<>(500,"Internal server error",error.getMessage()));
            } else {
                String contentDispositionValue = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);
                String fileName = "downloaded_"+UUID.randomUUID();
                if (contentDispositionValue != null) {
                    ContentDisposition disposition = ContentDisposition.parse(contentDispositionValue);
                    String fName = disposition.getFilename();
                    if (fName != null && !fName.trim().isBlank())
                        fileName = fName;
                }
                String downloadPath = FMConstants.DOWNLOAD_PATH + fileName;
                byte[] fileBytes = response.getBody();
                Files.write(Paths.get(downloadPath), fileBytes);
                return ResponseEntity.status(200).body(new ApiResponse<>(200,"File downloaded",null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(500,"Internal server error",e.getMessage()));
        }
    }

    public ResponseEntity<?> uploadFile( String filePath, String toPath)
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("toPath", toPath);  // destination path
            body.add("file", new FileSystemResource(new File(filePath)));

            HttpHeaders headers = new HttpHeaders();

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String url = SERVER_URL + "/file/uploadfile";
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(url, requestEntity, ApiResponse.class);
            HttpStatusCode statusCode = response.getStatusCode();
            if(statusCode.is2xxSuccessful())
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "File uploaded successfully", null));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>(500, "Error saving file", null));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error saving file", e.getMessage()));
        }
    }


    public boolean transferFile(String sPath, String dPath, boolean move) {
        try {
            Path sourcePath = Paths.get(sPath);
            Path destPath = Paths.get(dPath);

            Files.createDirectories(destPath.getParent());

            if (move) {
                Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public boolean renameFile(String filePath, String newName) {
        try {
            Path sourcePath = Paths.get(filePath);

            if (!Files.exists(sourcePath)) {
                System.err.println("File does not exist: " + filePath);
                return false;
            }

            Path parentDir = sourcePath.getParent();
            Path destPath = parentDir.resolve(newName); // new name in same directory

            Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (Exception e) {
            System.err.println("Error while renaming file: " + e.getMessage());
            return false;
        }
    }
}
