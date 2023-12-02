package io.github.moehreag.searchInResources;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.Resource;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.resource.loader.api.ResourceLoaderEvents;

public class SearchingTest implements ModInitializer {
	@Override
	public void init() {
		ResourceLoaderEvents.END_RESOURCE_RELOAD.register(() -> {
			Map<Identifier, Resource> map = Minecraft.getInstance().getResourceManager().findResources("", "", identifier ->
					identifier.getPath().endsWith("icon.png"));
			map.keySet().forEach(System.out::println);
		});
	}
}
