package qx.leizige.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;
import qx.leizige.BaseTest;
import qx.leizige.JsonFormatConverter;
import qx.leizige.module.JsonConvertTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonFormatConverterTest extends BaseTest {


	private static final String fileName = "/sourceJson.json";

	private String sourceJson;

	@Override
	public void before() {
		InputStream inputStream = JsonFormatConverterTest.class.getResourceAsStream(fileName);
		sourceJson = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
	}

	@Test
	public void test() {
		List<JsonConvertTemplate> templateList = new ArrayList<>();
		templateList.add(new JsonConvertTemplate("success", "n_success", "", "", ""));
		templateList.add(new JsonConvertTemplate("", "order_no", "", "", "ORDER_NO_66666"));
		templateList.add(new JsonConvertTemplate("orderName", "order_name", "", "", ""));
		templateList.add(new JsonConvertTemplate("createTime", "create_time", "LocalDateTime", "", ""));
		templateList.add(new JsonConvertTemplate("orderStatus", "order_status", "Short", "if(value == '3') {value = '10'}; if(value == '6'){value='20'};return value;", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].orderLineNo", "order_line_no_list[1].order_no", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].skuCode", "sku_code_list[1].sku_code", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].skuName", "sku_name_list[1].sku_name", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].price", "order_line_price_list[1].price", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].orderLineNo", "order_line_no_simple_list", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[]", "order_line_new_list_w[1]", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].orderLineNo", "order_line_new_list[1].order_no", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].skuCode", "order_line_new_list[1].sku_code", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].skuName", "order_line_new_list[1].sku_name", "", "", ""));
		templateList.add(new JsonConvertTemplate("orderLineList[].price", "order_line_new_list[1].price", "", "", ""));
		templateList.add(new JsonConvertTemplate("itemList[].skuList[].skuProperty", "sku_property_list[1].sku_property[1]", "", "", ""));
		JSONObject sourceJsonObj = JSON.parseObject(sourceJson);
		JSONObject targetJsonObj = JsonFormatConverter.exchangeJson(sourceJsonObj, templateList);
		System.err.println(JSON.toJSONString(targetJsonObj, true));
	}

	@Test
	public void groovyTest() {
		String valueExpression = " if(value == '3') {value = 10};else if(value == '6'){value=20};return value ";
		Binding binding = new Binding();
		binding.setVariable("value", "6");
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(valueExpression);
		System.out.println(value);
	}
}
