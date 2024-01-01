package dev.chimera.client.modules;

public enum ModuleCategory{
    COMBAT("Combat"),
    UTILITY("Utility"),
    GUI("Gui");


    public String title;
    ModuleCategory(String title){
        this.title = title;
    }
}
