package com.company.springbootstarter.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue // 告诉 JPA 自动生成主键
    @UuidGenerator // 默认生成时间有序的 UUID (v7)，对索引友好
    @JdbcTypeCode(SqlTypes.CHAR) // 强制 Hibernate 在 JDBC 层面使用 CHAR 类型传递参数
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
