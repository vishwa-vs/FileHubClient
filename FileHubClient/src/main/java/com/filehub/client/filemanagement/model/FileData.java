package com.filehub.client.filemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileData {

    private String fileName;
    private String filePath;
    private long fileSize;
    private long lastModified;
    private boolean isFile;

    public FileData() {
    }

    public boolean isFile() {
        return this.isFile;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FileData;
    }

}
