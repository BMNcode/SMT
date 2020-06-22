package org.bmn.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OutputResult {

    public static void outputCSV(Set<String> resultList, String nameFile, String path) {
        String csvName = nameFile + ".csv";
        List<String> result = Util.trimAlign(new ArrayList<>(resultList), 10);
        try (FileWriter writer = new FileWriter(path + "\\" + csvName)) {
            for (String s : result) {
                writer.write(s);
                writer.append(System.lineSeparator());
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Path path1 = Paths.get(path + "\\" + csvName);
    }

    public static void writeWorkbook(HSSFWorkbook wb, String fileName, String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path + "\\" + fileName);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            //Обработка ошибки
        }
    }


}

