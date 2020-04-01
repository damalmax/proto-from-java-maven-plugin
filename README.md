# (WIP) proto-from-java-maven-plugin 

# Maven usage example:
```xml
<plugin>
    <groupId>com.mdadzerkin</groupId>
    <artifactId>proto-from-java-maven-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <excludePackageNames>gl4java:com.example:com</excludePackageNames>
    </configuration>
</plugin>
```