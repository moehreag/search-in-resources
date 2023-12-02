package io.github.moehreag.searchInResources.mixin;

import java.util.Map;
import java.util.function.Predicate;

import io.github.moehreag.searchInResources.SearchInResources;
import io.github.moehreag.searchInResources.SearchableResourceManager;
import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.manager.FallbackResourceManager;
import net.minecraft.client.resource.manager.SimpleReloadableResourceManager;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class ReloadableResourceManagerImplMixin implements SearchableResourceManager {

	@Shadow
	@Final
	private Map<String, FallbackResourceManager> packs;

	@SuppressWarnings("AddedMixinMembersNamePattern")
	@Override
	public Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate, boolean debug) {
		return SearchInResources.getInstance().search(namespace, startingPath, allowedPathPredicate, debug, packs);
	}


}
