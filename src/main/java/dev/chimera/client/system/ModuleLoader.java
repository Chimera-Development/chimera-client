package dev.chimera.client.system;

import dev.chimera.client.modules.AbstractModule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModuleLoader {
    public static List<AbstractModule> moduleList = new ArrayList<>();

    //This method only accepts non-primitive types.
    public static void loadModule(Class<? extends AbstractModule> moduleClass, Object... params) {
        try {
            AbstractModule moduleInstance = moduleClass.getDeclaredConstructor(getConstructorParameterTypes(params)).newInstance(params);
            moduleInstance.onInit();
            moduleList.add(moduleInstance);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private static Class<?>[] getConstructorParameterTypes(Object... args) {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return parameterTypes;
    }

    public void loadClasspath(String path) {

    }

}