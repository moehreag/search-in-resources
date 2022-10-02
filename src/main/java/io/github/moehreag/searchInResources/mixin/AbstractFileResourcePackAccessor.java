package io.github.moehreag.searchInResources.mixin;

import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFileResourcePack.class)
public interface AbstractFileResourcePackAccessor {

    @Invoker
    static String callGetFilename(Identifier id) {
        throw new UnsupportedOperationException();
    }

}
