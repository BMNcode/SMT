package org.bmn.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Util {
    //выравнивает столбцы пробелами
    public static List<String> trimAlign(List<String> list, int space) {
        List<String> listAlign = new ArrayList<>();
        StringBuilder sbSpace = new StringBuilder();
        int maxPosition = 0;
        int tempCount = 0;
        int trimDelta = 0;

        //находим самое длинное слово в столбце
        for (int i = 0; i < list.size(); i++) {
            String[] s = list.get(i).split(";");
            tempCount = s[0].length();
            if (maxPosition < tempCount) {
                maxPosition = tempCount;
            }
        }

        //в соответствии с разницей каждого слова в 1м столбце с самым длинным словом добавляем пробелов ко второму столбцу
        for (String q : list) {
            for (int i = 0; i < 1; i++) {
                String[] s = q.split(";");
                trimDelta = s[0].length();
            }
            for (int i = 0; i < (maxPosition - trimDelta) + space; i++) {
                sbSpace.append(" ");
            }
            listAlign.add(q.replace(";", sbSpace.toString()));
            sbSpace = new StringBuilder();
        }
        return listAlign;
    }

    public static String customFormat(String pattern, double value) {
        return String.format(pattern, value);
    }

    public static HSSFWorkbook readWorkbook(String filename) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            return wb;
        } catch (Exception e) {
            return null;
        }
    }

    public static String sortReference(String reference) {
        String[] arr = reference.split(";");
        String[] refer = arr[0].replace(" ", "").split(",");
        Arrays.sort(refer);
        Arrays.sort(refer, Comparator.comparingInt(Util::isDigit));
        StringBuilder result = new StringBuilder();
        for (String s : refer) {
            result.append(s).append(",");
        }
        return result.deleteCharAt(result.lastIndexOf(",")).toString();
    }

    public static List<String> replaceReference(List<String> list) {
        List<String> result = new ArrayList<>();
        List<String> parseBigReference = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            String[] arr = s.split(";");
            if (arr[0].length() > 60) {
                String[] ref = arr[0].split(",");
                for (int i = 0; i < ref.length; i++) {
                    if (sb.length() <= 55) {
                        sb.append(ref[i]).append(",");
                    } else {
                        parseBigReference.add(sb.deleteCharAt(sb.lastIndexOf(",")).toString());
                        sb = new StringBuilder();
                    }
                }
                if (sb.length() != 0) {
                    parseBigReference.add(sb.deleteCharAt(sb.lastIndexOf(",")).toString());
                    sb = new StringBuilder();
                }
                result.add((pattern("", "", "")));
                result.add(pattern(parseBigReference.get(0), arr[1], arr[2]));
                for (int i = 1; i < parseBigReference.size(); i++) {
                    result.add(pattern(parseBigReference.get(i), "", ""));
                }
                result.add((pattern("", "", "")));
                parseBigReference.clear();
            } else {

                result.add(pattern(arr[0], arr[1], arr[2]));
            }
        }
        return result;
    }

    public static String pattern(String... s) {
        String pattern = "%s;%s;%s";
        return String.format(pattern, s[0], s[1], s[2]);
    }

    public static String isString(String s) {
        char[] src = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (!Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static int isDigit(String s) {
        char[] src = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return Integer.parseInt(sb.toString());
    }

    public static HSSFCellStyle createCellStyle(HSSFWorkbook book) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        HSSFCellStyle style = book.createCellStyle();



        style.setBorderTop(thin);
        style.setBorderBottom(thin);
        style.setBorderRight(thin);
        style.setBorderLeft(thin);

        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);

        return style;
    }


}
