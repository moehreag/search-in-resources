package io.github.moehreag.searchInResources.mixin;

import java.nio.file.Path;
import java.util.List;

import net.fabricmc.loader.api.ModContainer;
import net.ornithemc.osl.resource.loader.api.ModResourcePack;
import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltInModResourcePack.class)
public interface ModNioResourcePackAccessor {

    @Accessor(remap = false)
    List<Path> getRoots();

    @Accessor(remap = false)
    ModContainer getMod();
}
