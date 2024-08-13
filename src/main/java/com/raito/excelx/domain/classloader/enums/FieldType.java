package com.raito.excelx.domain.classloader.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author raito
 * @since 2024/8/12
 */
@RequiredArgsConstructor
@Getter
public enum FieldType {

    BYTE(0, "java.lang.Byte"),
    SHORT(1, "java.lang.Short"),
    INT(2, "java.lang.Integer"),
    LONG(3, "java.lang.Long"),
    FLOAT(4, "java.lang.Float"),
    DOUBLE(5, "java.lang.Double"),
    BOOLEAN(6, "java.lang.Boolean"),
    CHAR(7, "java.lang.Character"),
    STRING(8, "java.lang.String"),
    DATE(9, "java.time.LocalDate");

    private final Integer code;
    private final String packageName;


}
