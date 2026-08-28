package com.ommods.reopenedmodularturrets.compat.jade;

import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.block.TurretHeadBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeCompat.TurretBaseProvider.INSTANCE, TurretBaseBlock.class);
        registration.registerBlockComponent(JadeCompat.TurretHeadProvider.INSTANCE, TurretHeadBlock.class);
    }
}
