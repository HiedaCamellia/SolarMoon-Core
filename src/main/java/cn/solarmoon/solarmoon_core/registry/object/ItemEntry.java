package cn.solarmoon.solarmoon_core.registry.object;

import cn.solarmoon.solarmoon_core.client.ItemRenderer.BaseItemRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class ItemEntry<I extends Item> {

    private final DeferredRegister<Item> itemRegister;
    private String id;
    private Supplier<I> itemSupplier;
    private RegistryObject<I> itemObject;
    private boolean condition = true;

    public ItemEntry(DeferredRegister<Item> itemRegister) {
        this.itemRegister = itemRegister;
    }

    public ItemEntry<I> id(String id) {
        this.id = id;
        return this;
    }

    public ItemEntry<I> bound(Supplier<I> itemSupplier) {
        this.itemSupplier = itemSupplier;
        return this;
    }

    public ItemEntry<I> condition(boolean condition) {
        this.condition = condition;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends I> ItemEntry<T> build() {
        if (condition) {
            this.itemObject = itemRegister.register(id, itemSupplier);
        }
        return (ItemEntry<T>) this;
    }

    public I get() {
        return itemObject.get();
    }

    public RegistryObject<I> getObject() {
        return itemObject;
    }

}
