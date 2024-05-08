package cn.solarmoon.solarmoon_core.api.common.recipe.serializable;

import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import cn.solarmoon.solarmoon_core.api.util.UUIDGetter;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public record AttributeData(Attribute attribute, AttributeModifier attributeModifier) {

    public static AttributeData deserialize(JsonObject jsonObject) {
        String id = GsonHelper.getAsString(jsonObject, "id");

        double value = GsonHelper.getAsDouble(jsonObject, "value");

        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(id));
        UUID uuid = UUID.fromString(GsonHelper.getAsString(jsonObject, "uuid", UUIDGetter.get(attribute).toString()));

        String op = GsonHelper.getAsString(jsonObject, "operation", "addition");
        AttributeModifier.Operation operation;
        switch (op) {
            case "addition" -> operation = AttributeModifier.Operation.ADDITION;
            case "multiply_base" -> operation = AttributeModifier.Operation.MULTIPLY_BASE;
            case "multiply_total" -> operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
            default -> throw new RuntimeException("Unknown attribute operation, there are only 'addition', 'multiply_base' and 'multiply_total' can be choose.");
        }

        if (attribute == null) throw new RuntimeException("Unknown attribute id. Please Check your Recipe data.");
        AttributeModifier modifier = new AttributeModifier(uuid, TextUtil.splitFromColon(id), value, operation);
        return new AttributeData(attribute, modifier);
    }

    public static AttributeData read(FriendlyByteBuf buffer) {
        String id = buffer.readUtf();
        double value = buffer.readDouble();
        UUID uuid = buffer.readUUID();
        AttributeModifier.Operation operation = buffer.readEnum(AttributeModifier.Operation.class);

        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(id));
        AttributeModifier modifier = new AttributeModifier(uuid, TextUtil.splitFromColon(id), value, operation);
        return new AttributeData(attribute, modifier);
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(ForgeRegistries.ATTRIBUTES.getKey(attribute).toString());
        buffer.writeDouble(attributeModifier.getAmount());
        buffer.writeUUID(attributeModifier.getId());
        buffer.writeEnum(attributeModifier.getOperation());
    }

}
