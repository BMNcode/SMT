package org.bmn.service;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.bmn.model.Component;
import org.bmn.repos.ComponentRepo;
import org.bmn.util.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ComponentService implements ComponentRepo {

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

        //int controlSum = r.length;
        int designator = Util.indexSearch(r, "Designator");
        int midX = Util.indexSearch(r, "MidX");
        int midY = Util.indexSearch(r, "MidY");
        int tb = Util.indexSearch(r, "TB");
        int rotation = Util.indexSearch(r, "Rotation");

        Files.lines(path)
                .forEach(d -> {
                    String[] q = d.split("\\s+");
                    if (d.matches("^\\D{1,4}\\d{1,4}.?.+")) {
                        double xMid = q[midX].contains("mm") ? Double.parseDouble(q[midX].replace("mm", ""))
                                : Double.parseDouble(q[midX]);
                        double yMid = q[midY].contains("mm") ? Double.parseDouble(q[midY].replace("mm", ""))
                                : Double.parseDouble(q[midY]);
                                result.add(new Component((q[designator]), xMid,
                                        yMid, q[tb], Double.parseDouble(q[rotation])));
                    }
                });
        return result;
    }

    @Override
    public List<Component> findAllinXLS(Path path, int numSheet, int numRefColumn,
                                        int numPartColumn, String delimiter) throws IOException {
        //read xls source file
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(path.toFile())));
        //create container for components
        List<Component> components = new ArrayList<>();
        //read workbook numSheet
        HSSFSheet sheet = workbook.getSheetAt(numSheet - 1);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(numRefColumn - 1) == null) {
                break;
            } else {
                //for reference
                //dividing the reference string into separate parts
                List<String> listRef = Arrays.asList(row.getCell(numRefColumn - 1).getStringCellValue().split(delimiter));
                //extraction and verification partNumber for String

                //add components in list
                listRef.stream()
                        .forEach(t -> {
                            //  --------convert masRef "A1-A10" -> A1,A2,A3...
                            if (t.contains("-")) {
                                String[] intervalRef = t.replaceAll(" ", "").split("-");
                                String refChar = getString(intervalRef[0]);
                                CellType cellType = row.getCell(numPartColumn - 1).getCellTypeEnum();
                                String partNumber = "";
                                switch (cellType) {
                                    case STRING:
                                        partNumber = row.getCell(numPartColumn - 1).getStringCellValue();
                                        break;
                                    case NUMERIC:
                                        partNumber = String.valueOf(row.getCell(numPartColumn - 1).getNumericCellValue());
                                        break;
                                }
                                for (int i = getDigit(intervalRef[0]); i <= getDigit(intervalRef[1]); i++) {

                                    components.add(new Component(partNumber, refChar.replaceAll(" ", "") + i));
                                }
                            } else {
                                CellType cellType = row.getCell(numPartColumn - 1).getCellTypeEnum();
                                String partNumber = "";
                                switch (cellType) {
                                    case STRING:
                                        partNumber = row.getCell(numPartColumn - 1).getStringCellValue();
                                        break;
                                    case NUMERIC:
                                        partNumber = String.valueOf(row.getCell(numPartColumn - 1).getNumericCellValue());
                                        break;
                                }
                                components.add(new Component(partNumber, t.replaceAll(" ", "")));
                            }
                        });
            }
        }
        return components;
    }

    public static int getDigit(String s) {
        char[] src = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return Integer.parseInt(sb.toString());
    }

    public static String getString(String s) {
        char[] src = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (!Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

//    public Map<String, String> toCollectPartName(List<Component> componentsList) {
//
//        Map<String, String> result = new HashMap<>();
//        Set<String> nameList = componentsList.stream()
//                .map(Component::getName)
//                .collect(Collectors.toSet());
//
//        String partName = new String();
//        List<String> refListForJoin = new ArrayList<>();
//
//        Iterator iterator = nameList.iterator();
//
//        while (iterator.hasNext()) {
//            partName = (String) iterator.next();
//            for (Component component : componentsList) {
//
//                if (component.equalsName(iterator.next())) {
//                    refListForJoin.add(component.getReference());
//                }
//            }
//            String joined = String.join("," , refListForJoin);
//            result.put(partName.toString(), joined);
//
//        }
//
//        return result;
//    }

//    public static void main(String[] args) throws IOException {
//        File file = new File("E:\\temp\\BOM-SI-LPC-SBC-LCD56_v213d4.xls");
//        List<Component> sdc;
//        ComponentService componentService = new ComponentService();
//        sdc = componentService.findAllinXLS(file.toPath(), 1, 1, 2, ",");
//
//        Map<String, String> map = new HashMap<>();
//        map = componentService.toCollectPartName(sdc);
//
//        for(Map.Entry<String, String> entry: map.entrySet()) {
//            System.out.println(entry.getKey() + " - " + entry.getValue());
//        }
//    }


}
