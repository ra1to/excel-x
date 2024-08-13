package com.raito.excelx;

import com.raito.excelx.domain.classloader.entity.Class;
import com.raito.excelx.domain.classloader.entity.ClassWithField;
import com.raito.excelx.domain.classloader.entity.Field;
import com.raito.excelx.domain.classloader.repository.ClassRepo;
import com.raito.excelx.domain.classloader.repository.FieldRepo;
import com.raito.excelx.domain.classloader.util.ClassUtils;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExcelXApplicationTests {

    @Autowired
    private ClassRepo classRepo;

    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void contextLoads() {
        JpaTransactionManager transactionManagement = new JpaTransactionManager(entityManagerFactory);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManagement.getTransaction(def);

        Class clazz = new Class();
        clazz.setName("test");
        List<Field> fields = fieldRepo.findAll();
        ClassWithField classWithField = new ClassWithField();
//        classWithField.setClazz(clazz);
        classWithField.setField(fields.get(3));
        classWithField.setFieldName("id");
        classWithField.setClazz(clazz);
        clazz.setClassWithFields(List.of(classWithField));
        classRepo.save(clazz);
        transactionManagement.commit(status);
    }

    @Test
    void testClassLoader() throws ClassNotFoundException {
        JpaTransactionManager transactionManagement = new JpaTransactionManager(entityManagerFactory);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManagement.getTransaction(def);

        List<Class> classList = classRepo.findAll();
        List<java.lang.Class<?>> list = new ArrayList<>();
        classList.forEach(clazz -> {
            try {
                java.lang.Class<?> loadClass = ClassUtils.createAndLoadClass(clazz);
                list.add(loadClass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        String className = ClassUtils.PRE_FIX + "test";
        java.lang.Class<?> clazz = ClassUtils.getClass(className);
        transactionManagement.commit(status);
    }

}
