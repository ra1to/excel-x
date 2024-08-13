package com.raito.excelx.domain.classloader.init;

import com.raito.excelx.domain.classloader.entity.Field;
import com.raito.excelx.domain.classloader.enums.FieldType;
import com.raito.excelx.domain.classloader.repository.FieldRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author raito
 * @since 2024/8/12
 */
@Component
@EnableAsync
@RequiredArgsConstructor
public class FieldTypeInit implements CommandLineRunner {

    private final FieldRepo fieldRepo;
    @Override
    @Async
    public void run(String... args) {

        List<FieldType> list = List.of(FieldType.values());
        Set<FieldType> exists = fieldRepo.findByTypeIn(list)
                .stream().map(Field::getType).collect(Collectors.toSet());

        List<Field> add = list.stream().filter(item -> !exists.contains(item)).map(item -> {
            Field field = new Field();
            field.setType(item);
            return field;
        }).collect(Collectors.toList());

        fieldRepo.saveAll(add);

    }
}
