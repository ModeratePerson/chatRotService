package com.moderatePerson.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// 文件上传
@Service
public class FileUploadService {
    private final String uploadDir = "src/main/resources/file"; // 设置为你的文件上传目录

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // 保存文件到本地文件系统
        Path destinationFile = Paths.get(uploadDir)
                .resolve(Paths.get(file.getOriginalFilename()))
                .normalize()
                .toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        // 生成文件URL
        return destinationFile.toUri().toString();
    }
}
