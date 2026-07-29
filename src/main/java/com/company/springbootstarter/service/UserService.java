package com.company.springbootstarter.service;

import com.company.springbootstarter.dto.UserCreateRequest;
import com.company.springbootstarter.dto.UserResponse;
import com.company.springbootstarter.dto.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * 创建新用户
     *
     * @param request 包含新用户信息的创建请求对象
     * @return 创建成功后的用户响应对象
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * 获取所有用户列表
     *
     * @return 用户响应对象的集合，如果没有用户则返回空列表
     */
    List<UserResponse> getUsers();

    /**
     * 根据用户唯一标识符获取用户信息
     *
     * @param id 用户的唯一标识符 (UUID)
     * @return 匹配的用户响应对象
     */
    UserResponse getUserById(UUID id);

    /**
     * 更新指定用户的信息
     *
     * @param id 需要更新的用户唯一标识符 (UUID)
     * @param request 包含需要更新字段信息的请求对象
     * @return 更新后的用户响应对象
     */
    UserResponse updateUser(UUID id, UserUpdateRequest request);

    /**
     * 根据用户唯一标识符删除用户
     *
     * @param id 需要删除的用户唯一标识符 (UUID)
     */
    void deleteUser(UUID id);
}
