package dev.chimera.client.util;

import dev.chimera.client.ChimeraClient;
import dev.chimera.client.gui.widgets.TabNodeWidget;
import dev.chimera.client.modules.AbstractModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabTree implements Drawable {
    private final Text title;
    private final Map<String, Object> tree;
    private final List<String> path;
    private int layer;
    private TabNodeWidget renderNode;

    public TabTree(Text title) {
        this(title, 0);
    }

    public TabTree(Text title, int layer) {
        this.title = title;
        this.tree = new HashMap<>();
        this.path = new ArrayList<>();
        this.layer = layer;
        this.renderNode = new TabNodeWidget(this);
    }

    public Text getTitle() {
        return title;
    }

    public int getLayer() {
        return layer;
    }

    public List<String> getPath() {
        return path;
    }

    public List<Object> getPathObjects() {
        List<Object> objectPath = new ArrayList<>();
        Object current = this;
        objectPath.add(current);
        for (String key : path) {
            current = ((TabTree)current).get(key);
            objectPath.add(current);
        }
        return objectPath;
    }

    public void pathBack() {
        path.remove(path.size() - 1);
    }

    public void pathForwards(String key) {
        path.add(key);
    }

    public void pathUp() {
        List<String> currentNodeKeys = this.currentNodeKeys();
        int index = currentNodeKeys.indexOf(path.get(path.size() - 1));
        assert index != -1;
        index = (index - 1 + path.size()) % path.size();
        path.set(path.size() - 1, currentNodeKeys.get(index));
    }

    public void pathDown() {
        List<String> currentNodeKeys = this.currentNodeKeys();
        int index = currentNodeKeys.indexOf(path.get(path.size() - 1));
        assert index != -1;
        index = (index + 1) % path.size();
        path.set(path.size() - 1, currentNodeKeys.get(index));
    }

    private List<String> currentNodeKeys() {
        TabTree current = this;
        int i = 0;
        for (String key : path) {
            if (current.get(key) instanceof TabTree t) {
                current = t;
            } else {
                break;
            }
        }
        return current.getKeys();
    }

    public List<String> getKeys() {
        return tree.keySet().stream().toList();
    }

    public Object get(String key) {
        return tree.get(key);
    }

    public void put(String key, Object treeNode) {
        if (!(treeNode instanceof TabTree) && !(treeNode instanceof AbstractModule)) {
            ChimeraClient.LOGGER.warn("treeNode is of type '{}'", treeNode.getClass().toString());
            return;
        }
        if (treeNode instanceof TabTree t) {
            t.layer = this.layer + 1;
        }
        tree.put(key, treeNode);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderNode.render(context, mouseX, mouseY, delta);
    }
}