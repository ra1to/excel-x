package com.raito.excelx.domain.classloader.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(nullable = false)
    private String fieldName;

    @Comment("字段描述")
    @Lob
    private String fieldDescription;
}
