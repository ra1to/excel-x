package com.raito.excelx.domain.classloader.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * @author raito
 * @since 2024/8/12
 */
@Entity(name = "class_entity")
@Comment("类表")
@Data
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键")
    private Long id;

    @Comment("类名")
    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "clazz")
    private List<ClassWithField> classWithFields;

}
