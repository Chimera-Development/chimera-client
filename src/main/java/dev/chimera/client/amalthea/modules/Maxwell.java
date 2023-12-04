package dev.chimera.client.amalthea.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import de.javagl.jgltf.model.*;

import de.javagl.jgltf.model.v2.MaterialModelV2;
import dev.chimera.client.ChimeraClient;
import dev.chimera.client.events.RenderEvent;
import dev.chimera.client.modules.Module;
import dev.chimera.client.modules.ModuleCategory;
import dev.chimera.client.util.ColorHolder;
import dev.chimera.client.util.GltfUtil;
import net.engio.mbassy.listener.Handler;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import de.javagl.jgltf.model.io.GltfModelReader;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import static dev.chimera.client.ChimeraClient.*;

public class Maxwell extends Module {
    private boolean debug = false;
    private int rotationCounter = 0;


    public Maxwell() {
        super("Maxwell", "Spinning Cat", ModuleCategory.GUI,GLFW.GLFW_KEY_Z);

        //this.onEnable();
    }
    @Override
    public void onEnable() {
        ChimeraClient.LOGGER.info("enabled the cat");
        super.onEnable();
        EVENT_MANAGER.subscribe(this);
        MinecraftClient.getInstance().getMusicTracker().stop();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        EVENT_MANAGER.unsubscribe(this);
        MinecraftClient.getInstance().getMusicTracker().stop(maxwellSound);
        MinecraftClient.getInstance().getMusicTracker().play(MusicType.MENU);
    }
    @Handler
    public void render(RenderEvent event) {
        if (!MinecraftClient.getInstance().getMusicTracker().isPlayingType(maxwellSound)){
            MinecraftClient.getInstance().getMusicTracker().play(maxwellSound);
        }
        DrawContext drawContext = event.drawContext;
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        MatrixStack matrixStack = drawContext.getMatrices();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        File modDirectory = FabricLoader.getInstance().getModContainer(MOD_ID).get().getRootPath().toFile();
        File file = new File(modDirectory, "assets/chimera-client/maxwell.gltf");

        try {
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            GltfModelReader reader = new GltfModelReader();
            GltfModel gltfModel = reader.read(file.toPath());
            List<MeshModel> meshModels = gltfModel.getMeshModels();
            List<SceneModel> sceneModels = gltfModel.getSceneModels();
            for (SceneModel sceneModel : sceneModels) {
                List<NodeModel> nodeModels = sceneModel.getNodeModels();
                for (NodeModel nodeModel : nodeModels) {
                    List<NodeModel> children = nodeModel.getChildren();
                    for (NodeModel child : children) {
                        if (debug) {
                            System.out.println(String.format("%s:",child.getName()));
                            System.out.println(String.format("Rotation %s",Arrays.toString(child.getRotation())));
                            System.out.println(String.format("Scale %s",Arrays.toString(child.getScale())));
                            System.out.println(String.format("Translation %s",Arrays.toString(child.getTranslation())));
                            System.out.println(String.format("Length meshModels: %d",child.getMeshModels().size()));
                        }
                        Matrix4f scale = new Matrix4f().scale(child.getScale()[0]*50,child.getScale()[1]*50,child.getScale()[2]*50);
                        Quaternionf rotation = new Quaternionf().set(child.getRotation()[0],child.getRotation()[1],child.getRotation()[2],-child.getRotation()[3]);
                        rotation.rotateZ((float) Math.toRadians(rotationCounter%360));
                        Matrix4f translation = new Matrix4f().translate(600,600,300);
                        for (MeshModel meshModel : meshModels)
                        {
                            List<MeshPrimitiveModel> meshPrimitiveModels = meshModel.getMeshPrimitiveModels();
                            for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
                            {
                                Identifier texture = null;
                                if (meshPrimitiveModel.getMaterialModel() instanceof MaterialModelV2 materialModel) {
                                    ByteBuffer imageData = materialModel.getBaseColorTexture().getImageModel().getImageData();
                                    NativeImage image = NativeImage.read(imageData);
                                    NativeImageBackedTexture backedTexture = new NativeImageBackedTexture(image);
                                    texture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID,backedTexture);
                                }
                                if (texture == null){
                                    System.out.println("Huh texture == null");
                                    return;
                                }
                                RenderSystem.setShaderTexture(0, texture);
                                RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
                                RenderSystem.disableCull();
                                RenderSystem.setShaderColor(1f,1f,1f,1f);
                                matrixStack.push();
                                Matrix4f matrix = matrixStack.peek().getPositionMatrix();
                                bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES,VertexFormats.POSITION_TEXTURE_COLOR);
                                //ChimeraClient.LOGGER.info(String.valueOf(meshPrimitiveModel.getMode()));
                                //bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
                                int[] indices;
                                List<Vector4f> vertices = new ArrayList<>();
                                List<Vector2f> uvs = new ArrayList<>();
                                Map<String, AccessorModel> attributes = meshPrimitiveModel.getAttributes();

                                AccessorModel positionsAccessorModel = attributes.get("POSITION");
                                AccessorModel texture0AccessorModel = attributes.get("TEXCOORD_0");
                                AccessorData texture0AccessorData = texture0AccessorModel.getAccessorData();
                                AccessorData positionsAccessorData = positionsAccessorModel.getAccessorData();

                                // The POSITIONS are assumed to be "VEC3 / float" data here:
                                AccessorFloatData positionsData = (AccessorFloatData) positionsAccessorData;
                                AccessorFloatData texture0Data = (AccessorFloatData) texture0AccessorData;
                                //System.out.println(texture0Data.getNumElements()==positionsData.getNumElements());
                                int numPositions = positionsData.getNumElements();
                                for (int p = 0; p < numPositions; p++)
                                {
                                    float x = positionsData.get(p, 0);
                                    float y = positionsData.get(p, 1);
                                    float z = positionsData.get(p, 2);
                                    float u = texture0Data.get(p,0);
                                    float v = texture0Data.get(p,1);
                                    Vector4f vect = new Vector4f(x,y,z,0);
                                    vect = rotation.transform(vect);
                                    vect = scale.transform(vect);
                                    //vect = translation.transform(vect);
                                    vect.set(vect.x+event.mouseX,vect.y+event.mouseY,vect.z+300);
                                    vertices.add(vect);
                                    uvs.add(new Vector2f(u,v));
                                    //System.out.printf("X: %f Y: %f Z: %f u: %f v: %f",x,y,z,u,v);
                                }
                                AccessorModel indicesAccessorModel = meshPrimitiveModel.getIndices();
                                if (indicesAccessorModel != null)
                                {
                                    indices = GltfUtil.getIndicesAsIntArray(indicesAccessorModel);
                                    int numTriangles = indices.length / 3;
                                    for (int t = 0; t < numTriangles; t++)
                                    {
                                        int v0 = indices[t * 3];
                                        int v1 = indices[t * 3 + 1];
                                        int v2 = indices[t * 3 + 2];
                                        Vector4f ve1 = vertices.get(v0);
                                        Vector4f ve2 = vertices.get(v1);
                                        Vector4f ve3 = vertices.get(v2);
                                        Vector2f uv1 = uvs.get(v0);
                                        Vector2f uv2 = uvs.get(v1);
                                        Vector2f uv3 = uvs.get(v2);
                                        bufferBuilder.vertex(matrix,ve1.x,ve1.y,ve1.z).texture(uv1.x,uv1.y).color(255,255,255,255).next();
                                        bufferBuilder.vertex(matrix,ve2.x,ve2.y,ve2.z).texture(uv2.x,uv2.y).color(255,255,255,255).next();
                                        bufferBuilder.vertex(matrix,ve3.x,ve3.y,ve3.z).texture(uv3.x,uv3.y).color(255,255,255,255).next();
                                        //System.out.println("Triangle " + t + " has vertex indices " + v0 + " " + v1 + " " + v2);
                                    }
                                }
                                tessellator.draw();
                                matrixStack.pop();
                            }
                        }
                    }

                }
            }
            rotationCounter++;
//            for (MeshModel meshModel : meshModels)
//            {
//                List<MeshPrimitiveModel> meshPrimitiveModels = meshModel.getMeshPrimitiveModels();
//                for (MeshPrimitiveModel meshPrimitiveModel : meshPrimitiveModels)
//                {
//                    RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//                    matrixStack.push();
//                    Matrix4f matrix = matrixStack.peek().getPositionMatrix();
//                    bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES,VertexFormats.POSITION_COLOR);
//                    //ChimeraClient.LOGGER.info(String.valueOf(meshPrimitiveModel.getMode()));
//                    //bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
//                    int[] indices = new int[]{};
//                    List<Vector4f> vertices = new ArrayList<>();
//                    Map<String, AccessorModel> attributes = meshPrimitiveModel.getAttributes();
//
//                    AccessorModel positionsAccessorModel = attributes.get("POSITION");
//                    AccessorData positionsAccessorData = positionsAccessorModel.getAccessorData();
//
//                    // The POSITIONS are assumed to be "VEC3 / float" data here:
//                    AccessorFloatData positionsData = (AccessorFloatData) positionsAccessorData;
//
//                    int numPositions = positionsData.getNumElements();
//                    for (int p = 0; p < numPositions; p++)
//                    {
//                        float x = positionsData.get(p, 0);
//                        float y = positionsData.get(p, 1);
//                        float z = positionsData.get(p, 2);
//                        Vector4f vect = new Vector4f(x,y,z,0);
//                        vect = quaternionf.transform(vect);
//                        vect.set(vect.x*10000+300,vect.y*10000+300,vect.z*10000+300);
//                        //vect.mul(move);
//                        vertices.add(vect);
//                        //System.out.println("Vertex " + p + " is at " + x + " " + y + " " + z);
//                    }
//                    AccessorModel indicesAccessorModel = meshPrimitiveModel.getIndices();
//                    if (indicesAccessorModel != null)
//                    {
//                        indices = GltfUtil.getIndicesAsIntArray(indicesAccessorModel);
//                        int numTriangles = indices.length / 3;
//                        for (int t = 0; t < numTriangles; t++)
//                        {
//                            int v0 = indices[t * 3 + 0];
//                            int v1 = indices[t * 3 + 1];
//                            int v2 = indices[t * 3 + 2];
//                            Vector4f ve1 = vertices.get(v0);
//                            Vector4f ve2 = vertices.get(v1);
//                            Vector4f ve3 = vertices.get(v2);
//                            bufferBuilder.vertex(matrix,ve1.x,ve1.y,ve1.z).color(-1).next();
//                            bufferBuilder.vertex(matrix,ve2.x,ve2.y,ve2.z).color(-1).next();
//                            bufferBuilder.vertex(matrix,ve3.x,ve3.y,ve3.z).color(-1).next();
//                            //System.out.println("Triangle " + t + " has vertex indices " + v0 + " " + v1 + " " + v2);
//                        }
//                    }
//                    tessellator.draw();
//                    matrixStack.pop();
//                }
//            }



//            for (Vector3i vector : meshs.get("whiskers")) {
//                bufferBuilder.vertex(matrix,vector.x,vector.y,vector.z).color(ColorHolder.random().getARGB()).next();
//            }
//            tessellator.draw();
//            matrixStack.pop();
//            for (int i = 0;i<obj.getNumFaces();i++){
//                ObjFace face = obj.getFace(i);
//                //ChimeraClient.LOGGER.info(String.valueOf(face.getNumVertices()));
//                VertexConsumer c = bufferBuilder.vertex(matrix,face.getVertexIndex(0),face.getVertexIndex(1),face.getVertexIndex(2));
//                if (face.containsTexCoordIndices()){
//                    c.texture(face.getTexCoordIndex(0),face.getTexCoordIndex(1));
//                }
//                c.color(new ColorHolder(255,0,0,255).getARGB());
//                if (face.containsNormalIndices()){
//                    c.normal(face.getNormalIndex(0),face.getNormalIndex(1),face.getNormalIndex(2));
//                }
//                c.next();
//                //FloatTuple vertex = obj.getVertex(i);
//                //bufferBuilder.vertex(matrix,vertex.getX()*10+100,vertex.getY()*10+100,vertex.getZ()*10).color(new ColorHolder(255,0,0,255).getARGB()).next();
//                    //ChimeraClient.LOGGER.info(vertex.toString());
//                //
//            }

//            for (int i = 0; i < obj.getNumFaces(); i++) {
//                ObjFace face = obj.getFace(i);
//                for (int j = 0; j < face.getNumVertices(); j++) {
//                    int vertexIndex = face.getVertexIndex(j);
//                    //int normalIndex = face.getNormalIndex(j);
//                    //int texCoordIndex = face.getTexCoordIndex(j);
//
//                    FloatTuple vertex = obj.getVertex(vertexIndex - 1);
//                    //FloatTuple normal = obj.getNormal(normalIndex - 1);
//                    //FloatTuple texCoord = obj.getTexCoord(texCoordIndex - 1);
//
//                    // Adjust scaling and translation as needed
//                    float scaleFactor = 10.0f;
//                    float xOffset = 100.0f;
//                    float yOffset = 100.0f;
//                    bufferBuilder.vertex(matrix, vertex.getX() * scaleFactor + xOffset, vertex.getY() * scaleFactor + yOffset, vertex.getZ() * scaleFactor)
//                            .color(255, 0, 0, 255)
//                            .next();
////                    bufferBuilder.vertex(matrix, vertex.getX() * scaleFactor + xOffset, vertex.getY() * scaleFactor + yOffset, vertex.getZ() * scaleFactor)
////                            .texture(texCoord.getX(), texCoord.getY())// Set UV coordinates
////                            .color(255, 0, 0, 255)
////                            .normal(normal.getX(),normal.getY(),normal.getZ())
////                            .next();
//                }
//            }

            //tessellator.draw();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
