package mojo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.Name;
import exception.ParseFileException;
import scanner.ClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class JavaFileParser {
    private List<String> packagesToSkip;

    public JavaFileParser(List<String> packagesToSkip) {
        this.packagesToSkip = packagesToSkip;
    }

    public void parse(File file) {
        try (InputStream is = new FileInputStream(file)) {
            JavaParser javaParser = new JavaParser();
            ParseResult<CompilationUnit> result = javaParser.parse(is);
            if (!result.isSuccessful()) {
                System.err.println("Failed to parse file: " + file.getAbsolutePath());
                return;
            }
            result.ifSuccessful((CompilationUnit cu) -> {
                Optional<PackageDeclaration> packageDeclaration = cu.getPackageDeclaration();
                if (packageDeclaration.isPresent()) {
                    Name packageName = packageDeclaration.get().getName();
                    for (String packageToSkip : packagesToSkip) {
                        String diff = packageName.toString().replace(packageToSkip, "");
                        if (diff.isEmpty() || diff.startsWith(".")) {
                            return;
                        }
                    }
                    new ClassVisitor().visit(cu, file);
                }
            });
        } catch (IOException e) {
            throw new ParseFileException(e);
        }
    }
}
