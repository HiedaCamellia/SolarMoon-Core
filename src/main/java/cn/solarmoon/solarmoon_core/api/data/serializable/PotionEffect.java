package cn.solarmoon.solarmoon_core.api.data.serializable;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 药水效果
 */
public class PotionEffect {

    @SerializedName("id")
    public String id;

    @SerializedName("duration")
    public int duration;

    @SerializedName("amplifier")
    public int amplifier;

    @SerializedName("invisible")
    public boolean invisible;

    @SerializedName("chance")
    public double chance;

    @SerializedName("hide_icon")
    public boolean hide_icon;

    /**
     * @return 返回对应药水效果
     */
    public MobEffectInstance getEffect() {
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(this.id));
        if(effect == null) effect = MobEffects.MOVEMENT_SPEED;
        return new MobEffectInstance(effect, getDuration(), amplifier, false, !invisible, !hide_icon);
    }

    /**
     * @return 被添加药水的概率
     */
    public double getChance() {
        if(chance >= 1 || chance == 0.0) return 1;
        return chance;
    }

    /**
     * @return 把药水持续时间改为秒制
     */
    public int getDuration() {
        return duration * 20;
    }

}
