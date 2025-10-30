package com.ringme.controller.sys;

import com.ringme.common.UploadFile;
import com.ringme.dto.UploadChunkDto;
import com.ringme.dto.UploadDto;
import com.ringme.model.sys.Upload;
import com.ringme.model.sys.UploadChunk;
import com.ringme.repository.sys.FileConcatenationService;
import com.ringme.repository.sys.UploadChunkRepository;
import com.ringme.repository.sys.UploadChunkService;
import com.ringme.repository.sys.UploadService;
import com.ringme.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/cdn/media")
public class UploadController {
    @Autowired
    UserService userService;

    @Autowired
    UploadService uploadService;

    @Autowired
    UploadChunkService uploadChunkService;

    @Autowired
    UploadChunkRepository uploadChunkRepository;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    FileConcatenationService fileConcatenationService;

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@Valid @ModelAttribute("uploadFile") UploadDto upload,
                                              Errors error) throws ParseException {
        log.info("uploadVideo| upload getFilePath: {}", upload.getFilePath());
//        String userId = userService.getId();
        Long userIdInt = userService.getId();
        if (error.hasErrors()) {
            return ResponseEntity.ok().body("Error Upload");
        } else {
            Path relativePath = uploadFile.getSavedPath(upload, "videos"); //lưu video lên server
            Upload uploadVideoSave = upload.convertToEntity();
            uploadVideoSave.setFileName(uploadVideoSave.getFileName());
            uploadVideoSave.setFileSize(uploadVideoSave.getFileSize());
            String filePath = upload.getFilePath();
            if(filePath != null && !filePath.isEmpty() && !filePath.equals("undefined")) {
                log.info("uploadVideo| filePath: {}", filePath);
                uploadVideoSave.setFilePath(filePath);
            } else
                uploadVideoSave.setFilePath(relativePath.toString());
            uploadVideoSave.setExtension(uploadVideoSave.getExtension());
            uploadVideoSave.setStatus(0);
            uploadVideoSave.setUserId(userIdInt);
            uploadService.saveUpload(uploadVideoSave);
            Long uploadId = uploadVideoSave.getId();
            return ResponseEntity.ok().body(String.valueOf(uploadId));
        }
    }

    @ResponseBody
    @PostMapping("/upload-chunk")
    public ResponseEntity<?> uploadChunkVideo(@Valid @ModelAttribute("uploadChunkFile") UploadChunkDto uploadChunk,
                                                   @RequestParam ("uploadId") Long uploadId,
                                                   @RequestParam("chunkIndex") Integer chunkNo,
                                                   @RequestParam("chunkSize") Long chunkSize,
                                                   @RequestParam("fileSize") Long fileSize,
                                                   @RequestParam MultipartFile fileChunkUpload,
                                                   Errors error) throws ParseException, IOException {
        if (error.hasErrors()) {
            return ResponseEntity.ok().body("Error Upload Chunk");
        } else {
            UploadChunk chunkVideoSave = uploadChunk.convertToEntity();
            Long getFileSize = uploadChunkRepository.checkFileSize(uploadId);
            if (getFileSize == null) {
                getFileSize = 0L; // Assign 0 if getFileSize is null
            }
            Path fileInfo = uploadFile.saveFileChunk2Storage(fileChunkUpload, "chunkFiles"); //lưu video lên server
            chunkVideoSave.setUploadId(uploadId);
            chunkVideoSave.setChunkNo(chunkNo);
            chunkVideoSave.setChunkExt(chunkVideoSave.getChunkExt());
            chunkVideoSave.setChunkSize(chunkSize);
            chunkVideoSave.setFileSize(fileSize);
            chunkVideoSave.setChunkExt(getFileExtension(fileChunkUpload.getOriginalFilename()));
            chunkVideoSave.setChunkPath(fileInfo.toString());
            uploadChunkService.saveChunkUpload(chunkVideoSave);
            //TODO: xử lý 2 t/h thành công và không thành công
            Map<String,String> map = new HashMap<>();
            if (fileSize == (getFileSize + chunkSize)){
                Upload up = fileConcatenationService.concatenationAllFile(uploadChunk.getUploadId());
                map.put("status","1");
                map.put("value",up.getFilePath());
                return ResponseEntity.ok().body(map);
            }
            map.put("status","0");
            map.put("value","in-progress");
            return ResponseEntity.ok().body(map);
        }
    }

    // Helper method to get file extension from filename
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex >= 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return ""; // Return empty string if no extension found
    }
}
