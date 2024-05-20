package cn.solarmoon.solarmoon_core.api.client.model.item;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 物品贴图的叠加模型，main为在上层的贴图，overlay为下层
 */
public class CompositeBakedModel implements BakedModel {

    private final BakedModel main;
    private final BakedModel overlay;

    /**
     * 物品贴图的叠加模型
     * @param main 上层贴图
     * @param overlay 下层贴图
     */
    public CompositeBakedModel(BakedModel main, BakedModel overlay) {
        this.main = main;
        this.overlay = overlay;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        var m = new ArrayList<>(overlay.getQuads(state, side, rand)); // 必须新建一个不然会往原有的无限累加
        var o = main.getQuads(state, side, rand);
        m.addAll(o);
        return Collections.unmodifiableList(m);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return main.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return main.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return main.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return main.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return main.getTransforms();
    }

}
