package qx.leizige;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.lang3.StringUtils;
import qx.leizige.module.JsonConvertTemplate;
import qx.leizige.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author leizige
 * json格式+类型转换
 */
public class JsonFormatConverter {

    /**
     * json映射转换，返回转换后的json数据
     *
     * @param sourceJsonObj 源json数据
     * @param templateList  需要换为实际的映射对象，并传入类型和值集转换相关信息
     * @return jsonObject
     */
    public static JSONObject exchangeJson(JSONObject sourceJsonObj, List<JsonConvertTemplate> templateList) {
        convertPathToRegexPath(templateList);
        JSONObject newJson = new JSONObject(true);
        Map<String, Object> paths = JSONPath.paths(sourceJsonObj);
        for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
            templateList.forEach(template -> {
                if (pathEntry.getKey().matches(template.getOldPath())) {        //oldPath   /orderLineList/(\d+)/\d*/*skuCode
                   // key = /oderLine/1/skuCode   oldPath = /orderLine/(\d+)/\d*/skuCode  newPath = /sku_code_list/$1/sku_code
                    String newPath = pathEntry.getKey().replaceAll(template.getOldPath(), template.getNewPath());
                    //获取转换后的value
                    Object newValue = convertTo(template.getTargetType(), template.getValueExpression(), pathEntry.getValue());
                    if (JSONPath.contains(newJson, newPath) && JSONPath.size(newJson, newPath) < 0) {
                        //path下已经存在一个值，则需要将类型转换为jsonArray并添加第二个值
                        Object tmp = JSONPath.eval(newJson, newPath);
                        Object[] tArray = {tmp, newValue};
                        JSONPath.set(newJson, newPath, tArray);
                    } else if (JSONPath.size(newJson, newPath) > 1) {
                        //path下已经存在数组对象，直接追加
                        JSONPath.arrayAdd(newJson, newPath, newValue);
                    } else {
                        //path不存在，直接添加对象
                        JSONPath.set(newJson, newPath, newValue);
                    }
                }
            });
        }
        return newJson;
    }


    private final static String OLD_PATH_REGEX = "\\[\\]";      //  [] 匹配方括号,替换为正则表达式组 /(\d+)
    private final static String OLD_PATH_REPLACEMENT = "/(\\\\d+)"; // (\d+)正则表达式组,表示任意数字
    private final static String OLD_PATH_REPLACE = "/\\d*/*";  // 匹配 / 任意数字 / 任意值

    private final static String NEW_PATH_REGEX = "\\[(\\d+)\\]";      // \[(\d+)\] 匹配 中括号中任意数字
    private final static String NEW_PATH_REPLACEMENT = "/\\$$1";           // 获取下标为1的正则表达式组

    private final static String DOT = ".";
    private final static String SLASH = "/";        //路径中的.替换成/

    /**
     * 从规则生成转换正则，主要针对目标jsonPath和源jsonPath
     */
    private static void convertPathToRegexPath(List<JsonConvertTemplate> templateList) {
        templateList.forEach(template -> {
            //针对源路径进行转换     /orderList[].skuName
            String convertOldPath = "/" + template.getOldPath().replaceAll(OLD_PATH_REGEX, OLD_PATH_REPLACEMENT).replace(DOT, OLD_PATH_REPLACE);
            template.setOldPath(convertOldPath);
            //针对目标路径进行转换    /order_list[1].sku_name
            String convertNewPath = "/" + template.getNewPath().replaceAll(NEW_PATH_REGEX, NEW_PATH_REPLACEMENT).replace(DOT, SLASH);
            template.setNewPath(convertNewPath);
        });
    }

    /**
     * 类型转换器
     *
     * @param targetTypeName 要转成的类型
     * @param val            值
     * @return 转换类型后的Object
     */
    private static Object convertTo(String targetTypeName, String valueExpression, Object val) {

        if (StringUtils.isBlank(targetTypeName)) {
            return val;
        }

        if (StringUtils.isNotBlank(valueExpression)) {
            val = evaluate(val, valueExpression);
        }

        Object newVal;
        switch (targetTypeName) {
            /**需要转换的基本类型**/
            case "short":
            case "Short":
                newVal = Short.parseShort(val.toString().trim());
                break;
            case "int":
            case "Integer":
                newVal = Integer.parseInt(val.toString().trim());
                break;
            case "long":
            case "Long":
                newVal = Long.parseLong(val.toString().trim());
                break;
            case "float":
            case "Float":
                newVal = Float.parseFloat(val.toString().trim());
                break;
            case "double":
            case "Double":
                newVal = Double.parseDouble(val.toString().trim());
                break;
            case "boolean":
            case "Boolean":
                newVal = Boolean.parseBoolean(val.toString().trim());
                break;
            case "BigDecimal":
                newVal = new BigDecimal(val.toString().trim());
                break;
            case "BigInteger":
                newVal = new BigInteger(val.toString().trim());
                break;
            /**日期时间**/
            case "LocalDate":
                newVal = TimeUtils.toLocalDate(val.toString().trim());
                break;
            case "LocalTime":
                newVal = LocalTime.parse(val.toString().trim());
                break;
            case "LocalDateTime":
                newVal = TimeUtils.toLocalDateTime(val.toString().trim());
                break;
            default:
                newVal = val;
                break;
        }
        return newVal;
    }


    /**
     * 执行groovy表达式
     *
     * @param val        值
     * @param scriptText expression
     * @return 计算后的值
     */
    private static Object evaluate(Object val, String scriptText) {
        Binding binding = new Binding();
        binding.setVariable("value", val.toString());
        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(scriptText);
        return binding.getVariable("value");
    }

}
