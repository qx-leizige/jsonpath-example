package qx.leizige;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 转换json格式
 *
 * @author leizige
 */
public class JsonExchange {

    /**
     * json映射转换，返回转换后的json数据
     *
     * @param sourceJsonObj 源json数据
     * @param ruleMap       map的key为目标jsonPath，value为源jsonPath，需要换为实际的映射对象，并传入类型和值集转换相关信息
     * @return jsonObject
     */
    public static JSONObject exchangeJson(JSONObject sourceJsonObj, Map<String, String> ruleMap) {
        ruleMap = convertRules(ruleMap);
        JSONObject newJson = new JSONObject(true);
        Map<String, Object> paths = JSONPath.paths(sourceJsonObj);
        for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
            for (Map.Entry<String, String> ruleEntry : ruleMap.entrySet()) {
                if (pathEntry.getKey().matches(ruleEntry.getValue())) {
                    String newPath = pathEntry.getKey().replaceAll(ruleEntry.getValue(), ruleEntry.getKey());
                    //获取转换后的value。此处进行类型转换和值集映射转换
                    Object newValue = pathEntry.getValue();
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
            }
        }
        return newJson;
    }


    final static String ruleKeyRegex = "\\[(\\d+)\\]";      // \[(\d+)\] 匹配 中括号中任意数字
    final static String ruleKeyReplace = "/\\$$1";           // 获取下标为1的正则表达式组

    final static String ruleValueRegex = "\\[\\]";      //  [] 匹配方括号,替换为正则表达式组 /(\d+)
    final static String ruleValueReplace = "/(\\\\d+)"; // (\d+)正则表达式组,表示任意数字


    final static String target = ".";
    final static String ruleKeyPointReplace = "/";          //路径中的.替换成/
    final static String ruleValuePointReplace = "/\\d*/*";  // 匹配 / 任意数字 / 任意值


    /**
     * 从规则生成转换正则，主要针对目标jsonPath和源jsonPath
     */
    private static Map<String, String> convertRules(Map<String, String> oldRuleMap) {
        Map<String, String> newRuleMap = Maps.newLinkedHashMap();
        oldRuleMap.forEach((oldPath, newPath) -> {
            //针对目标路径进行转换
            String newKey = "/" + oldPath.replaceAll(ruleKeyRegex, ruleKeyReplace).replace(target, ruleKeyPointReplace);
            //针对源路径进行转换
            String newValue = "/" + newPath.replaceAll(ruleValueRegex, ruleValueReplace).replace(target, ruleValuePointReplace);
            newRuleMap.put(newKey, newValue);
        });
        return newRuleMap;
    }
}
