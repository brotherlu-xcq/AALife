package com.aalife.service.impl;

import com.aalife.exception.BizException;
import com.aalife.service.FileService;
import com.aalife.utils.UUIDUtil;
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
    @Override
    public String uploadFileToServer(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = "/detail/"+UUIDUtil.get16BitUUID();
        File newFile = new File(filePath+"/"+fileName);
        if (newFile.getParentFile() != null && !newFile.getParentFile().exists()) {
            boolean mkdirRes = newFile.getParentFile().mkdirs();
            if (!mkdirRes) {
                throw new BizException("创建目录失败，请联系运维。");
            }
        }
        String url = null;
        try {
            file.transferTo(newFile);
            url = newFile.toURI().toURL().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
