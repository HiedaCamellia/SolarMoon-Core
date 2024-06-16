package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.network.NETList;
import cn.solarmoon.solarmoon_core.registry.common.SolarNetPacks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CommonParticleSpawner {

    public static void sweep(LivingEntity entity, Level level) {
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(entity, 1);
        if (!level.isClientSide) {
            SolarNetPacks.CLIENT.getSender()
                    .vec3List(List.of(spawnPos))
                    .send(NETList.PARTICLE_SWEEP);
        } else {
            level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
        }
    }

}
