package cn.kk20.chat.api.controller;

import cn.kk20.chat.api.call.CallFileServerService;
import cn.kk20.chat.api.service.FileService;
import cn.kk20.chat.base.http.ResultData;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Roy
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

    @Autowired
    CallFileServerService callFileServerService;

    @GetMapping(value = "test")
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    public ResultData test(@RequestParam("filePath") String filePath) throws Exception {
        File file = new File(filePath);
        final String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true,
                substring);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        final ResultData upload = callFileServerService.upload(multipartFile);
        return upload;
    }

    @GetMapping(value = "test2")
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    public ResultData test2(@RequestParam("filePath") String filePath) throws Exception {
        File file = new File(filePath);
        final String substring = file.getName().substring(0, file.getName().lastIndexOf("."));
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true,
                substring);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        Map<String, Object> headers = new HashMap<>();
        headers.put("token", "royjnddnndndnfkajfajf;al");
        headers.put("refresh-token", "xnkdjfaf;ldafadjfal;fas;al");
        headers.put("custom", "rroy");
        final ResultData upload = callFileServerService.upload2(multipartFile, headers);
        return upload;
    }

    @PostMapping(value = "upload")
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    public ResultData upload(@RequestParam("file") MultipartFile file) throws Exception {
        String path = fileService.upload(file);
        return ResultData.success(path);
    }

    @PostMapping(value = "upload2")
    @ApiOperation(value = "上传单个文件", notes = "功能：上传单个文件")
    public ResultData upload2(@RequestParam("file") MultipartFile file, @RequestHeader Map<String, Object> header)
            throws Exception {
        final String token = (String) header.get("token");
        System.out.println("token==" + token);
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
