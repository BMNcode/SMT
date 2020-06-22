package org.bmn.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PnPConvert {
    private static final String PATTERNForXY= "[xX]{1}(\\d)*[yY]{1}(\\d)*";

    public static Set<String> pnpList(Path path) throws IOException {
        //читаем весь фаил по строкам
        List<String> listForFile = Files.readAllLines(path);
        StringBuilder sbResult = new StringBuilder();
        //создаем новый список и записываем в него совпадения по паттерну PATERNforXY
        List<String> listForPattern = new ArrayList<>();
        Pattern patternX = Pattern.compile(PATTERNForXY);
        //пробегаемся по исходным данным и преобразуем их
        for (String s: listForFile) {
            Matcher matcher = patternX.matcher(s);
            while (matcher.find()) {
                String mathReplace = matcher.group().replace("X", "").replace("Y", " ");
                String[] splitMatch = mathReplace.split(" ");
                for(int i = 0; i < splitMatch.length; i++) {
                    double d = BigDecimal.valueOf((Double.parseDouble(splitMatch[i]))/10000)
                            .setScale(3, RoundingMode.HALF_UP).doubleValue();
                    splitMatch[i] = Util.customFormat("%.3f", d);
                }
                listForPattern.add(sbResult.append(splitMatch[0]).append(";").append(splitMatch[1]).toString());
                sbResult = new StringBuilder();
            }
        }

        Set<String> set = new TreeSet<String>(listForPattern);
        return set;
    }
}
