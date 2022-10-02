# Search In Resources

This small lib provides the `ResourceManager.findResources(...)` syntax for Minecraft 1.8.9.

Might be janky. Should work though.

### Adding this library to your project

Add to your gradle build script:

```groovy
repositories {
    maven {
        name = "JitPack"
        url = "https://jitpack.io"
    }
}

dependencies {
    modImplementation include("com.github.moehreag:search-in-resources:1.0.0")
}
```

### Usage:
You need an instance of a ResourceManager, 
but since you'll probably have a IdentifiableResourceReloadListener that won't be a problem for you.


```java
public class YourReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public void reload(ResourceManager resourceManager) {
        ((SearchableResourceManager)resourceManager).findResources();
    }
}
```