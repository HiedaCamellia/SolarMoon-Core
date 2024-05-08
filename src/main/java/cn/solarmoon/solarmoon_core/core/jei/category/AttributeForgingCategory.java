package cn.solarmoon.solarmoon_core.core.jei.category;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import cn.solarmoon.solarmoon_core.api.util.AttributeUtil;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.EmbeddingData;
import cn.solarmoon.solarmoon_core.core.common.recipe.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AttributeForgingCategory extends BaseJEICategory<AttributeForgingRecipe> {

    public AttributeForgingCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AttributeForgingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder leftInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.input());
        IRecipeSlotBuilder rightInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 43, 1).addItemStack(recipe.material());
        List<ItemStack> stacks = new ArrayList<>();
        for (var in : recipe.input().getItems()) {
            ItemStack copy = in.copy();
            AttributeUtil.addAttributeToStack(copy, recipe);
            EmbeddingData resultEmbedData = CapabilityUtil.getData(copy, SolarCapabilities.ITEMSTACK_DATA).getEmbeddingData();
            resultEmbedData.embedItem(recipe.material(), false);
            stacks.add(copy);
        }
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 1).addItemStacks(stacks);

        if (recipe.input().getItems().length == stacks.size()) {
            builder.createFocusLink(leftInputSlot, outputSlot);
        }
    }

    @Override
    public void draw(AttributeForgingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        helper.getSlotDrawable().draw(guiGraphics, 0, 0);
        helper.getSlotDrawable().draw(guiGraphics, 42, 0);
        helper.getSlotDrawable().draw(guiGraphics, 94,0);
        PLUS.draw(guiGraphics, 23, 2);
        EMPTY_ARROW.draw(guiGraphics, 66, 1);
        if (mouseX >= 66 && mouseX <= 88 && mouseY >= 1 && mouseY <= 17) {
            List<Component> components = new ArrayList<>();
            components.add(Component.literal(""));
            components.add(SolarMoonCore.TRANSLATOR.set("jei", "attribute_forging.max_forging_count",
                    ChatFormatting.WHITE, recipe.maxForgeCount()));
            int expX = (int) mouseX + 12;
            int expY = (int) mouseY - 11;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(0, 0, 1000);
            EXP.draw(guiGraphics, expX, expY);
            guiGraphics.drawString(Minecraft.getInstance().font,
                    Component.literal("x" + recipe.expCost()).withStyle(ChatFormatting.GREEN),
                    expX + 9, expY, 0xFFFFFF);
            poseStack.popPose();
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, components, (int) mouseX, (int) mouseY);
        }
    }

}
