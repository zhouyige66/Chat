package cn.kk20.chat.api.call;

import cn.kk20.chat.base.http.ResultData;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

    @PostMapping(value = "upload2",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    ResultData upload2(@RequestPart("file") MultipartFile file, @RequestHeader Map<String, Object> header);
}
