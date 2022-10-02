package io.github.moehreag.searchInResources.mixin;

import net.legacyfabric.fabric.impl.resource.loader.ModNioResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.file.Path;

@Mixin(ModNioResourcePack.class)
public interface ModNioResourcePackAccessor {

    @Accessor(remap = false)
    Path getBasePath();
}
