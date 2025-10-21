package com.filehub.client.filemanagement.controller;

import com.filehub.client.filemanagement.model.ApiResponse;
import com.filehub.client.filemanagement.model.FileData;
import com.filehub.client.filemanagement.model.ModifyFileRequest;
import com.filehub.client.filemanagement.service.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;

import static com.filehub.client.FMConstants.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private final Util util;
    public FileController(Util util) {
        this.util = util;
    }

    @PostMapping("/listfile")
    public ResponseEntity<ApiResponse> listFile(@RequestParam String path)
    {
        try
        {
            String url = SERVER_URL + "/file/filelist?filePath="+path;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, ApiResponse.class);
//            ArrayList<FileData> fileList = util.getFileList(responseEntity);
//            return ResponseEntity.ok(new ApiResponse<>(200,"File list",fileList));
        }catch (Exception e){
            return ResponseEntity.status(500).body((new ApiResponse<>(500, "Internal server error", e.getMessage())));
        }
    }

    @PostMapping("/downloadfile")
    public ResponseEntity<?> receiveFile(@RequestParam String path)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/file/downloadfile?filePath="+path;
        return restTemplate.exchange(url, HttpMethod.GET,null,byte[].class);

    }

    @PostMapping("/uploadfile")
    public ResponseEntity<?> sendFile(@RequestParam String file, @RequestParam String toPath)
    {
        return util.uploadFile(file,toPath);
    }


    @PostMapping("/modifyfile")
    public ResponseEntity<ApiResponse> transferFile(@RequestBody ModifyFileRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = SERVER_URL + "/file/modifyfile";

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    url,
                    request,
                    ApiResponse.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Internal server error", e.getMessage()));
        }
    }

}
