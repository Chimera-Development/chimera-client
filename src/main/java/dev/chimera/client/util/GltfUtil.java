package dev.chimera.client.util;

import de.javagl.jgltf.model.*;
import net.minecraft.client.render.VertexFormat;

public class GltfUtil {
    public static VertexFormat.DrawMode getDrawModeFromInt(int mode){
        VertexFormat.DrawMode drawMode = null;
        switch (mode){
            case 4:
                drawMode = VertexFormat.DrawMode.QUADS;

        }
        return drawMode;
    }
    public static int[] getIndicesAsIntArray(AccessorModel accessorModel)
    {
        AccessorData accessorData = accessorModel.getAccessorData();
        int numElements = accessorData.getNumElements();
        int indices[] = new int[numElements];
        if (accessorData.getComponentType() == int.class)
        {
            AccessorIntData indicesData = (AccessorIntData) accessorData;
            for (int i = 0; i < numElements; i++)
            {
                indices[i] = indicesData.get(i);
            }
            return indices;
        }
        if (accessorData.getComponentType() == short.class)
        {
            AccessorShortData indicesData = (AccessorShortData) accessorData;
            for (int i = 0; i < numElements; i++)
            {
                indices[i] = indicesData.get(i);
            }
            return indices;
        }
        if (accessorData.getComponentType() == byte.class)
        {
            AccessorByteData indicesData = (AccessorByteData) accessorData;
            for (int i = 0; i < numElements; i++)
            {
                indices[i] = indicesData.get(i);
            }
            return indices;
        }
        // Should never happen for valid indices:
        return null;

    }
}
