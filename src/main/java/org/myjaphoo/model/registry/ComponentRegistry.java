package org.myjaphoo.model.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Registry to register additional components via scripting or customization.
 * Component instances could be registered here and then used by the base application logic.
 * @author mla
 * @version $Id$
 */
public class ComponentRegistry {
    private final static Logger logger = LoggerFactory.getLogger(ComponentRegistry.class.getName());

    /** the singleton component registry. */
    public static final ComponentRegistry registry = new ComponentRegistry();

    private static final String DEFAULT = "DEFAULT";

    /**
     * Assigns a class name to a map of components by component name.
     */
    private Map<String, Map> compRegistry = new HashMap<>();

    private ComponentRegistry() {
    }

    public void register(Class clazz, Object instance) {
        register(DEFAULT, clazz, instance);
    }

    public void register(String name, Class clazz, Object instance) {
        if (!clazz.isInstance(instance)) {
            throw new IllegalArgumentException(instance + " is no instance of " + clazz.getName() + "!");
        }

        Map<String, Object> classRegistry = getClassRegistryMap(clazz);
        classRegistry.put(name, instance);
        logger.info("register component " + clazz.getName() + " instance " + instance + " with name " + name);
    }

    public void register(String name, Class clazz, Class implementation) {
        if (!implementation.isAssignableFrom(implementation)) {
            throw new IllegalArgumentException(implementation.getName() + " is no implementation or super class of " + clazz.getName() + "!");
        }
        Map<String, Object> classRegistry = getClassRegistryMap(clazz);
        classRegistry.put(name, implementation);
        logger.info("register component " + clazz.getName() + " with class " + implementation + " with name " + name);
    }

    public void register(Class clazz, Class implementation) {
        register(DEFAULT, clazz, implementation);
    }


    public <T> Set<Map.Entry<String, T>> getEntries(Class<T> clazz) {
        return getClassRegistryMap(clazz).entrySet();
    }

    public <T> Collection<T> getEntryCollection(Class<T> clazz) {
        return getClassRegistryMap(clazz).values();
    }

    public <T> T newInstance(Class<T> clazz) {
        Class implClazz = (Class) getSingleEntryIntern(clazz);
        try {
            return (T) implClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("error creating new instance!", e);
        }
    }

    private Object getSingleEntryIntern(Class clazz) {
        Set<Map.Entry> set = getClassRegistryMap(clazz).entrySet();
        if (set.size() != 1) {
            throw new InternalError("class " + clazz.getName() + " has registered " + set.size() +
                    " instances, but is used as singleton!");
        }
        return set.iterator().next().getValue();
    }

    public <T> T getSingleEntry(Class<T> clazz) {
        return (T) getSingleEntryIntern(clazz);
    }

    private <T> Map<String, T> getClassRegistryMap(Class<T> clazz) {
        String clazzName = clazz.getName();
        Map<String, T> classRegistry = (Map<String, T>) compRegistry.get(clazzName);
        if (classRegistry == null) {
            classRegistry = new LinkedHashMap<>();
            compRegistry.put(clazzName, classRegistry);
        }
        return classRegistry;
    }

    public void clear() {
        compRegistry.entrySet().clear();
    }
}
