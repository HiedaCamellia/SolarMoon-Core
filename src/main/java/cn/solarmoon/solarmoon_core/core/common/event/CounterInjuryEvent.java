package cn.solarmoon.solarmoon_core.core.common.event;

import cn.solarmoon.solarmoon_core.core.common.registry.SolarAttributes;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CounterInjuryEvent {

    @SubscribeEvent
    public void counter(LivingDamageEvent event) {
        LivingEntity livingBeAttacked = event.getEntity();
        AttributeInstance attribute = livingBeAttacked.getAttribute(SolarAttributes.THORNS.get());
        Entity direct = event.getSource().getDirectEntity();
        Entity indirect = event.getSource().getEntity();
        DamageSource source = SolarDamageTypes.THORNS.get(livingBeAttacked.level(), livingBeAttacked);
        if (attribute != null && attribute.getValue() > 0 && event.getSource().type() != source.type()) {
            if (direct instanceof LivingEntity) {
                direct.hurt(source, (float) attribute.getValue());
                direct.invulnerableTime = 0; //使得荆棘也能同时生效
            }
        }
    }

}
