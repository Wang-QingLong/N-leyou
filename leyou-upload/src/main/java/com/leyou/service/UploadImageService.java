package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/19 15:16
 * @description:
 */
public interface UploadImageService {
    String upload(MultipartFile file);

    Map<String, Object> getSignature();

}
