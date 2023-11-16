package com.kjmaster.electrostatics.setup;

import com.kjmaster.electrostatics.Electrostatics;
import com.kjmaster.kjlib.setup.DefaultModSetup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);
        ElectrostaticsMessages.registerMessages(Electrostatics.MODID);
    }

    @Override
    protected void setupModCompat() {

    }
}
