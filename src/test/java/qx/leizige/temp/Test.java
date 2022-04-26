package qx.leizige.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import qx.leizige.BaseTest;
import qx.leizige.JsonFormatConverter;
import qx.leizige.module.JsonConvertTemplate;

import java.util.ArrayList;
import java.util.List;

public class Test extends BaseTest {


    String json = "{\n" +
            "        \"full_order_info_list\":[\n" +
            "            {\n" +
            "                \"full_order_info\":{\n" +
            "                    \"child_info\":{\n" +
            "                        \"child_orders\":[\n" +
            "\n" +
            "                        ]\n" +
            "                    },\n" +
            "                    \"remark_info\":{\n" +
            "                        \"buyer_message\":\"\"\n" +
            "                    },\n" +
            "                    \"address_info\":{\n" +
            "                        \"self_fetch_info\":\"\",\n" +
            "                        \"delivery_address\":\"dddd 8 -2-1 11212\",\n" +
            "                        \"delivery_postal_code\":\"\",\n" +
            "                        \"receiver_name\":\"ddd\",\n" +
            "                        \"delivery_province\":\"北京市\",\n" +
            "                        \"delivery_city\":\"北京市\",\n" +
            "                        \"delivery_district\":\"西城区\",\n" +
            "                        \"address_extra\":\"{\\\"areaCode\\\":\\\"110102\\\",\\\"lon\\\":116.37238235342001,\\\"lat\\\":39.918210167238826}\",\n" +
            "                        \"receiver_tel\":\"18513174780\"\n" +
            "                    },\n" +
            "                    \"pay_info\":{\n" +
            "                        \"outer_transactions\":[\n" +
            "                            \"2218141343550000020506\"\n" +
            "                        ],\n" +
            "                        \"post_fee\":\"0.00\",\n" +
            "                        \"phase_payments\":[\n" +
            "\n" +
            "                        ],\n" +
            "                        \"total_fee\":\"100.00\",\n" +
            "                        \"payment\":\"100.00\",\n" +
            "                        \"transaction\":[\n" +
            "                            \"2218141343550000020506\"\n" +
            "                        ]\n" +
            "                    },\n" +
            "                    \"buyer_info\":{\n" +
            "                        \"outer_user_id\":\"\",\n" +
            "                        \"buyer_phone\":\"18513174780\",\n" +
            "                        \"yz_open_id\":\"fjDNZRya704054594132512768\",\n" +
            "                        \"fans_type\":0,\n" +
            "                        \"fans_id\":0,\n" +
            "                        \"fans_nickname\":\"\"\n" +
            "                    },\n" +
            "                    \"orders\":[\n" +
            "                        {\n" +
            "                            \"is_cross_border\":\"\",\n" +
            "                            \"outer_item_id\":\"\",\n" +
            "                            \"item_type\":0,\n" +
            "                            \"discount_price\":\"25.00\",\n" +
            "                            \"num\":4,\n" +
            "                            \"oid\":\"2856256973371867214\",\n" +
            "                            \"title\":\"大米\",\n" +
            "                            \"fenxiao_payment\":\"0.00\",\n" +
            "                            \"buyer_messages\":\"\",\n" +
            "                            \"root_sku_id\":\"\",\n" +
            "                            \"is_present\":false,\n" +
            "                            \"cross_border_trade_mode\":\"\",\n" +
            "                            \"price\":\"25.00\",\n" +
            "                            \"sub_order_no\":\"\",\n" +
            "                            \"total_fee\":\"100.00\",\n" +
            "                            \"fenxiao_price\":\"0.00\",\n" +
            "                            \"alias\":\"2x7qk6j668x5r\",\n" +
            "                            \"payment\":\"100.00\",\n" +
            "                            \"is_pre_sale\":\"\",\n" +
            "                            \"outer_sku_id\":\"\",\n" +
            "                            \"sku_unique_code\":\"4080407261092426\",\n" +
            "                            \"goods_url\":\"https://h5.youzan.com/v2/showcase/goods?alias=2x7qk6j668x5r\",\n" +
            "                            \"customs_code\":\"\",\n" +
            "                            \"item_id\":408040726,\n" +
            "                            \"item_tags\":{\n" +
            "                                \"is_deliver_goods\":true\n" +
            "                            },\n" +
            "                            \"weight\":\"\",\n" +
            "                            \"sku_id\":1092426,\n" +
            "                            \"sku_properties_name\":\"[{\\\"k\\\":\\\"重量\\\",\\\"k_id\\\":308415,\\\"v\\\":\\\"三斤\\\",\\\"v_id\\\":5414953}]\",\n" +
            "                            \"pic_path\":\"https://img.yzcdn.cn/upload_files/2021/08/09/FrYFP_VPYimB2mLpkciEmwGKFn8K.jpeg\",\n" +
            "                            \"pre_sale_type\":\"\",\n" +
            "                            \"points_price\":\"0\",\n" +
            "                            \"root_item_id\":\"0\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"source_info\":{\n" +
            "                        \"is_offline_order\":false,\n" +
            "                        \"book_key\":\"03030221-70bc-451b-98e6-0e60871cb27e\",\n" +
            "                        \"biz_source\":\"\",\n" +
            "                        \"source\":{\n" +
            "                            \"platform\":\"browser\",\n" +
            "                            \"wx_entrance\":\"direct_buy\"\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"order_info\":{\n" +
            "                        \"consign_time\":\"2022-02-14 13:44:27\",\n" +
            "                        \"order_extra\":{\n" +
            "                            \"is_from_cart\":\"false\",\n" +
            "                            \"is_member\":\"true\",\n" +
            "                            \"salesman\":{\n" +
            "                                \"salesman_mobile\":\"18513174780\",\n" +
            "                                \"salesman_nick_name\":\"巴音\"\n" +
            "                            },\n" +
            "                            \"marketing_channel\":\"directSeller\",\n" +
            "                            \"is_points_order\":\"0\",\n" +
            "                            \"settle_time\":\"1644817586000\"\n" +
            "                        },\n" +
            "                        \"created\":\"2022-02-14 13:43:52\",\n" +
            "                        \"expired_time\":\"2022-02-14 15:43:52\",\n" +
            "                        \"status_str\":\"已完成\",\n" +
            "                        \"success_time\":\"2022-02-14 13:44:41\",\n" +
            "                        \"type\":0,\n" +
            "                        \"shop_name\":\"心怡画坊qa测试-12345678901234567788\",\n" +
            "                        \"tid\":\"E20220214134351041802034\",\n" +
            "                        \"confirm_time\":\"\",\n" +
            "                        \"pay_time\":\"2022-02-14 13:43:55\",\n" +
            "                        \"node_kdt_id\":491391,\n" +
            "                        \"update_time\":\"2022-02-14 13:46:34\",\n" +
            "                        \"pay_type_str\":\"ECARD\",\n" +
            "                        \"is_retail_order\":false,\n" +
            "                        \"pay_type\":28,\n" +
            "                        \"team_type\":0,\n" +
            "                        \"refund_state\":0,\n" +
            "                        \"root_kdt_id\":491391,\n" +
            "                        \"close_type\":0,\n" +
            "                        \"status\":\"TRADE_SUCCESS\",\n" +
            "                        \"express_type\":0,\n" +
            "                        \"order_tags\":{\n" +
            "                            \"is_member\":true,\n" +
            "                            \"is_secured_transactions\":true,\n" +
            "                            \"is_settle\":true,\n" +
            "                            \"is_payed\":true\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"total_results\":1\n" +
            "    }";


    @org.junit.Test
    public void xx(){
        List<JsonConvertTemplate> templateList = new ArrayList<>();
        templateList.add(new JsonConvertTemplate("deliveryProvince", "full_order_info_list[].full_order_info.address_info.delivery_province", "", ""));
        templateList.add(new JsonConvertTemplate("orderDetailList[1].Price", "full_order_info_list[].full_order_info.orders[].discount_price", "", ""));
        templateList.add(new JsonConvertTemplate("orderDetailList[1].skuId", "full_order_info_list[].full_order_info.orders[].sku_id", "", ""));
        templateList.add(new JsonConvertTemplate("orderDetailList[1].itemTage", "full_order_info_list[].full_order_info.orders[].item_tags.is_deliver_goods", "", ""));
        JSONObject sourceJsonObj = JSON.parseObject(json);
        JSONObject targetJsonObj = JsonFormatConverter.exchangeJson(sourceJsonObj, templateList);
        System.err.println(JSON.toJSONString(targetJsonObj, true));
    }

}
