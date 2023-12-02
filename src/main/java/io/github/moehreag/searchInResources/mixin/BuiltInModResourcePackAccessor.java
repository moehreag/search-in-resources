package io.github.moehreag.searchInResources.mixin;

import java.nio.file.Path;
import java.util.List;

import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltInModResourcePack.class)
public interface BuiltInModResourcePackAccessor {

	@Accessor(remap = false)
	List<Path> getRoots();

}
