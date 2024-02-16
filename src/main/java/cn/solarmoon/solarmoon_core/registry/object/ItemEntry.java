package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemEntry<I extends Item> {

    private final DeferredRegister<Item> itemRegister;
    private String id;
    private Supplier<I> itemSupplier;
    private RegistryObject<I> itemObject;

    public ItemEntry(DeferredRegister<Item> itemRegister) {
        this.itemRegister = itemRegister;
    }

    public ItemEntry<I> id(String id) {
        this.id = id;
        return this;
    }

    public ItemEntry<I> bound(Supplier<I> blockSupplier) {
        this.itemSupplier = blockSupplier;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends I> ItemEntry<T> build() {
        this.itemObject = itemRegister.register(id, itemSupplier);
        return (ItemEntry<T>) this;
    }

    public I get() {
        return itemObject.get();
    }

    public RegistryObject<I> getObject() {
        return itemObject;
    }

}
