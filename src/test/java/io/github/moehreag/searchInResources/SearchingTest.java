package io.github.moehreag.searchInResources;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.Resource;
import net.minecraft.client.resource.manager.ResourceManager;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;

public class SearchingTest implements ModInitializer {
	@Override
	public void init() {
		ResourceLoaderEvents.END_RESOURCE_RELOAD.register(() -> {

			//runTest("", "", identifier -> identifier.getPath().endsWith("icon.png"));
			//runTest("", "lang", identifier -> true);
			runTest("search-in-resources", "", identifier -> identifier.getPath().endsWith(".png"));
			runTest("search-in-resources", "directory", identifier -> identifier.getPath().endsWith(".png"));
			runTest("search-in-resources", "", identifier -> identifier.getPath().endsWith(".json"));
		});
	}

	private void runTest(String namespace, String start, Predicate<Identifier> predicate){
		ResourceManager manager = Minecraft.getInstance().getResourceManager();
		System.out.printf("-----##### Running Test in namespace %s, Start: %s%n", namespace, start);
		Map<Identifier, Resource> map = ((SearchableResourceManager)manager).findResources(namespace, start, predicate);
		map.keySet().forEach(System.out::println);
		System.out.println("-----##### Finished Test!");
	}
}
