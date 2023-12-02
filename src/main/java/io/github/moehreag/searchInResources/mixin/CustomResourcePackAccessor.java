package io.github.moehreag.searchInResources.mixin;

import java.io.File;

import net.minecraft.client.resource.pack.CustomResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CustomResourcePack.class)
public interface CustomResourcePackAccessor {

	@Accessor
	File getFile();

}
