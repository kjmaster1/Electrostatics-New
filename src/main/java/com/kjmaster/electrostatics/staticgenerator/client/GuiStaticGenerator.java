package com.kjmaster.electrostatics.staticgenerator.client;

import com.kjmaster.electrostatics.Electrostatics;
import com.kjmaster.electrostatics.setup.ElectrostaticsMessages;
import com.kjmaster.electrostatics.staticgenerator.StaticGeneratorModule;
import com.kjmaster.electrostatics.staticgenerator.StaticGeneratorTileEntity;
import com.kjmaster.kjlib.container.GenericContainer;
import com.kjmaster.kjlib.gui.GenericGuiContainer;
import com.kjmaster.kjlib.gui.ManualEntry;
import com.kjmaster.kjlib.gui.Window;
import com.kjmaster.kjlib.gui.layout.HorizontalAlignment;
import com.kjmaster.kjlib.gui.layout.VerticalAlignment;
import com.kjmaster.kjlib.gui.widgets.EnergyBar;
import com.kjmaster.kjlib.gui.widgets.Label;
import com.kjmaster.kjlib.gui.widgets.Widgets;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiStaticGenerator extends GenericGuiContainer<StaticGeneratorTileEntity, GenericContainer> {

    private EnergyBar energyBar;

    public GuiStaticGenerator(StaticGeneratorTileEntity tileEntity, GenericContainer container, Inventory inventory) {
        super(tileEntity, container, inventory, ManualEntry.EMPTY);
    }

    public static void register() {
        register(StaticGeneratorModule.CONTAINER_STATICGENERATOR.get(), GuiStaticGenerator::new);
    }

    @Override
    public void init() {

        window = new Window(this, tileEntity, ElectrostaticsMessages.INSTANCE, new ResourceLocation(Electrostatics.MODID, "gui/static_generator.gui"));
        super.init();
        initializeFields();
        getWindowManager().closeWindow(sideWindow.getWindow());
    }

    private void initializeFields() {
        energyBar = window.findChild("energybar");
    }

    private void updateFields() {
        if (window == null) {
            return;
        }
        updateEnergyBar(energyBar);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int x, int y) {
        updateFields();
        drawWindow(graphics);
    }
}
