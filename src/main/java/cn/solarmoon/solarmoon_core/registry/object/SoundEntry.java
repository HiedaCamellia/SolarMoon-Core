package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SoundEntry {

    private final DeferredRegister<SoundEvent> soundRegister;
    private final String modId;

    private String name;
    private RegistryObject<SoundEvent> soundObject;

    public SoundEntry(DeferredRegister<SoundEvent> soundRegister, String modId) {
        this.soundRegister = soundRegister;
        this.modId = modId;
    }

    public SoundEntry name(String name) {
        this.name = name;
        return this;
    }

    public SoundEntry build() {
        this.soundObject = soundRegister.register(name, () ->
                SoundEvent.createVariableRangeEvent(new ResourceLocation(modId, name)));
        return this;
    }

    public SoundEvent get() {
        return soundObject.get();
    }

}
