package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.service.FileService;
import cn.kk20.chat.base.http.ResultData;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2020/3/9 14:59
 * @Version: v1.0
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件相关controller")
@CrossOrigin
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping(value = "upload")
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    public ResultData upload(@RequestParam("file") MultipartFile file) throws Exception {
        String path = fileService.upload(file);
        return ResultData.success(path);
    }

    @PostMapping(value = "uploads")
    @ApiOperation(value = "上传多个文件", notes = "功能：上传多个文件")
    public ResultData uploads(@RequestPart(name = "files") MultipartFile[] files) throws Exception {
        Map<String, String> result = fileService.uploads(files);
        return ResultData.success(JSON.toJSONString(result));
    }

    @GetMapping("download")
    @ApiOperation(value = "下载文件", notes = "功能：下载文件")
    @ApiImplicitParam(name = "filePath", value = "下载文件绝对路径")
    public void download(@RequestParam String filePath, HttpServletResponse response) {
        try {
            File file = new File(filePath);
            String filename = file.getName();
            // 以流的形式下载文件
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[4096];
            // 清空response
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            String fileName = new String(filename.getBytes("gb2312"), "iso8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            OutputStream outputStream = response.getOutputStream();
            int readLength;
            while ((readLength = bis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
            bis.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
