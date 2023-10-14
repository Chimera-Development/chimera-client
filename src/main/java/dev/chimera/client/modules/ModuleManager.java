package dev.chimera.client.modules;

import dev.chimera.client.util.TabTree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<AbstractModule> moduleList = new ArrayList<>();

    public static void loadModule(TabTree tree, Class<? extends AbstractModule> moduleClass, Object... params) {
        try {
            AbstractModule moduleInstance = moduleClass.getDeclaredConstructor(getConstructorParameterTypes(params)).newInstance(params);
            moduleInstance.onInit();
            moduleList.add(moduleInstance);
            tree.put(moduleInstance.getName(), moduleInstance);
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