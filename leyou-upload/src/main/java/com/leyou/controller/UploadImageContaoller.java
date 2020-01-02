package com.leyou.controller;

import com.leyou.service.UploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/19 15:05
 * @description: 图片
 */
@RestController
@RequestMapping
public class UploadImageContaoller {

    @Autowired
    private UploadImageService uploadImageService;


    /**
     * 下载图片
     *
     * @param file
     * @return
     */
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // 返回200，并且携带url路径
        return ResponseEntity.ok(this.uploadImageService.upload(file));
    }


    /**
     * 获取通过上传阿里云的签名
     *
     * @return
     */
    @GetMapping("/signature")
    public ResponseEntity<Map<String, Object>> getAliSignature() {
        return ResponseEntity.ok(uploadImageService.getSignature());
    }

}
