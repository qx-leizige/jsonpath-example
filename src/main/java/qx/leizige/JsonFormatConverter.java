package qx.leizige;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import qx.leizige.module.JsonConvertTemplate;
import qx.leizige.utils.TimeUtils;

/**
 * @author leizige
 * json格式转换+字段类型转换+填充默认值
 */
@Slf4j
public class JsonFormatConverter {


	public static JSON exchangeJson(String sourceJson, List<JsonConvertTemplate> templateList) {
		Object sourceJsonObj = JSON.parse(sourceJson);
		if (sourceJsonObj instanceof JSONObject) {
			return exchangeJson((JSONObject) sourceJsonObj, templateList);
		}
		else if (sourceJsonObj instanceof JSONArray) {
			JSONArray jsonArray = new JSONArray();
			JSONArray sourceJsonArrayObj = (JSONArray) sourceJsonObj;
			sourceJsonArrayObj.forEach(obj -> jsonArray.add(exchangeJson((JSONObject) obj, templateList)));
			return jsonArray;
		}
		return new JSONObject();
	}

	/**
	 * json映射转换，返回转换后的json数据
	 *
	 * @param sourceJsonObj 源json数据
	 * @param templateList  需要换为实际的映射对象，并传入类型和值集转换相关信息
	 * @return jsonObject
	 */
	public static JSONObject exchangeJson(JSONObject sourceJsonObj, List<JsonConvertTemplate> templateList) {
		if (templateList.isEmpty()) {
			log.warn("jsonConvertTemplate empty !");
			return sourceJsonObj;
		}
		JSONObject newJson = new JSONObject(true);
		convertPathToRegexPath(templateList);
		Map<String, Object> paths = JSONPath.paths(sourceJsonObj);
		for (Map.Entry<String, Object> pathEntry : paths.entrySet()) {
			templateList.stream().filter(v -> StringUtils.isNotEmpty(v.getOldPath())).forEach(template -> {
				if (pathEntry.getKey()
						.matches(template.getOldPath())) {        //oldPath   /orderLineList/(\d+)/\d*/*skuCode
					// key = /orderLine/1/skuCode   oldPath = /orderLine/(\d+)/\d*/skuCode  newPath = /sku_code_list/$1/sku_code
					String newPath = pathEntry.getKey().replaceAll(template.getOldPath(), template.getNewPath());
					//获取转换后的value
					Object newValue = convertTo(template.getTargetType(), template.getValueExpression(), pathEntry.getValue());
					appendJsonPath(newJson, newPath, newValue);
				}
			});
		}
		//处理没有源路径的情况
		templateList.stream().filter(v -> StringUtils.isBlank(v.getOldPath())).forEach(template -> {
			String newPath = template.getNewPath();
			if (newPath.matches("(.*)\\[\\](.*)") || newPath.matches("(.*)\\[\\d+\\](.*)")) {        //如果是数组
				String fieldName = newPath.substring(newPath.lastIndexOf(".") + 1);
				List<String> fillDefaultValuePath = getFillDefaultValuePath(newJson, newPath);
				fillDefaultValuePath.forEach(path -> {
					String defaultValue = template.getDefaultValue();
					appendJsonPath(newJson, path + "/" + fieldName, StringUtils.isNotEmpty(defaultValue) ? defaultValue : "");
				});
			}
			else {
				String defaultValue = template.getDefaultValue();
				appendJsonPath(newJson, newPath, StringUtils.isNotEmpty(defaultValue) ? defaultValue : "");
			}
		});
		return newJson;
	}


	private static List<String> getFillDefaultValuePath(JSONObject newJson, String newPath) {
		String path = "/" + newPath.substring(0, newPath.lastIndexOf(".")).replaceAll("\\[\\w+\\]", "[]");
		String[] splitPaths = path.split("\\[\\]");

		List<String> tempPrefixList = new ArrayList<>();
		List<String> pathList = new ArrayList<>();
		for (int i = 0; i < splitPaths.length; i++) {
			if (i == 0) {
				String p = splitPaths[i].replace(DOT, SLASH);
				int size = JSONPath.size(newJson, p);
				if (size > 0) {
					for (int o = 0; o < size; o++) {
						String np = p + "/" + o;
						tempPrefixList.add(np);
					}
				}
				pathList.addAll(tempPrefixList);
			}
			else {
				if (!pathList.isEmpty()) pathList.clear();
				for (String prefix : tempPrefixList) {
					String p = prefix + splitPaths[i].replace(DOT, SLASH);
					int size = JSONPath.size(newJson, p);
					if (size > 0) {
						for (int o = 0; o < size; o++) {
							String np = p + "/" + o;
							pathList.add(np);
						}
					}
				}
				tempPrefixList.addAll(pathList);
				if (i != splitPaths.length - 1) pathList.clear();	//最后一次循环之前清理
			}
		}
		return pathList;
	}

	/**
	 * newJson 追加path和value
	 * @param newJson newJson
	 * @param newPath newPath
	 * @param newValue newValue
	 */
	private static void appendJsonPath(JSONObject newJson, String newPath, Object newValue) {
		if (JSONPath.contains(newJson, newPath) && JSONPath.size(newJson, newPath) < 0) {
			//path下已经存在一个值，则需要将类型转换为jsonArray并添加第二个值
			Object tmp = JSONPath.eval(newJson, newPath);
			Object[] tArray = {tmp, newValue};
			JSONPath.set(newJson, newPath, tArray);
		}
		else if (JSONPath.size(newJson, newPath) > 1) {
			//path下已经存在数组对象，直接追加
			JSONPath.arrayAdd(newJson, newPath, newValue);
		}
		else {
			//path不存在，直接添加对象
			JSONPath.set(newJson, newPath, newValue);
		}
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
			if (StringUtils.isNotBlank(template.getOldPath())) {
				//针对源路径进行转换     /orderList[].skuName
				String convertOldPath = "/" + template.getOldPath()
						.replaceAll(OLD_PATH_REGEX, OLD_PATH_REPLACEMENT)
						.replace(DOT, OLD_PATH_REPLACE);
				template.setOldPath(convertOldPath);

				//针对目标路径进行转换    /order_list[1].sku_name
				String convertNewPath = "/" + template.getNewPath()
						.replaceAll(NEW_PATH_REGEX, NEW_PATH_REPLACEMENT)
						.replaceAll(OLD_PATH_REGEX, "/0")        //如果转换后json单个数组为空就赛一个下标为0的对象到该集合（如jd的商品为单sku，要放到item中的sku集合中）
						.replace(DOT, SLASH);
				template.setNewPath(convertNewPath);
			}
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

		if (StringUtils.isNotBlank(valueExpression)) {
			val = evaluate(val, valueExpression);
		}

		if (StringUtils.isBlank(targetTypeName)) {
			return val;
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
		case "stringTime":
			newVal = TimeUtils.toString(val.toString().trim());
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
