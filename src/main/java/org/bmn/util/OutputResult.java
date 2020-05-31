package org.bmn.util;

import java.io.FileWriter;
import java.util.List;

public class OutputResult{

    public static void outputCSV(List<String> resultList, String nameFile, String path) {
        String csvName = nameFile + ".csv";
        List<String> result = Util.trimAlign(resultList, 5);
        try(FileWriter writer = new FileWriter(path + "\\" + csvName)){
            for(String s: result) {
                writer.write(s);
                writer.append('\n');
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

