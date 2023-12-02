package io.github.moehreag.searchInResources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.github.moehreag.searchInResources.mixin.BuiltInModResourcePackAccessor;
import io.github.moehreag.searchInResources.mixin.CustomResourcePackAccessor;
import io.github.moehreag.searchInResources.mixin.FallbackResourceManagerAccessor;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.SimpleResource;
import net.minecraft.client.resource.manager.FallbackResourceManager;
import net.minecraft.client.resource.pack.*;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.resource.loader.impl.BuiltInModResourcePack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Unique;

public class SearchInResources {
	@Getter
	private static final SearchInResources instance = new SearchInResources();

	private boolean debug;
	private final Logger searchLogger = LogManager.getLogger("Resource Search");

	public Map<Identifier, Resource> search(String namespace, String startingPath, Predicate<Identifier> allowedPathPredicate, boolean debug, Map<String, FallbackResourceManager> packs){
		this.debug = debug;
		Map<Identifier, Resource> map = new LinkedHashMap<>();

		try {
			for (FallbackResourceManager manager : packs.values()) {
				for (ResourcePack pack : ((FallbackResourceManagerAccessor) manager).getResourcePacks()) {
					if (!namespace.isEmpty()) {
						search(namespace, pack, startingPath, allowedPathPredicate, manager, map);
					} else {
						for (String s : pack.getNamespaces()) {
							search(s, pack, startingPath, allowedPathPredicate, manager, map);
						}
					}

				}
			}
		} catch (Exception e) {
			searchLogger.error("Error while searching: ", e);
		}

		return map;
	}


	@Unique
	private void search(String namespace, ResourcePack pack, String startingPath, Predicate<Identifier> allowedPathPredicate, FallbackResourceManager manager, Map<Identifier, Resource> map) throws IOException {

		Predicate<Identifier> predicate = identifier -> {
			if (!startingPath.isEmpty()){
				return identifier.getPath().startsWith(startingPath) && allowedPathPredicate.test(identifier);
			}
			return allowedPathPredicate.test(identifier);
		};

		if (pack instanceof BuiltInModResourcePack) {
			List<Path> paths = ((BuiltInModResourcePackAccessor) pack).getRoots().stream().map(pa -> pa.resolve("assets")).collect(Collectors.toList());
			for (Path p : paths) {
				walkTree(namespace, p, predicate, map, manager);
			}
		} else if (pack instanceof CustomResourcePack) {
			File f = ((CustomResourcePackAccessor) pack).getFile();

			if (pack instanceof DirectoryResourcePack) {
				walkTree(namespace, f.toPath().resolve("assets"), predicate, map, manager);
			} else if (pack instanceof ZippedResourcePack) {
				try (FileSystem system = FileSystems.newFileSystem(f.toPath(), (ClassLoader) null)) {
					walkTree(namespace, system.getPath("assets"), predicate, map, manager);
				}
			}
		} else if (pack instanceof BuiltInResourcePack) {
			Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer("minecraft");
			if (optional.isPresent()) {
				ModContainer c = optional.get();

				List<Path> paths = c.getRootPaths().stream().map(pa -> pa.resolve("assets")).collect(Collectors.toList());
				for (Path p : paths) {
					walkTree(namespace, p, predicate, map, manager);
				}
			}
		} else {
			if (debug) {
				searchLogger.warn("Unhandled pack: " + pack.getName() + " Instance: " + pack);
			}
		}
	}

	@Unique
	private void walkTree(String namespace, Path start, Predicate<Identifier> predicate, Map<Identifier, Resource> resources, FallbackResourceManager manager) throws IOException {
		if (!Files.exists(start)) {
			return;
		}
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				try {
					String[] parts = file.toString().split(namespace);
					if (parts.length < 2) {
						return FileVisitResult.CONTINUE;
					}
					String location = parts[parts.length - 1];
					if (!location.startsWith("/")){
						return FileVisitResult.CONTINUE;
					}
					Identifier id = new Identifier(namespace, file.getFileSystem().getPath(location.substring(1)).toString());
					Path metadata = file.getParent().resolve(file.getFileName().toString() + ".mcmeta");
					InputStream data = Files.exists(metadata) ? Files.newInputStream(metadata) : null;
					if (debug) {
						searchLogger.info("File: " + id + " (" + file + ") Passes: " + predicate.test(id));
					}
					if (predicate.test(id)) {
						resources.put(id, new SimpleResource(file.getFileName().toString(), id,
								Files.newInputStream(file), data, ((FallbackResourceManagerAccessor) manager).getSerializer()));
					}
				} catch (Exception e) {
					searchLogger.error("Error while traversing file tree: ", e);
					searchLogger.warn(file);
					searchLogger.warn(namespace);
				}
				return super.visitFile(file, attrs);
			}
		});
	}
}
