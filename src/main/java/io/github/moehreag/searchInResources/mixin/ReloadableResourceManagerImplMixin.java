package io.github.moehreag.searchInResources.mixin;

import io.github.moehreag.searchInResources.SearchableResourceManager;
import net.legacyfabric.fabric.impl.resource.loader.ModNioResourcePack;
import net.minecraft.client.resource.FallbackResourceManager;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class ReloadableResourceManagerImplMixin implements SearchableResourceManager {

    @Shadow @Final private Map<String, FallbackResourceManager> fallbackManagers;

    @Shadow @Final private static Logger LOGGER;

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        Map<Identifier, Resource> map = new LinkedHashMap<>();

        try {
            for (FallbackResourceManager manager : fallbackManagers.values()) {
                for (ResourcePack pack : ((FallbackResourceManagerAccessor) manager).getResourcePacks().stream().filter(resourcePack ->
                        !(resourcePack instanceof DefaultResourcePack)).collect(Collectors.toList())) {

                    for(Object s : pack.getNamespaces()) {
                        if(s instanceof String) {
                            search((String) s, pack, startingPath, allowedPathPredicate, manager, map);
                        }
                    }

                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    private void search(String s, ResourcePack pack, String startingPath, Predicate<Identifier> allowedPathPredicate, FallbackResourceManager manager, Map<Identifier, Resource> map) throws IOException {
        if(pack instanceof ModNioResourcePack) {
            Path p = ((ModNioResourcePackAccessor) pack).getBasePath().resolve("assets").resolve(s).resolve(startingPath);
            if (p.toString().contains(".jar") || p.toString().contains(".zip") || p.startsWith("/assets/")) {
                try (ZipFile file = new ZipFile(((ModNioResourcePack)pack).getOwner().getOrigin().toString())){
                    final Enumeration<? extends ZipEntry> entries = file.entries();
                    while (entries.hasMoreElements()){
                        ZipEntry e = entries.nextElement();
                        searchInZipFiles(map, s, e, allowedPathPredicate, manager, file, startingPath);
                    }
                } catch (Exception ignored){
                    //ignored.printStackTrace();
                }
            }
        } else {
            File base = new File("resourcepacks/" + pack.getName() + "/" + AbstractFileResourcePackAccessor.callGetFilename(new Identifier(startingPath, "")));

            System.out.println("Searching in Resource Pack " + pack.getName() + " Available Namespaces: " + s + " Base File: " + base.getAbsolutePath());

            if (base.toString().contains(".zip")) {
                try (ZipFile file = new ZipFile(base.toString().split(".zip")[0] + ".zip")) {
                    Enumeration<? extends ZipEntry> entries = file.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry e = entries.nextElement();
                        searchInZipFiles(map, s, e, allowedPathPredicate, manager, file, startingPath);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            } else {

                File[] files = base.listFiles((dir, name) -> allowedPathPredicate.test(new Identifier(dir.getName(), name)));
                System.out.println("Exists " + base.exists() + " Dir: " + base.isDirectory() + " Contents: " + Arrays.toString(base.listFiles()));
                if (files != null) {
                    List<File> list = Arrays.stream(files).sorted().collect(Collectors.toList());
                    System.out.println("Pack " + pack.getName() + " contains files " + Arrays.toString(files));
                    for (File f : list) {
                        Identifier id = new Identifier(startingPath, f.getName());
                        map.put(id, new ResourceImpl(id, Files.newInputStream(f.toPath()), new InputStream() {
                            @Override
                            public int read() {
                                return 0;
                            }
                        }, ((FallbackResourceManagerAccessor) manager).getSerializer()));
                    }
                }
            }
        }
    }

    private static void searchInZipFiles(Map<Identifier, Resource> map, String namespace, ZipEntry zipEntry, Predicate<Identifier> predicate, FallbackResourceManager manager, ZipFile root, String start){

        if(!zipEntry.isDirectory()){
            Identifier id = new Identifier(namespace, zipEntry.getName());
            if(id.getPath().contains("assets/"+id.getNamespace()+"/"+start) && predicate.test(id)){
                try {
                    String text = new BufferedReader(
                            new InputStreamReader(root.getInputStream(zipEntry), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    map.put(id, new ResourceImpl(id, IOUtils.toInputStream(text), new InputStream() {
                        @Override
                        public int read() {
                            return 0;
                        }
                    }, ((FallbackResourceManagerAccessor) manager).getSerializer()));
                } catch (Exception e){
                    LOGGER.info("Couldn't add Entry "+zipEntry.getName());
                    //e.printStackTrace();
                }
            }
        }
    }
}
