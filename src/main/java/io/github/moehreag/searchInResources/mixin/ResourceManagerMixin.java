package io.github.moehreag.searchInResources.mixin;

import io.github.moehreag.searchInResources.SearchableResourceManager;
import net.minecraft.client.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ResourceManager.class)
public interface ResourceManagerMixin extends SearchableResourceManager {

}
