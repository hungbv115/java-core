package com.learn.hungbv.example.impl;

import com.learn.hungbv.annotation.Schedule;
import com.learn.hungbv.annotation.Singleton;
import com.learn.hungbv.example.MyInterface;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class MyInterfaceImpl implements MyInterface {
    Map<String, List<String>> conditionSplit = new HashMap<>();

    @Schedule(time = 1)
    @Override
    public void performAction() {
        String condition = "(1,6,CH,EQ,C'SRINIV',OR,7,6,ZD,LT,1000)";
        conditionSplit = new HashMap<>();
        List<String> keys = new ArrayList<>();
        findByPattern("[0-9]+,[0-9]+", condition, keys);
        conditionSplit.put("INCLUDE", keys);
        keys = new ArrayList<>();
        findByPattern("(EQ|NE|GT|GE|LT|LE),([C|X]'.*'|[0-9]+)(,AND,|,OR,)?", condition, keys);
        conditionSplit.put("INCLUDE-OPERA", keys);
        System.out.println(conditionSplit);
        subFunc();
    }

    private static void findByPattern(String regex, String condition, List<String> keys) {
        Pattern conPattern = Pattern.compile(regex);
        Matcher conMatcher = conPattern.matcher(condition);
        while (conMatcher.find()) {
            keys.add(conMatcher.group());
        }
    }

    private void subFunc() {
        List<List<String>> fieldss = List.of(List.of("1-3", "2-3"), List.of("3-4"));
        List<String> keys = conditionSplit.get("INCLUDE");
        StringBuilder stringFinal = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            List<String> operator = conditionSplit.get("INCLUDE-OPERA");
            String opera = operator.get(i);
            String[] tempArr = new String[3];
            String tempFormat = "";
            Pattern conPattern = Pattern.compile("(EQ|NE|GT|GE|LT|LE),([C|X]'.*'|[0-9]+)(,(AND|OR),)?");
            Matcher conMatcher = conPattern.matcher(opera);
            while (conMatcher.find()) {
                tempArr[0] = conMatcher.group(1);
                tempArr[1] = conMatcher.group(2);
                tempArr[2] = conMatcher.group(4) != null ? conMatcher.group(4) : "";
                tempFormat = conMatcher.group(2).replaceAll("[C|X]'|'$", "");
            }

            int flag = 0;
            List<String> fields = fieldss.get(i);
            StringBuilder result = new StringBuilder();
            for (String element : fields) {
                int start = flag + Integer.parseInt(element.split("-")[1]);
                start = Math.min(start, tempFormat.length());
                String content = tempArr[1].replace(tempFormat, tempFormat.substring(flag, start));
                if(flag > 0) {
                    result.append(",AND,");
                }
                result.append(element.split("-")[0]).append(",0,0,")
                        .append(element.split("-")[1]).append(",Str,")
                        .append(tempArr[0]).append(",").append(content);
                flag = start;
            }
            if(fields.size() > 1) {
                result.insert(0,'(');
                result.insert(result.length(),')');
            }
            stringFinal.append(opera.replace(tempArr[0]+","+tempArr[1], result));

        }
        System.out.println(stringFinal);
    }

//    @Schedule(fixedRate = 2000)
//    private void methodRate() {
//        System.out.println("Đang chạy ...");
//    }

}
