package com.ringme.repository.sys;

import com.ringme.model.sys.Upload;
import com.ringme.model.sys.UploadChunk;
import com.ringme.config.AppConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
public class FileConcatenationServiceImpl implements FileConcatenationService{
    @Autowired
    UploadChunkService uploadChunkService;
    @Autowired
    AppConfig configuration;
    @Autowired
    UploadService uploadService;

    @Override
    public Upload concatenationAllFile(Long uploadId) {
        List<UploadChunk> listUploadChunks = uploadChunkService.listFileChunk(uploadId);

        Upload uploadInfo = uploadService.fileUpload(uploadId);

            try {
                concatenateBlobFiles(listUploadChunks, uploadInfo);

                // TODO xoa thong tin chunk trong DB

            } catch (Exception e) {
                log.error("ERROR|" + e.getMessage(), e);
            }
            return uploadInfo;
    }

    public void concatenateBlobFiles(List<UploadChunk> uploadChunks, Upload uploadInfo) throws IOException {
        Path obsoluteOutputPath = Paths.get(configuration.getRootPath() + "/" + uploadInfo.getFilePath());
        Path folderPath = obsoluteOutputPath.getParent();
        if(!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        try (OutputStream outputStream = Files.newOutputStream(obsoluteOutputPath)) {
            for (UploadChunk uploadChunk : uploadChunks) {
//                Path obsoulteChunkPath = Paths.get(configuration.getRootPath() + "/" + uploadChunk.getChunkPath());
                Path obsoulteChunkPath = Paths.get(uploadChunk.getChunkPath());
                log.info("MERGE|" + "|obsoulteChunkPath = " + obsoulteChunkPath + "|obsoluteOutputPath = " + obsoluteOutputPath);

                try (InputStream inputStream = Files.newInputStream(obsoulteChunkPath)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();

                } catch (Exception e) {
                    log.error("ERROR|" + e.getMessage(), e);
                }
                log.info("DELETE|" + "|obsoulteChunkPath = " + obsoulteChunkPath);
                Files.delete(obsoulteChunkPath);

            }
        }  catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
}
