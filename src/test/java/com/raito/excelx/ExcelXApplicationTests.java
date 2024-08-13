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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

        Class old = classRepo.findByName("TemplateInfo");

        if (old != null) {
            classRepo.deleteById(old.getId());
            transactionManagement.commit(status);
            def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManagement.getTransaction(def);
        }

        Class clazz = new Class();
        clazz.setName("TemplateInfo");
        List<Field> fields = fieldRepo.findAll();
        fields.sort(Comparator.comparing(Field::getId));
        this.builder(clazz, fields);
        classRepo.save(clazz);
        transactionManagement.commit(status);
    }


    private void builder(Class clazz, List<Field> fields) {

        List<ClassWithField> classWithFields = List.of(
                classWithFieldBuilder(clazz, fields.get(8), "date", "日期"),
                classWithFieldBuilder(clazz, fields.get(3), "id", "主键"),
                classWithFieldBuilder(clazz, fields.get(8), "name", "模板名称"),
                classWithFieldBuilder(clazz, fields.get(8), "type", "模板版式类型"),
                classWithFieldBuilder(clazz, fields.get(8), "enterpriseNo", "企业编码"),
                classWithFieldBuilder(clazz, fields.get(8), "enterpriseName", "企业名称"),
                classWithFieldBuilder(clazz, fields.get(8), "iphoneType", "手机型号"),
                classWithFieldBuilder(clazz, fields.get(8), "analysisType", "识别类型"),
                classWithFieldBuilder(clazz, fields.get(2), "successCount", "模板被解析成功次数"),
                classWithFieldBuilder(clazz, fields.get(2), "messageExposurePV", "消息曝光PV"),
                classWithFieldBuilder(clazz, fields.get(2), "messageExposureUV", "消息曝光UV"),
                classWithFieldBuilder(clazz, fields.get(8), "displayExposureRate", "展示曝光率"),
                classWithFieldBuilder(clazz, fields.get(2), "messageClickPV", "消息点击PV"),
                classWithFieldBuilder(clazz, fields.get(2), "messageClickUV", "消息点击UV"),
                classWithFieldBuilder(clazz, fields.get(8), "displayClickRate", "展示点击率"),
                classWithFieldBuilder(clazz, fields.get(8), "exposureClickRate", "曝光点击率")
        );
        clazz.setClassWithFields(classWithFields);
    }

    private ClassWithField classWithFieldBuilder(Class clazz, Field field, String fieldName, String fieldDescription) {

        return ClassWithField.builder()
                .field(field)
                .fieldName(fieldName)
                .clazz(clazz)
                .fieldDescription(fieldDescription)
                .build();

    }

    @Test
    void testClassLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
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
        String className = ClassUtils.PRE_FIX + "TemplateInfo";
        java.lang.Class<?> clazz = ClassUtils.getClass(className);
        Object object = clazz.getDeclaredConstructor().newInstance();
        System.out.println(object);
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));
        System.out.println(Arrays.toString(clazz.getDeclaredMethods()));
        transactionManagement.commit(status);
    }

}
