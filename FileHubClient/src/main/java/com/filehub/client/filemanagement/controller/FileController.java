package com.filehub.client.filemanagement.controller;

import com.filehub.client.filemanagement.model.ApiResponse;
import com.filehub.client.filemanagement.model.FileData;
import com.filehub.client.filemanagement.service.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;

import static com.filehub.client.filemanagement.FMConstants.*;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private final Util util;
    public FileController(Util util) {
        this.util = util;
    }

    @GetMapping("/listfile")
    public ResponseEntity<ApiResponse> listFile(@RequestParam String path)
    {
        try
        {
            String url = SERVER_URL + "/file/filelist?filePath="+path;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(url, ApiResponse.class);
            ArrayList<FileData> fileList = util.getFileList(responseEntity);
            return ResponseEntity.ok(new ApiResponse<>(200,"File list",fileList));
        }catch (Exception e){
            return ResponseEntity.status(500).body((new ApiResponse<>(500, "Internal server error", e.getMessage())));
        }
    }

    @GetMapping("/downloadfile")
    public ResponseEntity<?> receiveFile(@RequestParam String path)
    {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/file/downloadfile?filePath="+path;
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET,null,byte[].class);
        return util.downloadFile(response);
    }

    @PostMapping("/uploadfile")
    public ResponseEntity<?> sendFile(@RequestParam String file, @RequestParam String toPath)
    {
        return util.uploadFile(file,toPath);
    }

    @GetMapping("/deletefile")
    public ResponseEntity<ApiResponse> deleteFile(@RequestParam String path)
    {
        try
        {
            File file = new File(path);
            // Check if file exists
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "File not found", null));
            }
            util.deleteFile(file);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "File deleted successfully", file.getAbsolutePath()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Internal server error", e.getMessage()));
        }
    }

    @GetMapping("/transferfile")
    public ResponseEntity<ApiResponse> transferFile(@RequestParam String sPath,@RequestParam String dPath,@RequestParam boolean move) {
        try {

            File file = new File(sPath);
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "File not found", null));
            }
            boolean isCopied = util.transferFile(sPath, dPath, move);
            if (isCopied)
                return ResponseEntity.ok(new ApiResponse<>(200, "File transferred  successfully", dPath));
            return ResponseEntity.ok(new ApiResponse<>(401, "Failed to transfer", sPath));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Internal server error", e.getMessage()));
        }
    }

    @GetMapping("/renamefile")
    public ResponseEntity<ApiResponse> renameFile(@RequestParam String sPath,@RequestParam String dPath,@RequestParam boolean move)
    {
        try {
            File file = new File(sPath);
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "File not found", null));
            }
            boolean isCopied = util.transferFile(sPath,dPath,move);
            if (isCopied)
                return ResponseEntity.ok(new ApiResponse<>(200, "File transferred  successfully", dPath));
            return ResponseEntity.ok(new ApiResponse<>(401, "Failed to transfer", sPath));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Internal server error", e.getMessage()));
        }
    }
}
