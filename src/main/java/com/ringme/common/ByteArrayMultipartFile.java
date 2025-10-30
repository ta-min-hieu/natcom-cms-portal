package com.ringme.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

public class ByteArrayMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;
    private final long size; // Thêm dung lượng file
    private final Map<String, String> headers; // Thêm metadata HTTP headers (nếu cần)

    public ByteArrayMultipartFile(String name, String originalFilename, String contentType, byte[] content, Map<String, String> headers) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
        this.size = content.length;
        this.headers = headers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}

