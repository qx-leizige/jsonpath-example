package qx.leizige.exchange;

import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Before;
import org.junit.Test;
import qx.leizige.JsonFormatConverter;
import qx.leizige.module.JsonConvertTemplate;
import qx.leizige.utils.TestFileUtil;

/**
 * 测试默认值填充
 */
public class FillDefaultValueTest {


	String sourceJson = "";
	List<JsonConvertTemplate> templateList = Collections.emptyList();

	final String convertTemplatePath = "/fill/jingdong.ware.read.searchWare4Valid(response).json";

	@Before
	public void before() {
		sourceJson = TestFileUtil.readFileToString("/fill/SourceJson.json");
		templateList = JSON.parseObject(TestFileUtil.readFileToString(convertTemplatePath), new TypeReference<List<JsonConvertTemplate>>() { }.getType());
	}


	@Test
	public void Test() {
		JSON json = JsonFormatConverter.exchangeJson(sourceJson, templateList);
		System.out.println(json.toJSONString());
	}


}
