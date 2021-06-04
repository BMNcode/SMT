package org.bmn.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.bmn.model.Component;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static Set<String> spaceAlign(Set<String> src, String delimiter, int countSpace) {
        Set<String> result = new HashSet<>();
        List<String> temp = new ArrayList<>(src);
        int countColumn = temp.get(0).split(delimiter).length;
        String v = Stream.generate(() -> " ")
                .limit(countColumn * countSpace)
                .collect(Collectors.joining());

        StringBuilder sb = new StringBuilder(v);

        src.stream()
                .forEach(s -> {
                    String[] r = s.split(delimiter);
                    sb.delete(0, sb.length());
                    sb.append(v);
                    for (int i = 0; i < r.length; i++) {
                        sb.insert(i * countSpace, r[i]);
                    }
                    result.add(sb.toString().trim());
                });
        return result;
    }


    public static String customFormat(String pattern, double value) {
        return String.format(pattern, value);
    }

    public static HSSFWorkbook readWorkbook(String filename) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            return new HSSFWorkbook(fs);
        } catch (Exception e) {
            return null;
        }
    }

    public static String sortReference(String reference) {
        String[] arr = reference.split(";");
        String[] refer = arr[0].replace(" ", "").split(",");
        Arrays.sort(refer);
        Arrays.sort(refer, Comparator.comparingInt(Util::getDigit));
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
                        i--;
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
        StringBuilder sb = new StringBuilder();
        for (String c : s) {
            sb.append(c).append(";");
        }
        return sb.deleteCharAt(sb.lastIndexOf(";")).toString();
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

    private static HSSFCellStyle createGreenStyle(HSSFWorkbook workbook) {
        // Font
        HSSFFont font = workbook.createFont();
        //font.setBold(true);
        //font.setItalic(true);
        // Font Height
        //font.setFontHeightInPoints((short) 18);
        // Font Color
        font.setColor(IndexedColors.GREEN.index);
        // Style
        short black = IndexedColors.BLACK.getIndex();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);
        style.setFont(font);

        return style;
    }

    private static HSSFCellStyle createBlackStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);

        short black = IndexedColors.BLACK.getIndex();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);

        return style;
    }

    public static List<String> getDataAssignXlsInList(HSSFWorkbook wb, int indexSheet, String typeWB) {
        int length = 0;
        List<String> xlsList = new ArrayList<>();
        HSSFSheet sheet = wb.getSheetAt(indexSheet);
        Iterator<Row> rowIter = sheet.iterator();
        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            if (row.getCell(0) == null) {
                break;
            } else {
                if (typeWB.equals("assign")) {
                    length = 3;
                } else if (typeWB.equals("spec")) {
                    length = row.getLastCellNum();
                }
                String[] xlsMasStr = new String[length];
                for (int i = 0; i < length; i++) {
                    if (row.getCell(i) == null) {
                        break;
                    }
                    CellType cellType = row.getCell(i).getCellTypeEnum();
                    switch (cellType) {
                        case STRING:
                            xlsMasStr[i] = row.getCell(i).getStringCellValue();
                            break;
                        case NUMERIC:
                            xlsMasStr[i] = String.valueOf(row.getCell(i).getNumericCellValue());
                            break;
                        case BLANK:
                            xlsMasStr[i] = "";
                    }
                }
                xlsList.add(Util.pattern(xlsMasStr));
            }
        }
        return xlsList;
    }

    public static String unionPartDataAndAssign(String assign, String spec, String delimiter) {
        return assign + delimiter + spec;
    }

    public static List<String> unionPartDataForVerify(List<String> assign, List<String> spec) {
        StringBuilder sb = new StringBuilder();
        List<String> resultVerify = new ArrayList<>();
        boolean flag = false;
        for (String sp : spec) {
            for (String as : assign) {
                String[] asMas = as.split(";");
                if (sp.contains(asMas[0])) {
                    sb.append("1;").append(sp).append(";<----------->;").append(as);
                    resultVerify.add(sb.toString());
                    sb = new StringBuilder();
                    flag = true;
                    break;
                }
            }
            if (!flag) resultVerify.add(sp);
        }
        return resultVerify;
    }

    public static List<String> unionPartDataForVerifyComponents(List<Component> assign, List<Component> spec) {
        List<String> result = new ArrayList<>();
        StringBuilder pattern = new StringBuilder();
        for (Component sp : spec) {
            for (Component asgn : assign) {
                if (sp.equals(asgn)) {
                    pattern.append(sp.getReference()).append("      ").append(sp.getName()).append("  <----------->  ")
                            .append(asgn.getReference()).append("      ").append(asgn.getName());
                }
            }
            if (pattern.toString().isEmpty()) {
                pattern = new StringBuilder();
                pattern.append(sp.getReference()).append("      ").append(sp.getName()).append("  <<<не найден");
            }
            result.add(pattern.toString());
            pattern = new StringBuilder();
        }
        return result;
    }

//    public static List<String> unionPartDataForVerifyComponents(Map<String, List<Component>> specSrc, Map<String,
//                                                                List<Component>> assignSrc) {
//
//
//    }

    public static HSSFWorkbook createWB(List<String> list) {
        HSSFWorkbook resultBook = new HSSFWorkbook();
        HSSFSheet resultSheet = resultBook.createSheet();
        HSSFCellStyle styleSimple = Util.createCellStyle(resultBook);
        HSSFCellStyle styleGreen = Util.createGreenStyle(resultBook);
        Cell resultCell;
        Row resultRow;
        for (int i = 0; i < list.size(); i++) {
            resultRow = resultSheet.createRow(i);
            String[] rowSplit = list.get(i).split(";");
            if (rowSplit[0].equals("1")) {
                for (int j = 1; j < rowSplit.length; j++) {
                    {
                        resultCell = resultRow.createCell(j, CellType.STRING);
                        resultCell.setCellValue(rowSplit[j]);
                        resultCell.setCellStyle(styleGreen);
                    }
                }
            } else {
                for (int j = 0; j < rowSplit.length; j++) {
                    {
                        resultCell = resultRow.createCell(j, CellType.STRING);
                        resultCell.setCellValue(rowSplit[j]);
                        resultCell.setCellStyle(styleSimple);
                    }
                }
            }
        }
        return resultBook;
    }

    public static HSSFWorkbook createWBWithoutBorder(List<String> list) {

        HSSFWorkbook resultBook = new HSSFWorkbook();
        HSSFSheet resultSheet = resultBook.createSheet();
        HSSFCellStyle styleBlack = Util.createBlackStyle(resultBook);
        Cell resultCell;
        Row resultRow;
        for (int i = 0; i < list.size(); i++) {
            resultRow = resultSheet.createRow(i);
            String[] rowSplit = list.get(i).split(";");

            for (int j = 0; j < rowSplit.length; j++) {
                {
                    resultCell = resultRow.createCell(j, CellType.STRING);
                    resultCell.setCellValue(rowSplit[j]);
                    resultCell.setCellStyle(styleBlack);
                }
            }

        }
        return resultBook;
    }

//    public static HSSFWorkbook createWB()

    /*
    находит референсы в строке
     */
    public static String getReference(String str, String pattern, String delimiter) {
        String result = "";
        String[] splitStr = str.split(delimiter);
        Pattern ptrn = Pattern.compile(pattern);
        for (String s : splitStr) {
            Matcher matcher = ptrn.matcher(s);
            if (matcher.find()) {
                result = s;
            }
        }
        return result;
    }

    /*
    разбивает, а после перечисляет референсы+
    */
    public static List<String> parseForReference(String str, String delimiter) {
        List<String> result = new ArrayList<>();
        String[] parse = str.split(delimiter);
        for (String s : parse) {
            if (s.contains("-")) {
                String[] refDel = s.split("-");
                String refChar = getString(refDel[0]);
                for (int i = getDigit(refDel[0]); i <= getDigit(refDel[1]); i++) {
                    result.add(refChar.replaceAll(" ", "") + i);
                }
            } else {
                result.add(s.replaceAll(" ", ""));
            }
        }
        return result;
    }

    //метод для преобразования Double значения в формат #.###
    public static String formatDouble(Double d) {
        return String.format("%.3f", BigDecimal.valueOf(d / 10000)
                .setScale(3, RoundingMode.HALF_UP).doubleValue());
    }

    public static int indexSearch(String[] src, String strIndexSearch) {
        for (int i = 0; i < src.length; i++) {
            if (src[i].equals(strIndexSearch))
                return i;
        }
        return -1;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public static List<String> transXAndYLocation(List<String> src, double x, double y) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            String[] tempArray = src.get(i).split(";");
            tempArray[2] = String.valueOf(Double.parseDouble(tempArray[2]) - x);
            tempArray[3] = String.valueOf(Double.parseDouble(tempArray[3]) - y);
            result.add(String.join(";", tempArray));
        }

        return result;
    }


}
