package org.bmn.service;

import org.bmn.model.Component;
import org.bmn.repos.ComponentRepo;
import org.bmn.util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ComponentService implements ComponentRepo {
    @Override
    public Component getComponent(String src) {
        return null;
    }

    @Override
    public List<Component> findAllinGerber(Path path, String pattern) throws IOException {
        List<Component> result = new ArrayList<>();
        Files.lines(path)
                .filter(s -> s.matches(pattern))
                .map(s -> s.replace("X", ""))
                .map(s -> s.replace("Y", " "))
                .forEach(s -> {
                    String[] a = s.split(" ");
                    result.add(new Component(Double.parseDouble(a[0]), Double.parseDouble(a[1])));
                });
        return result;
    }

    @Override
    public List<Component> findAllinPCAD(Path path) throws IOException {
        List<Component> result = new ArrayList<>();

        String[] r = Files.lines(path)
                .filter(s -> s.contains("RefDes"))
                .flatMap(s -> Stream.of(s.split("\\s+")))
                .toArray(String[]::new);
        int controlSum = r.length;
        int refDes = Util.indexSearch(r, "RefDes");
        int locationX = Util.indexSearch(r, "LocationX");
        int locationY = Util.indexSearch(r, "LocationY");
        int layer = Util.indexSearch(r, "Layer");
        int rotation = Util.indexSearch(r, "Rotation");

        Files.lines(path)
                .forEach(d -> {
                    String[] q = d.split("\\s+");
                    if (q.length == controlSum && d.matches("^\\D{1,5}\\d{1,5}.?.+"))
                        result.add(new Component(q[refDes], Double.parseDouble(q[locationX]),
                                Double.parseDouble(q[locationY]), q[layer], Double.parseDouble(q[rotation])));
                });
        return result;
    }

    @Override
    public List<Component> findAllinAD(Path path) throws IOException {
        List<Component> result = new ArrayList<>();
        String[] r = Files.lines(path)
                .filter(s -> s.contains("Designator"))
                .map(s -> s.replace("Mid X", "MidX"))
                .map(s -> s.replace("Mid Y", "MidY"))
                .map(s -> s.replace("Ref X", "RefX"))
                .map(s -> s.replace("Ref Y", "RefY"))
                .map(s -> s.replace("Pad X", "PadX"))
                .map(s -> s.replace("Pad Y", "PadY"))
                .flatMap(s -> Stream.of(s.split("\\s+")))
                .toArray(String[]::new);

        int controlSum = r.length;
        int designator = Util.indexSearch(r, "Designator");
        int midX = Util.indexSearch(r, "MidX");
        int midY = Util.indexSearch(r, "MidY");
        int tb = Util.indexSearch(r, "TB");
        int rotation = Util.indexSearch(r, "Rotation");

        Files.lines(path)
                .forEach(d -> {
                    String[] q = d.split("\\s+");
                    if (q.length == controlSum && d.matches("^\\D{1,5}\\d{1,5}.?.+"))
                        result.add(new Component((q[designator]), Double.parseDouble(q[midX].replace("mm", "")),
                                Double.parseDouble(q[midY].replace("mm", "")), q[tb], Double.parseDouble(q[rotation])));
                });
        return result;
    }
}
