package cn.objectspace.webssh.controller;


import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class UploadController {


    @RequestMapping("/files")
    public ResponseEntity upload1(@RequestParam("files") MultipartFile[] file, HttpServletResponse response) throws Exception {

        List<String> urlList = new ArrayList<>();
        String path = "D:\\var\\uploaded_files\\";
        for (MultipartFile multipartFile : file) {
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String name = path + originalFilename;
            IOUtils.write(bytes, new FileOutputStream(name));
            urlList.add(Base64Utils.encodeToString(name.getBytes(StandardCharsets.UTF_8)));
        }
        return ResponseEntity.ok(urlList);
    }
}
