package io.github.moehreag.searchInResources;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;

public interface SearchableResourceManager extends ResourceManager {

    Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate);
}
