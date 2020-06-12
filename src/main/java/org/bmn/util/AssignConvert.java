package org.bmn.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssignConvert {
    public static HSSFWorkbook assignConvert(File files) {
        HSSFWorkbook wb = Util.readWorkbook(files.toString());
        HSSFWorkbook resultBook = new HSSFWorkbook();
        HSSFSheet resultSheet = resultBook.createSheet();
        HSSFCellStyle style = Util.createCellStyle(resultBook);
        Cell resultCell;
        Row resultRow;
        List<String> xlsList = new ArrayList<>();
        assert wb != null;
        //Обрабатываем входной xls фаил
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIter = sheet.iterator();
        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            if (row.getCell(0) == null) {
                break;
            } else {
                String[] xlsMasStr = new String[3];
                for (int i = 0; i < 3; i++) {
                    CellType cellType = row.getCell(i).getCellTypeEnum();
                    switch (cellType) {
                        case STRING:
                            xlsMasStr[i] = row.getCell(i).getStringCellValue();
                            break;
                        case NUMERIC:
                            xlsMasStr[i] = String.valueOf(row.getCell(i).getNumericCellValue());
                            break;
                    }
                }
                xlsList.add(Util.pattern(Util.sortReference((xlsMasStr[0])), xlsMasStr[1], xlsMasStr[2]));
            }
        }
        //формирование нового xls
        List<String> res = Util.replaceReference(xlsList);
        for(int i = 0; i < res.size(); i++) {
            resultRow = resultSheet.createRow(i);
            String[] rowSplit = res.get(i).split(";");
            for(int j = 0; j < rowSplit.length; j++) {
                resultCell = resultRow.createCell(j, CellType.STRING);
                resultCell.setCellValue(rowSplit[j]);
                resultCell.setCellStyle(style);
            }
        }
        return resultBook;
    }


}
