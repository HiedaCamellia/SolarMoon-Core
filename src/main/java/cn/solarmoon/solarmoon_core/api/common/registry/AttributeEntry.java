package cn.solarmoon.solarmoon_core.api.common.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AttributeEntry {

    private final String modId;
    private final DeferredRegister<Attribute> register;

    private String id;
    private Supplier<? extends Attribute> attributeSup;
    private boolean applyToLivingEntity;
    private RegistryObject<Attribute> attributeObject;

    public AttributeEntry(DeferredRegister<Attribute> register, String modId) {
        this.register = register;
        this.modId = modId;
    }

    public AttributeEntry id(String id) {
        this.id = id;
        return this;
    }

    public AttributeEntry bound(Supplier<? extends Attribute> attributeSup) {
        this.attributeSup = attributeSup;
        return this;
    }

    public AttributeEntry boundRanged(double defaultValue, double min, double max) {
        attributeSup = () -> new RangedAttribute( "attribute" + "." + modId + "." + id, defaultValue, min, max);
        return this;
    }

    public AttributeEntry applyToLivingEntity() {
        this.applyToLivingEntity = true;
        return this;
    }

    public AttributeEntry build() {
        attributeObject = register.register(id, attributeSup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::applyAttribute);
        return this;
    }

    public Attribute get() {
        return attributeObject.get();
    }

    public RegistryObject<Attribute> getObject() {
        return attributeObject;
    }

    @SubscribeEvent
    public void applyAttribute(EntityAttributeModificationEvent event) {
        if (applyToLivingEntity) {
            for (var entity : event.getTypes()) {
                event.add(entity, get());
            }
        }
    }

}
