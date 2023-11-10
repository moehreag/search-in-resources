package io.github.moehreag.searchInResources.mixin;

import net.minecraft.client.resource.pack.CustomResourcePack;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CustomResourcePack.class)
public interface AbstractFileResourcePackAccessor {

	@Invoker
	static String callGetPathToResource(Identifier id) {
		throw new UnsupportedOperationException();
	}

}
