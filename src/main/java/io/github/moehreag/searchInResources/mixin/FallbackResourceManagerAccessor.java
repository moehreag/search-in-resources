package io.github.moehreag.searchInResources.mixin;

import java.util.List;

import net.minecraft.client.resource.manager.FallbackResourceManager;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallbackResourceManager.class)
public interface FallbackResourceManagerAccessor {

    @Accessor("fallbacks")
    List<ResourcePack> getResourcePacks();

    @Accessor("metadataSerializers")
    ResourceMetadataSerializerRegistry getSerializer();
}
