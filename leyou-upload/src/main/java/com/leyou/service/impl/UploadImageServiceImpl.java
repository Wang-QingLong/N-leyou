package com.leyou.service.impl;

import com.leyou.config.OSSProperties;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.service.UploadImageService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/19 15:17
 * @description:
 */
@Service
public class UploadImageServiceImpl implements UploadImageService {

    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg", "image/bmp");



    @Autowired
    private OSSProperties prop;

    @Autowired
    private OSS client;

    /**
     * 下载图片
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        //1，图片信息校验
        //1.1检验文件类型
        String type = file.getContentType();
        if (!suffixes.contains(type)) {
            throw new LyException(ExceptionEnum.IMAGE_TYPE_DISQUALIFIED);
        }
        //2.1校验图片内容:ImageIo是专门用来读取图片的，如果可以读取，说明是图片
        BufferedImage image = null;

        try {
            image = ImageIO.read(file.getInputStream());
        } catch (IOException e) {  //说明给的资源是空的
            throw new LyException(ExceptionEnum.IMAGE_CONTENT_BE_NULL);
        }
        if (image == null) { //说明给的不是图片不能读取
            throw new LyException(ExceptionEnum.IMAGE_CONTENT_BE_NULL);
        }

        // 2、保存图片
        // 2.1、生成保存目录,保存到nginx所在目录的html下，这样可以直接通过nginx来访问到
        File dir = new File("E:\\nginx-1.12.2\\nginx-1.12.2\\html");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            // 2.2、
            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            return "http://image.leyou.com/" + file.getOriginalFilename();
        } catch (Exception e) {  //图片上传异常
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }

    }

    /**
     * 获取通过阿里云上传图片的签名
     *
     * @return
     */
    @Override
    public Map<String, Object> getSignature() {

        try {
            long expireTime = prop.getExpireTime();
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, prop.getMaxFileSize());
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, prop.getDir());

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, Object> respMap = new LinkedHashMap<>();
            respMap.put("accessId", prop.getAccessKeyId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", prop.getDir());
            respMap.put("host", prop.getHost());
            respMap.put("expire", expireEndTime);
            return respMap;
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}

