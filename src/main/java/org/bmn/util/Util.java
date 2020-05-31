package org.bmn.util;

import java.util.ArrayList;
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
        for(int i = 0; i < list.size(); i++) {
            String[] s = list.get(i).split(";");
            tempCount = s[0].length();
            if (maxPosition < tempCount) {
                maxPosition = tempCount;
            }
        }

        //в соответствии с разницей каждого слова в 1м столбце с самым длинным словом добавляем пробелов ко второму столбцу
        for (String q: list) {
            for(int i = 0; i < 1; i++) {
                String[] s = q.split(";");
                trimDelta = s[0].length();
            }
            for(int i = 0; i < (maxPosition - trimDelta) + space; i++) {
                sbSpace.append(" ");
            }
            listAlign.add(q.replace(";", sbSpace.toString()));
            sbSpace = new StringBuilder();
        }
        return listAlign;
    }
}
