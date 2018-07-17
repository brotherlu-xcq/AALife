package com.aalife.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author mosesc
 * @date 2018-07-17
 */
public interface FileService {
    /**
     * 上传文件到服务器
     * @param file
     * @return
     */
    String uploadFileToServer(MultipartFile file);
}
