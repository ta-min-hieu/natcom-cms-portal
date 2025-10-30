package com.ringme.common;

import com.ringme.config.AppConfig;
import com.ringme.dto.UploadDto;
import lombok.extern.log4j.Log4j2;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
public class UploadFile {
    @Autowired
    AppConfig appConfig;

    @Deprecated
    public void upload(MultipartFile image, String[] fileName) {
        try {
            if(fileName == null)
                return;
            Path path = Paths.get(appConfig.getRootPath() + "/" + fileName[0]);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            path = Paths.get(appConfig.getRootPath() + "/" + fileName[1]);
            try (OutputStream os = Files.newOutputStream(path)) {
                os.write(image.getBytes());
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    @Deprecated
    public String[] saveContractFile(MultipartFile image, String type){
        try {
            log.info("appConfiguration|" + appConfig.getFileInDBPrefix());
            if(image == null)
                return null;
            String originalFilename = image.getOriginalFilename();
            if(originalFilename == null || originalFilename.equals(""))
                return null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if(fileExtension == null || fileExtension.isEmpty())
                return null;
            String fileName = Helper.generateRandomString(32);
            String time = Helper.getTimeNow();
            Path staticPath = Paths.get(appConfig.getFileInDBPrefix());
            log.info("staticPath|" + staticPath);
            Path imagePath = Paths.get(type + "/" + time);
            String[] file = new String[3];
            file[0] = staticPath.resolve(imagePath).toString().replaceAll("\\\\", "/");
            file[1] = staticPath.resolve(imagePath).resolve(fileName + "." + fileExtension).toString().replaceAll("\\\\", "/");
            file[2] = originalFilename;
            return file;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path createImageFile(String thumbUpload, String type) {
        try {
            if(thumbUpload == null || thumbUpload.equals(""))
                return null;
            String[] dataArray = thumbUpload.trim().split(",");
            String imgBase64 = "";
            String fileExtension = "jpg";
            if (dataArray.length > 1) {
                imgBase64 = dataArray[1];
                fileExtension = dataArray[0].replace("data:image/", "").replace(";base64", "");
            } else {
                imgBase64 = thumbUpload;
            }
            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();
            Path relativePath = Paths.get(appConfig.getFileInDBPrefix()).resolve(type).resolve(timePath);
            relativePath = relativePath.resolve(fileName + "." + fileExtension);
            Path obsoluteSavePath = Paths.get(appConfig.getRootPath() + "/" + relativePath);
            Path folderParent = obsoluteSavePath.getParent();
            if(!Files.exists(folderParent)) {
                Files.createDirectories(folderParent);
            }
            log.info("PATH|" + "|relativePath = " + relativePath + "|obsolutePath = " + obsoluteSavePath);

            try (OutputStream os = Files.newOutputStream(obsoluteSavePath)) {
                os.write(Base64.getDecoder().decode(imgBase64));
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }
            return relativePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path getSavedPath(UploadDto data, String type){
        try {
            String filename = data.getFileName();
            log.info("relativePath| filename: {}", filename);
            if(!StringUtils.hasLength(filename))
                return null;

            String fileExtension = filename.substring(filename.lastIndexOf(".") + 1);

            if(fileExtension == null || fileExtension.isEmpty())
                return null;

            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();
            Path relativePath = Paths.get(appConfig.getFileInDBPrefix()).resolve(type).resolve(timePath);

            relativePath = relativePath.resolve(fileName + "." + fileExtension);

            log.info("relativePath| relativePath: {}", relativePath);
            return relativePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path saveFileChunk2Storage(MultipartFile fileChunk, String type){
        try {
            String originalFilename = fileChunk.getOriginalFilename();

            if(!StringUtils.hasLength(originalFilename))
                return null;

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            if(!StringUtils.hasLength(fileExtension))
                return null;

            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();

            Path relativePath = Paths.get(appConfig.getFileInDBPrefix()).resolve(type).resolve(timePath);

            relativePath = relativePath.resolve(fileName + "." + fileExtension);

            Path obsolutePath = Paths.get(appConfig.getRootPath() + "/" + relativePath);

            log.info("CHUNK|" + fileChunk.getContentType() + "|relativePath = " + relativePath + "|obsolutePath = " + obsolutePath);
            Path folderPath = obsolutePath.getParent();
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            try (OutputStream os = Files.newOutputStream(obsolutePath)) {
                os.write(fileChunk.getBytes());
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
                return null;
            }

            return obsolutePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public String[] createImageFileInfo(String thumbUpload, String type) {
        try {
            if(thumbUpload == null || thumbUpload.equals(""))
                return null;
            String imgBase64 = thumbUpload.trim().replace("data:image/jpeg;base64,", "");
            byte[] imageData = Base64.getDecoder().decode(imgBase64);

            String fileExtension = "jpg"; // You can change this based on the image type
            String fileName = Helper.generateRandomString(32);
            String time = Helper.getTimeNow();
            Path staticPath = Paths.get(appConfig.getFileInDBPrefix());
            Path imagePath = Paths.get(type + "/" + time);

            String[] fileInfo = new String[3];
            fileInfo[0] = staticPath.resolve(imagePath).toString().replaceAll("\\\\", "/");
            fileInfo[1] = staticPath.resolve(imagePath).resolve(fileName + "." + fileExtension).toString().replaceAll("\\\\", "/");
            fileInfo[2] = fileName + "." + fileExtension;

            return fileInfo;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    public void uploadBase64(String thumbUpload, String[] fileName) {
        try {
//            Path CURRENT_FOLDER = processFilePath();
            if (fileName == null) {
                return;
            }
            Path path = Paths.get(appConfig.getRootPath() + "/" + fileName[0]);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            path = Paths.get(appConfig.getRootPath() + "/" + fileName[1]);
            try (OutputStream os = Files.newOutputStream(path)) {
                os.write(Base64.getDecoder().decode(thumbUpload.trim().replace("data:image/jpeg;base64,", "")));
            }
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    @Deprecated
    public String[] fileName(MultipartFile image, String type){
        try {
            log.info("appConfiguration|" + appConfig.getFileInDBPrefix());
            String originalFilename = image.getOriginalFilename();
            if(originalFilename == null || originalFilename.equals(""))
                return null;
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if(fileExtension == null || fileExtension.isEmpty())
                return null;
            String fileName = Helper.generateRandomString(32);
            String time = Helper.getTimeNow();
            Path staticPath = Paths.get(appConfig.getFileInDBPrefix());
            log.info("staticPath|" + staticPath);
            Path imagePath = Paths.get(type + "/" + time);
            String[] file = new String[3];
            file[0] = staticPath.resolve(imagePath).toString().replaceAll("\\\\", "/");
            file[1] = staticPath.resolve(imagePath).resolve(fileName + "." + fileExtension).toString().replaceAll("\\\\", "/");
            file[2] = originalFilename;
            return file;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path createImageFileV1(String thumbUpload, String type, String prefix, String rootPath) {
        try {
            if(thumbUpload == null || thumbUpload.isEmpty())
                return null;
            String[] dataArray = thumbUpload.trim().split(",");
            String imgBase64 = "";
            String fileExtension = "jpg";
            if (dataArray.length > 1) {
                imgBase64 = dataArray[1];
                fileExtension = dataArray[0].replace("data:image/", "").replace(";base64", "");
            } else {
                imgBase64 = thumbUpload;
            }
            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();
            Path relativePath = Paths.get(prefix).resolve(type).resolve(timePath);
            relativePath = relativePath.resolve(fileName + "." + fileExtension);
            Path obsoluteSavePath = Paths.get(rootPath + "/" + relativePath);
            Path folderParent = obsoluteSavePath.getParent();
            if(!Files.exists(folderParent)) {
                Files.createDirectories(folderParent);
            }
            log.info("PATH|" + "|relativePath = " + relativePath + "|obsolutePath = " + obsoluteSavePath);

            try (OutputStream os = Files.newOutputStream(obsoluteSavePath)) {
                os.write(Base64.getDecoder().decode(imgBase64));
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }
            return relativePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    public Path uploadAudioFile(MultipartFile file, String type){
        try {
            String originalFilename = file.getOriginalFilename();

            if(!StringUtils.hasLength(originalFilename))
                return null;

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            if(!StringUtils.hasLength(fileExtension))
                return null;

            String fileName = Helper.generateRandomString(32);
            Path timePath = Helper.getPathByTime();

            Path relativePath = Paths.get(appConfig.getFileInDBPrefix()).resolve(type).resolve(timePath);

            relativePath = relativePath.resolve(fileName + "." + fileExtension);

            Path obsolutePath = Paths.get(appConfig.getRootPath() + "/" + relativePath);

            log.info("AUDIO|" + file.getContentType() + "|relativePath = " + relativePath + "|obsolutePath = " + obsolutePath);
            Path folderPath = obsolutePath.getParent();
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            try (OutputStream os = Files.newOutputStream(obsolutePath)) {
                os.write(file.getBytes());
                os.flush();
            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
                return null;
            }

            return obsolutePath;
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return null;
    }

    // Hàm xử lý file upload và chuyển đổi charset, trả về MultipartFile
    public MultipartFile handleFileUpload(MultipartFile file) {
        try {
            // Phát hiện charset của file tự động
            String detectedCharset = detectCharset(file);

            // Đọc nội dung file với charset tự động phát hiện
            Charset inputCharset = Charset.forName(detectedCharset);
            InputStreamReader reader = new InputStreamReader(file.getInputStream(), inputCharset);

            // Đọc toàn bộ nội dung của file
            BufferedReader bufferedReader = new BufferedReader(reader);
            String content = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));

            bufferedReader.close();

            // Chuyển nội dung sang UTF-8
            byte[] utf8Content = content.getBytes(StandardCharsets.UTF_8);

            // Tạo MultipartFile từ nội dung đã chuyển đổi sang UTF-8
            // Tên của file
            // Tên gốc của file
            // Content type của file
            // Dữ liệu byte của file
            String contentType = "text/plain; charset=UTF-8";
            return new ByteArrayMultipartFile(
                    file.getName(),  // Tên của file
                    file.getOriginalFilename(),  // Tên gốc của file
                    contentType,  // Content type của file
                    utf8Content,  // Dữ liệu byte của file
                    Map.of("Content-Type", contentType)
            );  // Trả về MultipartFile đã chuyển đổi
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
            return null;
        }
    }

    // Hàm phát hiện charset của file
    private String detectCharset(MultipartFile file) throws IOException {
        UniversalDetector detector = new UniversalDetector(null);

        // Đọc file và truyền vào detector để phát hiện charset
        byte[] buffer = new byte[4096];
        try (InputStream is = file.getInputStream()) {
            int nread;
            while ((nread = is.read(buffer)) > 0 && !detector.isDone()) {
                detector.handleData(buffer, 0, nread);
            }
            detector.dataEnd();
        }

        // Trả về charset phát hiện được
        String detectedCharset = detector.getDetectedCharset();
        log.info("detected charset = " + detectedCharset);
        return detectedCharset != null ? detectedCharset : "UTF-8"; // Mặc định là UTF-8 nếu không phát hiện được
    }
}