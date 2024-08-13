package com.raito.excelx.domain.classloader.repository;

import com.raito.excelx.domain.classloader.entity.Field;
import com.raito.excelx.domain.classloader.enums.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author raito
 * @since 2024/8/12
 */
@Repository
public interface FieldRepo extends JpaRepository<Field, Long> {
    List<Field> findByTypeIn(List<FieldType> type);
}
