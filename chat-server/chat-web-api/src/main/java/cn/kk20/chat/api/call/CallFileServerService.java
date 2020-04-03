package cn.kk20.chat.api.call;

import cn.kk20.chat.base.http.ResultData;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/16 11:13
 * @Version: v1.0
 */
@FeignClient(name = "file", path = "file", url = "localhost:8081")
public interface CallFileServerService {

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers("Content-Type: multipart/form-data")
    ResultData upload(@RequestPart("file") MultipartFile file);

}
