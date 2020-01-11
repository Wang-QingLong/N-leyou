package com.leyou.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * <p>
 * 这里我们假设用户信息包含3部分：
 * <p>
 * - id：用户id
 * - username：用户名
 * - role：角色（权限中会使用）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long id;

    private String username;

    private String role;
}