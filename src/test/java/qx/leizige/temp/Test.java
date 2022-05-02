package qx.leizige.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import qx.leizige.BaseTest;
import qx.leizige.JsonFormatConverter;
import qx.leizige.exchange.JsonFormatConverterTest;
import qx.leizige.module.JsonConvertTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test extends BaseTest {


    private static final String fileName = "/omsOrder.json";

    private String sourceJson;

    @Override
    public void before() {
        InputStream inputStream = Test.class.getResourceAsStream(fileName);
        sourceJson = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
    }



    @org.junit.Test
    public void xx() {
        List<JsonConvertTemplate> templateList = new ArrayList<>();
        templateList.add(new JsonConvertTemplate("full_order_info_list[].full_order_info.address_info.delivery_province", "deliveryProvince", "", "", ""));
        templateList.add(new JsonConvertTemplate("full_order_info_list[].full_order_info.address_info.delivery_province", "a[1].delivery_province", "", "", ""));
        templateList.add(new JsonConvertTemplate("full_order_info_list[].full_order_info.orders[].sub_order_no", "a[1].subOrderDetailList[2].subOrderNo", "", "", ""));
        templateList.add(new JsonConvertTemplate("full_order_info_list[].full_order_info.orders[].refund_state", "a[1].subOrderDetailList[2].returnStatus", "", "", ""));
        templateList.add(new JsonConvertTemplate("full_order_info_list[].full_order_info.orders[].title", "a[1].subOrderDetailList[2].itemName", "", "", ""));

//        templateList.add(new JsonConvertTemplate("", "omsOrder[1].sitCode", "", "", "YZ"));
        JSONObject sourceJsonObj = JSON.parseObject(sourceJson);
        JSONObject targetJsonObj = JsonFormatConverter.exchangeJson(sourceJsonObj, templateList);
        System.err.println(JSON.toJSONString(targetJsonObj, true));
    }

}
