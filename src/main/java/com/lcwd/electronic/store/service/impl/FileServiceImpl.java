package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dto.ApiConstant;
import com.lcwd.electronic.store.exception.BadApiRequest;
import com.lcwd.electronic.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        //abc.png
        String originalFilename = file.getOriginalFilename();
        logger.info("fileName:{}", originalFilename);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

            //save file
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

            logger.info("Completed request for file upload");

            return fileNameWithExtension;

        } else {
            throw new BadApiRequest(ApiConstant.EXTENSION + extension);
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        logger.info("inside getResource()");

        String fullPath = path + File.separator + name;

        InputStream inputStream = new FileInputStream(fullPath);

        logger.info("Completed request for getResource()");

        return inputStream;
    }
}
