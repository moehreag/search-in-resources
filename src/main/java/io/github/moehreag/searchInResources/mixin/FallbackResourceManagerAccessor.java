package io.github.moehreag.searchInResources.mixin;

import net.minecraft.client.resource.FallbackResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.MetadataSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(FallbackResourceManager.class)
public interface FallbackResourceManagerAccessor {

    @Accessor
    List<ResourcePack> getResourcePacks();

    @Accessor
    MetadataSerializer getSerializer();
}
