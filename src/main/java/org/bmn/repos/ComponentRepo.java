package org.bmn.repos;

import org.bmn.model.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ComponentRepo {

    List<Component> findAllinGerber(Path path, String pattern) throws IOException;
    List<Component> findAllinPCAD(Path path) throws IOException;
    List<Component> findAllinAD(Path path) throws IOException;
    List<Component> findAllinXLS(Path path, int numSheet, int numRefColumn,
                                 int numPartColumn, String delimiter) throws IOException;
}
