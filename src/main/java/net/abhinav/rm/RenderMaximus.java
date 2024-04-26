package net.abhinav.rm;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "rm", name = "Render Maximus", version = "1.0")
public class RenderMaximus {

    private static final int CHUNK_LOAD_RADIUS = 5000;
    private int progress = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Pre-initialization code
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialization code
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (progress > 0) {
                renderProgressBar(progress);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            if (progress < 100) {
                loadChunks();
                simulateRendering();
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiIngameMenu) {
            // Reset progress when returning to the main menu or exiting the game
            progress = 0;
        }
    }

    private void loadChunks() {
        // Simulate chunk loading process
        int chunksLoaded = Math.min(progress / 2, CHUNK_LOAD_RADIUS); // Simulate loading 2 chunks per tick
        progress = Math.min(progress + chunksLoaded, CHUNK_LOAD_RADIUS * 2);
    }

    private void simulateRendering() {
        // Simulate rendering process
        int renderingProgress = Math.min((progress - CHUNK_LOAD_RADIUS) * 2, 100);
        progress = Math.min(progress + renderingProgress, 200);
    }

    private void renderProgressBar(int progress) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        int progressBarWidth = screenWidth / 3;
        int progressBarHeight = 10;
        int progressBarX = (screenWidth - progressBarWidth) / 2;
        int progressBarY = screenHeight / 2 + 30;
        int progressFill = progress * progressBarWidth / 200;

        drawRect(progressBarX, progressBarY, progressBarX + progressFill, progressBarY + progressBarHeight, 0xFF00FF00); // Green
        drawRect(progressBarX + progressFill, progressBarY, progressBarX + progressBarWidth, progressBarY + progressBarHeight, 0xFFFF0000); // Red
    }

    // Utility method to draw a rectangle
    private void drawRect(int left, int top, int right, int bottom, int color) {
        int j1;

        if (left < right) {
            j1 = left;
            left = right;
            right = j1;
        }

        if (top < bottom) {
            j1 = top;
            top = bottom;
            bottom = j1;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.glBegin(7);
        GlStateManager.glVertex2f((float) left, (float) top);
        GlStateManager.glVertex2f((float) left, (float) bottom);
        GlStateManager.glVertex2f((float) right, (float) bottom);
        GlStateManager.glVertex2f((float) right, (float) top);
        GlStateManager.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
