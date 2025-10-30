package com.ringme.repository.sys;

import com.ringme.model.sys.UploadChunk;

import java.util.List;

public interface UploadChunkService {
    void saveChunkUpload(UploadChunk uploadChunk);

    List<UploadChunk> listFileChunk(Long uploadId);
}
