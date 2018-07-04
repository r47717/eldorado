package ru.r47717.eldorado.core.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class Container {

    private static Map<Class, Object> injectedObjects = new HashMap<>();

    public static boolean inject(Object controllerInstance, Field field)
            throws IllegalAccessException, InvocationTargetException, InstantiationException
    {
        Class injectedClass = field.getType();
        Object instance = null;

        if (!injectedObjects.containsKey(injectedClass)) {
            Constructor[] constructors = injectedClass.getConstructors();
            for (Constructor constructor: constructors) {
                if (constructor.getParameterCount() == 0) {
                    // found default constructor
                    instance = constructor.newInstance();
                    injectedObjects.put(injectedClass, instance);
                    break;
                }
            }

        } else {
            instance = injectedObjects.get(injectedClass);
        }

        if (instance != null) {
            field.setAccessible(true);
            field.set(controllerInstance, instance);
            return true;
        }

        return false;
    }

}
