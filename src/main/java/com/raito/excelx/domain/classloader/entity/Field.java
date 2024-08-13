package com.raito.excelx.domain.classloader.entity;

import com.raito.excelx.domain.classloader.enums.FieldType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * @author raito
 * @since 2024/8/12
 */
@Entity(name = "field_entity")
@Comment("字段表")
@Data
@Table(indexes = {
        @Index(name = "idx_field_name", columnList = "type", unique = true)
})
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Comment("字段类型")
    private FieldType type;

    @OneToMany(mappedBy = "field")
    @ToString.Exclude
    private List<ClassWithField> classWithFields;

}
