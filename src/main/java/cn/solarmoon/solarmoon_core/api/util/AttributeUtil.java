package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.core.common.recipe.AttributeForgingRecipe;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class AttributeUtil {

    public static void addAttributeModifier(ItemStack stack, Attribute attribute, AttributeModifier modifier, @Nullable EquipmentSlot slot) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("AttributeModifiers", 9)) {
            tag.put("AttributeModifiers", new ListTag());
        }

        ListTag listtag = tag.getList("AttributeModifiers", 10);
        CompoundTag attTag = modifier.save();
        String name = BuiltInRegistries.ATTRIBUTE.getKey(attribute).toString();
        attTag.putString("AttributeName", name);
        if (slot != null) {
            attTag.putString("Slot", slot.getName());
        }

        boolean doAdd = true;
        for (int i = 0; i < listtag.size(); i++) {
            CompoundTag presentTag = listtag.getCompound(i);
            // 检查当前属性修饰符是否与要添加的属性修饰符冲突
            if (presentTag.getString("AttributeName").equals(name)) {
                // 如果冲突，就覆盖，并且后续不再添加
                listtag.set(i, attTag);
                doAdd = false;
            }
        }

        if (doAdd) listtag.add(attTag);
    }

    public static void addAttributeToStack(ItemStack stackIn, AttributeForgingRecipe recipe) {
        EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stackIn);
        var map = stackIn.getAttributeModifiers(slot);
        var optionalSet = map.get(recipe.getAttribute()).stream().findFirst();
        double addValue = recipe.getAddValue();
        if (optionalSet.isPresent()) {
            addValue = optionalSet.get().getAmount() + addValue;
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(map);
        AttributeModifier attributeAddon = new AttributeModifier(recipe.getUUID(), recipe.getName(), addValue, recipe.getOperation());
        builder.put(recipe.getAttribute(), attributeAddon);
        var attributeMap = builder.build();
        for (var set : attributeMap.entries()) {
            AttributeUtil.addAttributeModifier(stackIn, set.getKey(), set.getValue(), slot);
        }
    }

}
