package com.filehub.client.filemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyFileRequest {
        private String sPath;
        private String dPath;
}
