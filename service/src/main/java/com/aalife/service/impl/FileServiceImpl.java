package com.aalife.service.impl;

import com.aalife.exception.BizException;
import com.aalife.service.FileService;
import com.aalife.utils.UUIDUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author mosesc
 * @date 2018-07-17
 */
@Service
public class FileServiceImpl implements FileService {
    private static Logger logger = Logger.getLogger(FileService.class);
    @Value("${file.baseDir}")
    private String baseDir;

    @Override
    public String uploadFileToServer(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = "/detail/"+UUIDUtil.get16BitUUID();
        File newFile = new File(baseDir+filePath+"/"+fileName);
        if (newFile.getParentFile() != null && !newFile.getParentFile().exists()) {
            boolean mkdirRes = newFile.getParentFile().mkdirs();
            if (!mkdirRes) {
                throw new BizException("创建目录失败，请联系运维。");
            }
        }
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.error("保存图片失败", e);
            throw new BizException("保存图片失败", e);
        }
        return filePath+"/"+fileName;
    }

}
