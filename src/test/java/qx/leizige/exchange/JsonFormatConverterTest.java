package qx.leizige.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;
import qx.leizige.BaseTest;
import qx.leizige.JsonFormatConverter;
import qx.leizige.module.JsonConvertTemplate;

import java.util.ArrayList;
import java.util.List;

public class JsonFormatConverterTest extends BaseTest {

    private final String sourceJson = "{\n" +
            "  \"orderNo\": \"ORDER666\",\n" +
            "  \"orderName\": \"ORDER_NAME\",\n" +
            "  \"createTime\":\"2020-11-18T04:31:40.886Z\",\n" +
            "  \"orderStatus\":\"3\",\n" +
            "  \"orderLineList\": [\n" +
            "    {\n" +
            "      \"orderLineNo\": \"ORDER_LINE666\",\n" +
            "      \"skuCode\": \"SKU_CODE666\",\n" +
            "      \"skuName\": \"NIKE\",\n" +
            "      \"price\": \"123\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"orderLineNo\": \"ORDER_LINE777\",\n" +
            "      \"skuCode\": \"SKU_CODE777\",\n" +
            "      \"skuName\": \"NIKE\",\n" +
            "      \"price\": \"123\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"itemList\": [\n" +
            "    {\n" +
            "      \"itemName\": \"鞋子\",\n" +
            "      \"skuList\": [\n" +
            "        {\n" +
            "          \"skuName\": \"鞋子A\",\n" +
            "          \"skuPrice\": \"66\",\n" +
            "          \"skuProperty\": [\n" +
            "            {\n" +
            "              \"pName\": \"颜色\",\n" +
            "              \"pValue\": \"黑色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"尺码\",\n" +
            "              \"pValue\": \"30\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"材料\",\n" +
            "              \"pValue\": \"塑料\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"skuName\": \"鞋子B\",\n" +
            "          \"skuPrice\": \"77\",\n" +
            "          \"skuProperty\": [\n" +
            "            {\n" +
            "              \"pName\": \"颜色\",\n" +
            "              \"pValue\": \"白色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"尺码\",\n" +
            "              \"pValue\": \"40\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"材料\",\n" +
            "              \"pValue\": \"塑料\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"itemName\": \"裤子\",\n" +
            "      \"skuList\": [\n" +
            "        {\n" +
            "          \"skuName\": \"裤子A\",\n" +
            "          \"skuPrice\": \"66\",\n" +
            "          \"skuProperty\": [\n" +
            "            {\n" +
            "              \"pName\": \"颜色\",\n" +
            "              \"pValue\": \"黄色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"尺码\",\n" +
            "              \"pValue\": \"L\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"材料\",\n" +
            "              \"pValue\": \"纯棉\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"skuName\": \"裤子B\",\n" +
            "          \"skuPrice\": \"77\",\n" +
            "          \"skuProperty\": [\n" +
            "            {\n" +
            "              \"pName\": \"颜色\",\n" +
            "              \"pValue\": \"白色\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"尺码\",\n" +
            "              \"pValue\": \"XXL\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"pName\": \"材料\",\n" +
            "              \"pValue\": \"纯棉\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void test() {
        List<JsonConvertTemplate> templateList = new ArrayList<>();
        templateList.add(new JsonConvertTemplate("orderNo","order_no",  "", ""));
        templateList.add(new JsonConvertTemplate( "orderName", "order_name","", ""));
        templateList.add(new JsonConvertTemplate("createTime","create_time",  "LocalDateTime", ""));
        templateList.add(new JsonConvertTemplate("orderStatus", "order_status", "Short", "if(value == '3') {value = '10'}; if(value == '6'){value='20'};return value;"));
        templateList.add(new JsonConvertTemplate( "orderLineList[].orderLineNo","order_line_no_list[1].order_no", "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].skuCode", "sku_code_list[1].sku_code", "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].skuName", "sku_name_list[1].sku_name", "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].price","order_line_price_list[1].price",  "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].orderLineNo","order_line_no_simple_list",  "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[]","order_line_new_list_w[1]",  "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].orderLineNo","order_line_new_list[1].order_no",  "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].skuCode","order_line_new_list[1].sku_code",  "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].skuName", "order_line_new_list[1].sku_name", "", ""));
        templateList.add(new JsonConvertTemplate("orderLineList[].price","order_line_new_list[1].price",  "", ""));
        templateList.add(new JsonConvertTemplate("itemList[].skuList[].skuProperty","sku_property_list[1].sku_property[1]",  "", ""));
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
