package io.github.codingspeedup.execdoc.toolbox.documents.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Optional;

/**
 * http://javaparser.org/
 */
@NoArgsConstructor
public class JavaDocument extends TextFileWrapper {

    public static final PrinterConfiguration DEFAULT_PRINTER_CONFIGURATION = new DefaultPrinterConfiguration();

    private static final ParserConfiguration CONFIGURATION = new ParserConfiguration();
    private static final JavaParser PARSER = new JavaParser(CONFIGURATION);

    @Getter
    @Setter
    private PrinterConfiguration printerConfiguration = DEFAULT_PRINTER_CONFIGURATION;
    private CompilationUnit compilationUnit;

    public JavaDocument(File file) {
        super(file);
    }

    @SneakyThrows
    @Override
    protected void loadFromWrappedFile() {
        compilationUnit = PARSER.parse(getFile()).getResult().orElse(new CompilationUnit());
    }

    public CompilationUnit getCompilationUnit() {
        if (compilationUnit == null) {
            compilationUnit = new CompilationUnit();
        }
        return compilationUnit;
    }

    @Override
    public String toString() {
        if (compilationUnit == null) {
            return null;
        }
        return compilationUnit.toString(printerConfiguration);
    }

    public String getPackageName() {
        Optional<PackageDeclaration> oPD = getCompilationUnit().getPackageDeclaration();
        if (oPD.isPresent()) {
            return oPD.get().getNameAsString();
        }
        return "";
    }

    public TypeDeclaration<?> getTypeDeclaration(String[] namePath) {
        for (TypeDeclaration<?> foo : getCompilationUnit().getTypes()) {
            if (foo.getNameAsString().equals(namePath[0])) {
                return foo;
            }
        }
        return null;
    }

}
