package cn.kk20.chat.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/19 17:21
 * @Version: v1.0
 */
public interface FileService {

    String upload(MultipartFile file) throws Exception;

    Map<String,String> uploads(MultipartFile... multipartFiles);

}
