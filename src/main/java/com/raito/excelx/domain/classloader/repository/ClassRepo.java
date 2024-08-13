package com.raito.excelx.domain.classloader.repository;

import com.raito.excelx.domain.classloader.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author raito
 * @since 2024/8/12
 */
@Repository
public interface ClassRepo extends JpaRepository<Class, Long> {
    Class findByName(String name);
}
