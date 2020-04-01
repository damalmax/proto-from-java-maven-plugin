package mojo;

import com.github.javaparser.JavaParser;
import exception.ScanFileException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "scan-to-proto", defaultPhase = LifecyclePhase.COMPILE)
public class ProtoGenMojo extends AbstractMojo {
    public static final JavaParser JAVA_PARSER = new JavaParser();
    public static final String JAVA_FILE_EXT = ".java";
    /**
     * The Maven Project Object
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter
    protected String excludePackageNames;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<String> packagesToSkip = getPackagesToSkip();
        List<String> sourceRoots = project.getCompileSourceRoots();
        for (String sourceRoot : sourceRoots) {
            File dir = new File(sourceRoot);
            if (!dir.exists()) {
                continue;
            }
            try (Stream<Path> paths = Files.walk(dir.toPath())) {
                JavaFileParser parser = new JavaFileParser(packagesToSkip);
                paths.filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(file -> file.getAbsolutePath().endsWith(JAVA_FILE_EXT))
                        .forEach(parser::parse);
            } catch (IOException e) {
                throw new ScanFileException(e);
            }
        }
    }

    private List<String> getPackagesToSkip() {
        if (excludePackageNames == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(excludePackageNames.split(":"))
                .filter(item -> item != null && !item.trim().isEmpty())
                .collect(Collectors.toList());
    }
}
