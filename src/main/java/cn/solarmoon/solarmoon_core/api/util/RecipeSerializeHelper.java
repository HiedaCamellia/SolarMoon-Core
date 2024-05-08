package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.util.RecipeUtil;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import cn.solarmoon.solarmoon_core.api.util.UUIDGetter;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 超便捷快速json配方解码，这里所有的内容都是写成可选的（也就是输错主条目不报错）
 */
public class RecipeSerializeHelper {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /**
     * @return 机会物品源列表
     */
    public static NonNullList<ChanceResult> readChanceResults(JsonObject json, String id) {
        NonNullList<ChanceResult> results = NonNullList.create();
        if (json.has(id)) {
            for (JsonElement result : GsonHelper.getAsJsonArray(json, id)) {
                results.add(ChanceResult.deserialize(result));
            }
        }
        return results;
    }

    /**
     * @return 从buf中读取机会物品源列表
     */
    public static NonNullList<ChanceResult> readChanceResults(FriendlyByteBuf buffer) {
        int i = buffer.readVarInt();
        NonNullList<ChanceResult> resultsIn = NonNullList.withSize(i, ChanceResult.EMPTY);
        resultsIn.replaceAll(ignored -> ChanceResult.read(buffer));
        return resultsIn;
    }

    /**
     * 写入机会物品源列表
     */
    public static void writeChanceResults(FriendlyByteBuf buffer, NonNullList<ChanceResult> results) {
        buffer.writeVarInt(results.size());
        for (ChanceResult result : results) {
            result.write(buffer);
        }
    }

    //找机会改个能读tag的，不过现在暂时用不到
    public static Fluid readFluid(JsonObject json, String ide) {
        Fluid fluid = Fluids.EMPTY;
        if (json.has(ide)) {
            String id = GsonHelper.getAsString(GsonHelper.getAsJsonObject(json, ide), "id");
            fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(id));
            if (fluid == null) fluid = Fluids.EMPTY;
        }
        return fluid;
    }

    public static Fluid readFluid(FriendlyByteBuf buffer) {
        return buffer.readRegistryId();
    }

    public static void writeFluid(FriendlyByteBuf buffer, Fluid fluid) {
        buffer.writeRegistryId(ForgeRegistries.FLUIDS, fluid);
    }

    /**
     * read buffer和write有直接的方法，只有json读取需要这个。
     * @return null时返回空
     */
    public static FluidStack readFluidStack(JsonObject json, String id) {
        if (json.has(id)) {
            JsonObject jsonObject = GsonHelper.getAsJsonObject(json, id);
            Fluid fluid = readFluid(json, id);
            int amount = 1000;
            if (jsonObject.has("amount")) {
                amount = GsonHelper.getAsInt(jsonObject, "amount");
            }
            FluidStack read = new FluidStack(fluid, amount);
            if (GsonHelper.isValidPrimitive(jsonObject, "nbt")) {
                try {
                    JsonElement element = jsonObject.get("nbt");
                    read.setTag(TagParser.parseTag(element.isJsonObject() ? GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
                } catch (CommandSyntaxException var7) {
                    var7.printStackTrace();
                }
            }
            return read;
        }
        else return FluidStack.EMPTY;
    }

    public static ItemStack readItemStack(JsonObject json, String id) {
        return CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, id), true);
    }

    public static List<ItemStack> readItemStacks(JsonObject json, String id) {
        List<ItemStack> stacks = new ArrayList<>();
        if (json.has(id)) {
            for (var element : GsonHelper.getAsJsonArray(json, id)) {
                stacks.add(CraftingHelper.getItemStack(element.getAsJsonObject(), true));
            }
        }
        return stacks;
    }

    public static List<ItemStack> readItemStacks(FriendlyByteBuf buffer) {
        List<ItemStack> stacks = new ArrayList<>();
        int itemCount = buffer.readVarInt();
        for (int i = 0; i < itemCount; i++) {
            stacks.add(buffer.readItem());
        }
        return stacks;
    }

    public static void writeItemStacks(FriendlyByteBuf buffer, List<ItemStack> stacks) {
        buffer.writeVarInt(stacks.size());
        for (ItemStack stack : stacks) {
            buffer.writeItem(stack);
        }
    }

    public static List<Ingredient> readIngredients(JsonObject json, String id) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (json.has(id)) {
            for (var element : GsonHelper.getAsJsonArray(json, id)) {
                ingredients.add(Ingredient.fromJson(element));
            }
        }
        return ingredients;
    }

    public static List<Ingredient> readIngredients(FriendlyByteBuf buffer) {
        List<Ingredient> ingredients = new ArrayList<>();
        int inCount = buffer.readVarInt();
        for (int i = 0; i < inCount; i++) {
            ingredients.add(Ingredient.fromNetwork(buffer));
        }
        return ingredients;
    }

    public static void writeIngredients(FriendlyByteBuf buffer, List<Ingredient> ingredients) {
        buffer.writeVarInt(ingredients.size());
        for (var in : ingredients) {
            in.toNetwork(buffer);
        }
    }

    public static Block readBlock(JsonObject json, String id) {
        if (json.has(id)) {
            JsonObject bj = GsonHelper.getAsJsonObject(json, id);
            String blockId = GsonHelper.getAsString(bj, "id");
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
            if (block != null) return block;
        }
        return Blocks.AIR;
    }

    public static Block readBlock(JsonObject json, String id, Block blockDefault) {
        if (json.has(id)) {
            JsonObject bj = GsonHelper.getAsJsonObject(json, id);
            String blockId = GsonHelper.getAsString(bj, "id");
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
            if (block != null) return block;
        }
        return blockDefault;
    }

    public static Block readBlock(FriendlyByteBuf buf) {
        return buf.readRegistryId();
    }

    public static void writeBlock(FriendlyByteBuf buf, Block block) {
        buf.writeRegistryId(ForgeRegistries.BLOCKS, block);
    }

}
