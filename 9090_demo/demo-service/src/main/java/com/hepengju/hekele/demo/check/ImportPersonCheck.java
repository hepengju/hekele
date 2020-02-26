package com.hepengju.hekele.demo.check;

import com.hepengju.hekele.base.core.excel.ExcelSheetCheck;
import com.hepengju.hekele.base.core.exception.ExcelCheckException;

import java.util.*;

/**
 * 导入Excel文件检查的示例
 *
 * @author he_pe
 */
public class ImportPersonCheck implements ExcelSheetCheck {

    private final String VALUE = "VALUE";
    private final List<String> genderEnumList = Arrays.asList("M", "F");
    //private final DecimalFormat decimalFormat = new DecimalFormat("0");

    @Override
    public List<String> checkResult(String sheetName, Integer columnCount, Integer dataBeginRow, Map<String, Integer> titleIndexMap, List<List<Object>> dataList) throws ExcelCheckException {
        List<String> errList = new ArrayList<>();
        Map<String,String> phoneMap = new HashMap<>(dataList.size());

        for (int i = 0; i < dataList.size(); i++) {
            int curRow = i + dataBeginRow;
            List<Object> rowList = dataList.get(i);

            Object userName         = rowList.get(titleIndexMap.get("姓名"));
            Object gender           = rowList.get(titleIndexMap.get("性别"));
            Object phone            = rowList.get(titleIndexMap.get("手机号"));
            Object birth            = rowList.get(titleIndexMap.get("出生日期"));
            Object familyPopulation = rowList.get(titleIndexMap.get("家庭人口"));
            Object salaryYear       = rowList.get(titleIndexMap.get("年收入"));
            Object managerNo        = rowList.get(titleIndexMap.get("经理编号"));
            Object orgNo            = rowList.get(titleIndexMap.get("机构编号"));
            //Object familyAddr       = rowList.get(titleIndexMap.get("家庭地址"));

            // 非空判断
            if (userName  == null) errList.add("姓名不能为空，错误行：" + curRow);
            if (phone     == null) errList.add("手机号不能为空，错误行：" + curRow);
            if (birth     == null) errList.add("出生日期不能为空，错误行：" + curRow);
            if (managerNo == null) errList.add("经理编号不能为空，错误行：" + curRow);
            if (orgNo     == null) errList.add("机构编号不能为空，错误行：" + curRow);

            // 枚举值判断
            if(gender != null && !genderEnumList.contains(gender.toString())){
                errList.add("性别只能填入M或F，错误行：" + curRow);
            }

            // 重复判断
            if(phone != null){
                if (phoneMap.get(phone) != null) errList.add("手机号码不能重复，错误行：" + curRow);
                phoneMap.put(phone.toString(), VALUE);
            }


            // 数字校验
            try {
                if (familyPopulation != null && !(familyPopulation instanceof Double)) Double.parseDouble(String.valueOf(familyPopulation));
            } catch (Exception e) {
                errList.add("家庭人口必须为整数, 错误行: " + curRow);
            }

            // 可以为空的Double校验
            try{
                if(salaryYear != null && !(salaryYear instanceof Double)) Double.parseDouble(String.valueOf(salaryYear));
            }catch (Exception e){
                errList.add("年薪必须为数字, 错误行: " + curRow);
            }
        }

        return errList;
    }
}
