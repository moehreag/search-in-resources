package io.github.moehreag.searchInResources;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.client.resource.Resource;
import net.minecraft.resource.Identifier;

public interface SearchableResourceManager {

	default Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
		return findResources("", startingPath, allowedPathPredicate);
	}

	default Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate) {
		return findResources(namespace, startingPath, allowedPathPredicate, false);
	}

	default Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate, boolean debug) {
		return Collections.emptyMap();
	}
}
