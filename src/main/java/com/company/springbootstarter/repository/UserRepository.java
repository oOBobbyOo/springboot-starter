package com.company.springbootstarter.repository;

import com.company.springbootstarter.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * 根据邮箱地址查询用户信息。
     *
     * @param email 用户邮箱地址，不能为空
     * @return 包含用户的 {@link Optional} 对象，如果未找到则返回 {@link Optional#empty()}
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查指定用户名是否已存在。
     *
     * @param username 用户名，不能为空
     * @return 如果用户名已存在返回 {@code true}，否则返回 {@code false}
     */
    boolean existsByUsername(String username);

    /**
     * 检查指定邮箱是否已存在。
     *
     * @param email 邮箱地址，不能为空
     * @return 如果邮箱已存在返回 {@code true}，否则返回 {@code false}
     */
    boolean existsByEmail(String email);
}
