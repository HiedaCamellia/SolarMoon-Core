package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarNetPacks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CommonParticleSpawner {

    public static void sweep(LivingEntity entity, Level level) {
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(entity, 1);
        if (!level.isClientSide) {
            SolarNetPacks.CLIENT.getSender().send(SolarNETList.PARTICLE_SWEEP, List.of(spawnPos));
        } else {
            level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
        }
    }

}
