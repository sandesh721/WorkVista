package com.task.WorkVista.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Component
public class EntityReferenceResolver {

    private final EntityManager entityManager;

    public EntityReferenceResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Automatically replaces transient entity fields (with only id set)
     * by managed JPA references.
     */
    public void resolve(Object obj) {
        resolve(obj, new HashSet<>());
    }

    private void resolve(Object obj, Set<Object> visited) {
        if (obj == null) return;

        // Prevent infinite recursion
        if (visited.contains(obj)) return;
        visited.add(obj);

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value == null) continue;

                // If field type looks like an entity (has @Id)
                if (isEntity(value.getClass())) {
                    Long id = extractId(value);
                    if (id != null) {
                        Object ref = entityManager.getReference(value.getClass(), id);
                        field.set(obj, ref);
                    }
                }
                // Recursively resolve nested objects (like DTOs inside DTOs)
                // But skip collections to avoid performance issues
                else if (!isPrimitiveOrWrapper(field.getType())
                        && !(value instanceof String)
                        && !(value instanceof java.util.Collection)
                        && !(value instanceof java.util.Map)) {
                    resolve(value, visited);
                }

            } catch (Exception e) {
                // Just skip problematic fields safely
            }
        }
    }

    private boolean isEntity(Class<?> clazz) {
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private Long extractId(Object entity) {
        try {
            for (Field f : entity.getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(Id.class)) {
                    f.setAccessible(true);
                    Object idVal = f.get(entity);
                    if (idVal instanceof Number num) return num.longValue();
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type.equals(Boolean.class) || type.equals(Byte.class) ||
                type.equals(Character.class) || type.equals(Double.class) ||
                type.equals(Float.class) || type.equals(Integer.class) ||
                type.equals(Long.class) || type.equals(Short.class);
    }
}