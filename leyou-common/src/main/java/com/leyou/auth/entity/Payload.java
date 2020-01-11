package com.leyou.auth.entity;

import lombok.Data;

import java.util.Date;


/**
 * 载荷对象：
 * <p>
 * JWT中，会保存载荷数据，我们计划存储3部分：
 * <p>
 * - id：jwt的id
 * - 用户信息：用户数据，不确定，可以是任意类型
 * - 过期时间：Date
 *
 * @param <T>
 */
@Data
public class Payload<T> {
    private String id; //tokenId
    private T info; //实际存放内容
    private Date expiration; //过期时间
}