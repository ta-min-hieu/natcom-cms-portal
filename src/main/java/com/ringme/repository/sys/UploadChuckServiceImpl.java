package com.ringme.repository.sys;

import com.ringme.model.sys.UploadChunk;
import com.ringme.repository.sys.UploadChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadChuckServiceImpl implements UploadChunkService {

    @Autowired
    UploadChunkRepository uploadChuckRepository;

    @Override
    public void saveChunkUpload(UploadChunk uploadChunk) {
        uploadChuckRepository.save(uploadChunk);
    }

    @Override
    public List<UploadChunk> listFileChunk(Long uploadId) {
        return uploadChuckRepository.listFileChunk(uploadId);
    }
}
