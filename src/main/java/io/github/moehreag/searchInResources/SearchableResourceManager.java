package io.github.moehreag.searchInResources;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Predicate;

public interface SearchableResourceManager extends ResourceManager {

    default Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate){
        return findResources("", startingPath, allowedPathPredicate);
    }

    Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate);
}
