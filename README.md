# Search In Resources

This small lib provides the `ResourceManager.findResources(...)` syntax for Minecraft 1.8.9.

Might be janky. Should work though.

### Adding this library to your project

Add to your gradle build script:

```groovy
repositories {
    maven {
        url "https://moehreag.duckdns.org/maven/releases"
    }
}

dependencies {
    modImplementation include("io.github.moehreag:search-in-resources:<VERSION>")
}
```

### Usage:
You need an instance of a ResourceManager, 
but since you'll probably have a IdentifiableResourceReloadListener that won't be a problem for you.


```java
public class YourReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public void reload(ResourceManager resourceManager) {
        resourceManager.findResources(...);
    }
}
```
