package com.aalife.web.publicapi;

import com.aalife.service.FileService;
import com.aalife.web.util.JsonEntity;
import com.aalife.web.util.ResponseHelper;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mosesc
 * @date 2018-07-17
 */
@RequiresAuthentication
@RequestMapping(value = "/public/api")
@Api
@RestController
public class FileApi {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload/picture", method = RequestMethod.POST)
    public JsonEntity<String> uploadPicture(MultipartFile file){
        return ResponseHelper.createInstance(fileService.uploadFileToServer(file));
    }
}
