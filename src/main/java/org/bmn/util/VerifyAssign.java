package org.bmn.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyAssign {
    public static HSSFWorkbook verifyAssign(File assignName, File specName, int indexSheetSpec) {
        HSSFWorkbook assignWB = Util.readWorkbook(assignName.toString());
        HSSFWorkbook specWB = Util.readWorkbook(specName.toString());
        List<String> assignList = new ArrayList<>();
        List<String> specList = new ArrayList<>();
        assert assignWB != null;
        assert specWB != null;
        assignList = Util.getDataAssignXlsInList(assignWB, 0, "assign");
        specList = Util.getDataAssignXlsInList(specWB, indexSheetSpec, "spec");
        return Util.createWB(Util.unionPartDataForVerify(assignList, specList));
    }
}
