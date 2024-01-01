package dev.chimera.client.modules;

import dev.chimera.client.util.TabTree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> moduleList = new ArrayList<>();

    public static void loadModule(Class<? extends Module> moduleClass, Object... params) {
        try {
            Module moduleInstance = moduleClass.getDeclaredConstructor(getConstructorParameterTypes(params)).newInstance(params);
            moduleInstance.onInit();
            moduleList.add(moduleInstance);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
    public static Module getModule(Class<? extends Module> moduleClass){
        Module returnModule = null;
        for (Module module : moduleList) {
            if (module.getClass().equals(moduleClass)){
                returnModule = module;
            }
        }
        return returnModule;
    }
    public static Module[] getModulesByCategory(ModuleCategory category){
        ArrayList<Module> modulesLi = new ArrayList<>();
        for (Module module : moduleList){
            if (category.equals(module.getCategory())){
                modulesLi.add(module);
            }
        }
        if (modulesLi.size() == 0){
            throw new RuntimeException("No Modules of category " + category.title);
        }
        Module[] modules = new Module[modulesLi.size()];
        for (int i = 0; i < modulesLi.size(); i++) {
            modules[i] = modulesLi.get(i);
        }
        return modules;
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