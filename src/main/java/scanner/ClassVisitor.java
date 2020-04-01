package scanner;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;

/**
 * The class is responsible for scanning Java class file
 * parts and converting them to Proto format.
 * <p>
 * TODO: Implement
 */
public class ClassVisitor extends VoidVisitorAdapter<File> {
    @Override
    public void visit(PackageDeclaration n, File arg) {

    }
}
