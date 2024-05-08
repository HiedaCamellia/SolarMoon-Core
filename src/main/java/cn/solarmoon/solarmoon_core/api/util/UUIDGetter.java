package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.core.common.registry.SolarAttributes;
import net.minecraft.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ArmorItem;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDGetter {

    private static final EnumMap<ArmorItem.Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        map.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        map.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        map.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    private static final Map<Attribute, UUID> MODIFIER_UUID_PER_ATTRIBUTE = Util.make(new HashMap<>(), (map) -> {
        map.put(SolarAttributes.THORNS.get(), UUID.fromString("52C60B59-E3C9-A909-EF70-2BB023E1479D"));
    });

    public static UUID get(ArmorItem.Type type) {
        return ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
    }

    public static UUID get(Attribute attribute) {
        return MODIFIER_UUID_PER_ATTRIBUTE.get(attribute);
    }

}
