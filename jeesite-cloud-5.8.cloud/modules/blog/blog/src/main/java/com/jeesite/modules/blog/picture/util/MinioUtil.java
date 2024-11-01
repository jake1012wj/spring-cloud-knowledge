package com.jeesite.modules.blog.picture.util;

import com.jeesite.modules.blog.base.global.Constants;
import com.jeesite.modules.blog.commons.entity.SystemConfig;
import com.jeesite.modules.blog.picture.global.MessageConf;
import com.jeesite.modules.blog.utils.FileUtils;
import com.jeesite.modules.blog.utils.ResultUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.messages.DeleteObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 本地对象存储服务 Minio上传工具类
 *
 * @author: 陌溪
 * @create: 2020-10-19-10:22
 */
@Component

public class MinioUtil {
    private static Logger log = LoggerFactory.getLogger(MinioUtil.class);

    @Autowired
    FeignUtil feignUtil;

    /**
     * 文件上传
     *
     * @param data
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile data) {
        return this.uploadSingleFile(data);
    }

    /**
     * 批量文件上传
     *
     * @param list
     * @return
     * @throws Exception
     */
    public String batchUploadFile(List<MultipartFile> list) {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : list) {
            urlList.add(this.uploadSingleFile(file));
        }
        return ResultUtil.successWithData(urlList);
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     * @return
     */
    public String deleteFile(String fileName) {
        try {
            // 获取系统配置
            SystemConfig systemConfig = feignUtil.getSystemConfig();
            MinioClient minioClient = MinioClient.builder().endpoint(systemConfig.getMinioEndPoint()).credentials(systemConfig.getMinioAccessKey(), systemConfig.getMinioSecretKey()).build();

            // Remove object.
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(systemConfig.getMinioBucket()).object(fileName).build());
        } catch (Exception e) {
            log.error("删除Minio中的文件失败 fileName: {}, 错误消息: {}", fileName, e.getMessage());
            return ResultUtil.errorWithData(MessageConf.DELETE_DEFAULT_ERROR);
        }
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    /**
     * 批量删除文件
     *
     * @param fileNameList
     * @return
     */
    public String deleteBatchFile(List<String> fileNameList) {
        // 获取系统配置
        SystemConfig systemConfig = feignUtil.getSystemConfig();
        MinioClient minioClient = MinioClient.builder().endpoint(systemConfig.getMinioEndPoint()).credentials(systemConfig.getMinioAccessKey(), systemConfig.getMinioSecretKey()).build();
        try {
            List<DeleteObject> objects = new LinkedList<>();
            for (String fileName : fileNameList) {
                objects.add(new DeleteObject(fileName));
            }
            minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(systemConfig.getMinioBucket()).objects(objects).build());
        } catch (Exception e) {
            log.error("批量删除文件失败, 错误消息: {}", e.getMessage());
            return ResultUtil.errorWithData(MessageConf.DELETE_DEFAULT_ERROR);
        }
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    /**
     * 上传单个文件，返回上传成功后的地址
     *
     * @param multipartFile
     * @return
     */
    private String uploadSingleFile(MultipartFile multipartFile) {

        String url = "";
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            SystemConfig systemConfig = feignUtil.getSystemConfig();
            MinioClient minioClient = MinioClient.builder().endpoint(systemConfig.getMinioEndPoint()).credentials(systemConfig.getMinioAccessKey(), systemConfig.getMinioSecretKey()).build();
            String oldName = multipartFile.getOriginalFilename();
            //获取扩展名，默认是jpg
            String picExpandedName = FileUtils.getPicExpandedName(oldName);
            //获取新文件名
            String newFileName = System.currentTimeMillis() + Constants.SYMBOL_POINT + picExpandedName;

            // 重新生成一个文件名
            InputStream inputStram = multipartFile.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(systemConfig.getMinioBucket()).object(newFileName).stream(
                            inputStram, multipartFile.getSize(), -1)
                            .contentType(multipartFile.getContentType())
                            .build());
            url = Constants.SYMBOL_LEFT_OBLIQUE_LINE + systemConfig.getMinioBucket() + Constants.SYMBOL_LEFT_OBLIQUE_LINE + newFileName;
        } catch (Exception e) {
            e.getStackTrace();
            log.error("上传文件出现异常: {}", e.getMessage());
        }
        return url;
    }
}
