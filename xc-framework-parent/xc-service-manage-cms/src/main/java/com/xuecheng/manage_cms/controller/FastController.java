package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

@RequestMapping("/fastDFS")
@CrossOrigin
@RestController
public class FastController {
    private static Logger log = LoggerFactory.getLogger(FastController.class);
    /**
     * 上传文件到FastDFS
     *
     * @param file
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult fastDFSUpload(MultipartFile file) {

        String ext_Name = file.getOriginalFilename().split("\\.")[1];
        String file_Name = file.getOriginalFilename().split("\\.")[0];

        byte[] bytes = null;
        Long fileLength = null;
        try {
            bytes = file.getBytes();
            fileLength = file.getSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath= uploadFile(bytes, file.getOriginalFilename(), fileLength);
        log.warn("文件路劲："+filePath);

        CommonCode success = CommonCode.SUCCESS;
        success.setMessage(filePath);
        return new ResponseResult(success);
    }

    /**
     * FastDFS实现文件下载
     *
     * @param filePath
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public void fastDFSDownload(@RequestParam("group") String group, @RequestParam("filePath") String filePath) {
        try {
            ClientGlobal.initByProperties("fastDFS.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);
            byte[] b = storageClient.download_file(group, filePath);
            if (b == null) {
                throw new IOException("文件" + filePath + "不存在");
            }
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            FileOutputStream fileOutputStream = new FileOutputStream("c://" + fileName);
            IOUtils.write(b, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * FastDFS获取将上传文件信息
     */
    @RequestMapping(value = "/getFileInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult fastDFSGetFileInfo(@RequestParam("group") String group, @RequestParam("filePath") String filePath) {
        try {
            // 链接FastDFS服务器，创建tracker和Stroage
            ClientGlobal.initByProperties("fastDFS.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);

            FileInfo fi = storageClient.get_file_info(group, filePath);
            if (fi == null) {
                throw new IOException("文件" + filePath + "不存在");
            }

            log.debug("文件信息=" + fi.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult();
    }

    /**
     * FastDFS获取文件名称
     */
    @RequestMapping(value = "/getFileName", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult fastDFSGetFileName(@RequestParam("group") String group, @RequestParam("filePath") String filePath) {
        try {
            // 链接FastDFS服务器，创建tracker和Stroage
            ClientGlobal.initByProperties("fastDFS.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);

            NameValuePair[] nvps = storageClient.get_metadata(group, filePath);
            if (nvps == null) {
                throw new IOException("文件" + filePath + "不存在");
            }

            log.debug(nvps[0].getName() + "." + nvps[0].getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseResult();
    }

    /**
     * FastDFS实现删除文件
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult fastDFSDelete(@RequestParam("group") String group, @RequestParam("filePath") String filePath) {
        int i = 22;
        try {
            // 初始化文件资源
            ClientGlobal.initByProperties("fastDFS.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);

            i = storageClient.delete_file(group, filePath);
            log.debug(i != 22 ? "删除成功" : "删除失败:" + i);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseResult(i != 22 ? CommonCode.SUCCESS:CommonCode.FAIL);
    }

    public String uploadFile(byte[] byteFile, String file_Name, Long fileLength) {

        // 拼接服务区的文件路径
        StringBuffer imgPath = new StringBuffer();
        try {
            // 初始化文件资源
            ClientGlobal.initByProperties("fastDFS.properties");
            TrackerGroup g_tracker_group = ClientGlobal.getG_tracker_group();
            InetSocketAddress tracker_server = g_tracker_group.tracker_servers[0];
            imgPath.append("http:/"+tracker_server.getAddress()+"/");

            //System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            // 链接FastDFS服务器，创建tracker和Stroage
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer connection = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(connection, null);

            //利用字节流上传文件
            String ext = FilenameUtils.getExtension(file_Name);
            NameValuePair[] metaList  = new NameValuePair[3];
            metaList[0] = new NameValuePair("fileName", file_Name);
            metaList[1] = new NameValuePair("fileExtName", ext);
            metaList[2] = new NameValuePair("fileLength", String.valueOf(fileLength));
            if(storageClient.isConnected()) {
                String[] strings = storageClient.upload_file(byteFile, ext, null );
                imgPath.append(String.join("/", strings));
            }
            log.debug("上传路径=" + imgPath.toString());
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return imgPath.toString();
    }


}
