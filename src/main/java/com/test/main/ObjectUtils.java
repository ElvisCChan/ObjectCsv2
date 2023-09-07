package com.test.main;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ObjectUtils {

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                // field.setAccessible(true);
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    public static List<String> getObjectFieldNames(Object object) {
        List<String> fieldNames = new ArrayList<>();

        Class<?> clazz = object.getClass();
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                    Class<?> listType = (Class<?>) typeArguments[0];
                    fieldNames.addAll(getAllFields(listType).stream()
                            .map(Field::getName)
                            .map(fieldName -> field.getName() + "." + fieldName)
                            .collect(Collectors.toList()));
                }
            } else {
                fieldNames.add(field.getName());
            }
        }

        return fieldNames;
    }

    public static CellProcessor[] getProcessors(List<String> fieldNames) {
        List<CellProcessor> processors = new ArrayList<>();

        for (String fieldName : fieldNames) {
            processors.add(new Optional(new MapToCellProcessor(fieldName)));
        }

        return processors.toArray(new CellProcessor[0]);
    }

    private static class MapToCellProcessor extends CellProcessorAdaptor {
        private final String fieldName;

        public MapToCellProcessor(String fieldName) {
            super();
            this.fieldName = fieldName;
        }

        @Override
        public <T> T execute(Object value, CsvContext context) {
            @SuppressWarnings("unchecked")
            Map<String, Object> rowData = (Map<String, Object>) value;
            return next.execute(rowData.get(fieldName), context);
        }
    }
}
