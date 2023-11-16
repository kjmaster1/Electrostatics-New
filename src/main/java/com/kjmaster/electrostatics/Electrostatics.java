package com.kjmaster.electrostatics;



import com.kjmaster.electrostatics.setup.ClientSetup;
import com.kjmaster.electrostatics.setup.ModSetup;
import com.kjmaster.electrostatics.setup.Registration;
import com.kjmaster.electrostatics.staticgenerator.StaticGeneratorModule;
import com.kjmaster.electrostatics.treetap.TreeTapModule;
import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.modules.Modules;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Electrostatics.MODID)
public class Electrostatics
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "electrostatics";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("PublicField")
    public static final ModSetup setup = new ModSetup();

    @SuppressWarnings("PublicField")
    public static Electrostatics instance;

    private final Modules modules = new Modules();

    public Electrostatics()
    {
        instance = this;
        setupModules();

        Registration.register();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);
        bus.addListener(modules::init);
        bus.addListener(this::onDataGen);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientSetup::init);
            bus.addListener(modules::initClient);
        });

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void onDataGen(GatherDataEvent event) {
        DataGen datagen = new DataGen(MODID, event);
        modules.datagen(datagen);
        datagen.generate();
    }

    public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
        return setup.tab(supplier);
    }

    private void setupModules() {
        modules.register(new TreeTapModule());
        modules.register(new StaticGeneratorModule());
    }
}