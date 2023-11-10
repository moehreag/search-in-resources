package io.github.moehreag.searchInResources;

import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Predicate;

public interface SearchableResourceManager extends ResourceManager {

    Logger searchLogger = LogManager.getLogger("Resource Search");

    default Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate){
        return findResources("", startingPath, allowedPathPredicate);
    }

    default Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate){
        return findResources(namespace, startingPath, allowedPathPredicate, false);
    }

    Map<Identifier, Resource> findResources(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate, boolean debug);
}
