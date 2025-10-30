package com.ringme.repository.sys;

import com.ringme.model.sys.Upload;

public interface UploadService {
    void saveUpload(Upload upload);

    Upload fileUpload(Long uploadId);
}
