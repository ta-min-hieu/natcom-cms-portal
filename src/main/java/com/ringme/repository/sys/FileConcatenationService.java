package com.ringme.repository.sys;

import com.ringme.model.sys.Upload;

import java.io.IOException;

public interface FileConcatenationService {
    Upload concatenationAllFile (Long uploadId) throws IOException;
}
