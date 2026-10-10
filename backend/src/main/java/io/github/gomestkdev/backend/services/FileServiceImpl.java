package io.github.gomestkdev.backend.services;

import io.github.gomestkdev.backend.services.interfaces.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String cleanFileName = StringUtils.cleanPath(originalFileName);
        String randomID = UUID.randomUUID().toString();
        String fileExtension = cleanFileName.substring(cleanFileName.lastIndexOf("."));
        String fileName = randomID.concat(fileExtension);

        String filePath = path + File.separator + fileName;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}