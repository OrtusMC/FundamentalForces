package com.sammy.fundamental_forces.core.setup.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.sammy.fundamental_forces.core.helper.ColorHelper;
import com.sammy.fundamental_forces.core.helper.DataHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

//https://github.com/Tamaized/Frostfell/blob/1.18/src/main/java/tamaized/frostfell/client/ClientListener.java#L23-L57
public class TextureGrabber {

    private static final ColorLerp GRADIENT = (image, x, y, luminosity) -> ((y % 16) / 16f);
    private static final ColorLerp LUMINOUS_GRADIENT = (image, x, y, luminosity) -> (((y % 16) / 16f) + luminosity/255f)/2f;
    private static final ColorLerp LUMINOUS = (image, x, y, luminosity) -> luminosity/255f;

    public static void setup() {
        registerGrabber("fire_0", "block/fire_0",new ResourceLocation("textures/block/fire_0.png"), (image)->multiColorGradient(image, LUMINOUS, new Color(255,255,255), new Color(255,0,255), new Color(100, 25, 200)));
        registerGrabber("fire_1", "block/fire_1",new ResourceLocation("textures/block/fire_1.png"), (image)->multiColorGradient(image, LUMINOUS, new Color(255,255,255), new Color(255,0,255), new Color(100, 25, 200)));
    }
    public static void registerGrabber(String loaderName, String targetPath, ResourceLocation sourcePath, TextureModifier modifier) {

        IEventBus busMod = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForgeClient.registerTextureAtlasSpriteLoader(DataHelper.prefix(loaderName), (atlas, resourceManager, textureInfo, resource, atlasWidth, atlasHeight, spriteX, spriteY, mipmapLevel, image) -> {
            Resource r = null;
            try {
                r = resourceManager.getResource(sourcePath);
                image = modifier.modifyTexture(NativeImage.read(r.getInputStream()));
            } catch (Throwable throwable1) {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                }

                throwable1.printStackTrace();
            }
            TextureAtlasSprite.Info info = null;
            if (r != null) {
                try {
                    AnimationMetadataSection section = r.getMetadata(AnimationMetadataSection.SERIALIZER);
                    if (section == null) {
                        section = AnimationMetadataSection.EMPTY;
                    }

                    Pair<Integer, Integer> pair = section.getFrameSize(image.getWidth(), image.getHeight());
                    info = new TextureAtlasSprite.Info(textureInfo.name(), pair.getFirst(), pair.getSecond(), section);
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new TextureAtlasSprite(atlas, info == null ? textureInfo : info, mipmapLevel, atlasWidth, atlasHeight, spriteX, spriteY, image) {
            };
        });
        busMod.addListener((Consumer<TextureStitchEvent.Pre>) event -> event.addSprite(DataHelper.prefix(targetPath)));
    }
    public static NativeImage grayscale(NativeImage nativeimage)
    {
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int L = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                nativeimage.setPixelRGBA(x, y, NativeImage.combine((pixel >> 24) & 0xFF, L, L, L));
            }
        }
        return nativeimage;
    }
    public static NativeImage multiColorGradient(NativeImage nativeimage, ColorLerp colorLerp, Color... colors)
    {
        int colorCount = colors.length-1;
        for (int x = 0; x < nativeimage.getWidth(); x++) {
            for (int y = 0; y < nativeimage.getHeight(); y++) {
                int pixel = nativeimage.getPixelRGBA(x, y);
                int luminosity = (int) (0.299D * ((pixel) & 0xFF) + 0.587D * ((pixel >> 8) & 0xFF) + 0.114D * ((pixel >> 16) & 0xFF));
                float lerp = 1-colorLerp.lerp(pixel, x, y, luminosity);
                float colorIndex = colorCount*lerp;
                int index = (int) Mth.clamp(colorIndex, 0, colorCount);
                Color color = colors[index];
                Color nextColor = index == colorCount ? color : colors[index+1];
                Color transition = ColorHelper.colorLerp(colorIndex-(int)(colorIndex), nextColor, color);
                nativeimage.setPixelRGBA(x, y, NativeImage.combine((pixel >> 24) & 0xFF, transition.getBlue(), transition.getGreen(), transition.getRed()));
            }
        }
        return nativeimage;
    }
    public interface ColorLerp
    {
        float lerp(int pixel, int x, int y, int luminosity);
    }
    public interface TextureModifier
    {
        NativeImage modifyTexture(NativeImage nativeImage);
    }
}