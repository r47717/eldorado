package ru.r47717.eldorado.core.di;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class Container {

    private static Map<Class, Object> injectedObjects = new HashMap<>();

    public static boolean inject(Class controller, Field field) {

        if (!injectedObjects.containsKey(controller)) {
            System.out.println("Field " + field.getName() + "(" + field.getType() + ")" + " will be injected");
            injectedObjects.put(controller, new Object());
        }

        return true;
    }

}
