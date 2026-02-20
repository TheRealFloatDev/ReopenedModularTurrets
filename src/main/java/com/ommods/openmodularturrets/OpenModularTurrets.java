package com.ommods.openmodularturrets;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ommods.openmodularturrets.reference.Reference.MOD_ID;

@Mod(MOD_ID)
public class OpenModularTurrets {
    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MOD_ID));
    private static final Logger LOGGER = LogManager.getLogger();
    public static NonNullSupplier<CreativeModeTab> creativeModeTabNonNullSupplier;
    public static CreativeModeTab creativeModeTab;

    public OpenModularTurrets() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Hello from omt's preinit");
        creativeModeTab = new OMTCreativeModeTab();
        creativeModeTabNonNullSupplier = () -> creativeModeTab;

        OMTItems.register(REGISTRATE.get());
    }

}
