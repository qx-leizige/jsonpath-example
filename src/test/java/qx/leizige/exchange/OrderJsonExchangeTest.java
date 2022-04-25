package qx.leizige.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import qx.leizige.BaseTest;
import qx.leizige.JsonExchange;

import java.util.HashMap;
import java.util.Map;

public class OrderJsonExchangeTest extends BaseTest {

    private final String sourceJson = "{\n" +
            "  \"orderNo\": \"ORDER666\",\n" +
            "  \"orderName\": \"ORDER_NAME\",\n" +
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_no", "orderNo");
        map.put("order_name", "orderName");

        //单个字段转集合
        map.put("order_line_no_list[1].order_no", "orderLineList[].orderLineNo");
        map.put("sku_code_list[1].sku_code", "orderLineList[].skuCode");
        map.put("sku_name_list[1].sku_name", "orderLineList[].skuName");
        map.put("order_line_price_list[1].price", "orderLineList[].price");
        //集合中的字段转换成单一字段(逗号分隔)
        map.put("order_line_no_simple_list", "orderLineList[].orderLineNo");


        //集合转换
        //1.整个数组复制过来,改变数组属性名
//        map.put("order_line_new_list[1]", "orderLineList[]");
        //2.数组下面所有字段转换成新的,字段名转换
        map.put("order_line_new_list[1].order_no", "orderLineList[].orderLineNo");
        map.put("order_line_new_list[1].sku_code", "orderLineList[].skuCode");
        map.put("order_line_new_list[1].sku_name", "orderLineList[].skuName");
        map.put("order_line_new_list[1].price", "orderLineList[].price");
        //3.集合套集合
        map.put("sku_property_list[1].sku_property[1]", "itemList[].skuList[].skuProperty");
        map.put("sku_property_list[1].sku_property[1].sku_name", "itemList[].skuList[].skuName");



        //层级合并
//        map.put("c.c1", "C.C1.C11");
//        //部分层级合并
//        map.put("d.d1[1].d12", "C.C1[].C11");
//        //层级重组
//        map.put("e[2].e1[1].e12", "C[].C1[].C11");
        JSONObject sourceJsonObj = JSON.parseObject(sourceJson);

        JSONObject targetJsonObj = JsonExchange.exchangeJson(sourceJsonObj, map);
        System.err.println(JSON.toJSONString(targetJsonObj, true));

    }

}
