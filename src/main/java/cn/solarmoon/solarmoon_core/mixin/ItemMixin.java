package cn.solarmoon.solarmoon_core.mixin;


import cn.solarmoon.solarmoon_core.client.ItemRenderer.ICustomItemRendererProvider;
import cn.solarmoon.solarmoon_core.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.registry.Capabilities;
import cn.solarmoon.solarmoon_core.registry.Packs;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.namespace.NETList;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow
    protected static BlockHitResult getPlayerPOVHitResult(Level p_41436_, Player p_41437_, ClipContext.Fluid p_41438_) {
        return null;
    }

    /**
     * IClientItemExtensions接口实现，获取手中物品渲染器
     */
    @Inject(remap = false, method = "initializeClient", at = @At("HEAD"))
    public void initializeClient(Consumer<IClientItemExtensions> consumer, CallbackInfo ci) {
        if(this instanceof ICustomItemRendererProvider provider) {
            consumer.accept(new IClientItemExtensions() {
                final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(provider.getRendererFactory()::get);

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer.get();
                }
            });
        }
    }

    /**
     * IOptionalRecipeItem接口实现，随时更新目视配方
     */
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isHeld, CallbackInfo ci) {
        if (stack.getItem() instanceof IOptionalRecipeItem<?> orStack) {

            if (entity instanceof Player player && isHeld) {
                BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
                assert hit != null;
                orStack.recipeCheckAndUpdate(level, hit);

                //客户端同步配方选择序列数
                if (player instanceof ServerPlayer sp) {
                    RecipeSelectorData selector = CapabilityUtil.getData(sp, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                    Packs.BASE_CLIENT_PACK.getSender().send(NETList.SYNC_INDEX, selector.getIndex(orStack.getRecipeType()), orStack.getRecipeType().toString());
                    Packs.BASE_CLIENT_PACK.getSender().send(NETList.SYNC_RECIPE_INDEX, selector.getRecipeIndex(orStack.getRecipeType()), orStack.getRecipeType().toString());
                }

            }

        }
    }

}
