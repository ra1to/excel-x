package com.raito.excelx.domain.classloader.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Comment;

/**
 * @author raito
 * @since 2024/8/12
 */
@Table(indexes = {
        @Index(name = "idx_class_with_field", columnList = "clazz_id,field_id,field_name", unique = true)
})
@Data
@Entity(name = "class_with_field")
@Comment("类和字段关联表")
public class ClassWithField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @Comment("类id")
    @ToString.Exclude
    private Class clazz;

    @ManyToOne
    @JoinColumn(name = "field_id")
    @Comment("字段id")
    private Field field;

    @Comment("字段名")
    private String fieldName;
}
