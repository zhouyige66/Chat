package cn.kk20.chat.api.service.impl;

import cn.kk20.chat.api.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/19 17:23
 * @Version: v1.0
 */
@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.parentPath}")
    private String parentPath;

    @Override
    public String upload(MultipartFile file) throws Exception {
        if (null == file) {
            throw new Exception("上传文件为空");
        }

        Resource resource = file.getResource();
        String fileName = resource.getFilename();
        String fileSavePath;
        String contentType = file.getContentType();
        if (!StringUtils.isEmpty(contentType)) {
            String type = contentType.split("/")[0];
            fileSavePath = parentPath + type + File.separator + fileName;
        } else {
            fileSavePath = parentPath + "unknown" + File.separator + fileName;
        }
        logger.info("保存文件路径为：{}", fileSavePath);
        File saveFile = new File(fileSavePath);
        saveFile.getParentFile().mkdirs();
        try (
                InputStream inputStream = file.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(saveFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            return fileSavePath;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Map<String, String> uploads(MultipartFile... multipartFiles) {
        Map<String, String> resultMap = new HashMap<>();
        for (MultipartFile file : multipartFiles) {
            Resource resource = file.getResource();
            String filename = resource.getFilename();
            try {
                String upload = upload(file);
                resultMap.put(filename, upload);
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put(filename, "");
            }
        }
        return resultMap;
    }

}
