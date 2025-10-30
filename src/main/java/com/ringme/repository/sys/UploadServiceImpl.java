package com.ringme.repository.sys;

import com.ringme.model.sys.Upload;
import com.ringme.repository.sys.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    UploadRepository uploadRepository;

    @Override
    public void saveUpload(Upload upload) {
        uploadRepository.save(upload);
    }

    @Override
    public Upload fileUpload(Long uploadId) {
        return uploadRepository.fileUpload(uploadId);
    }
}
