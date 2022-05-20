package qx.leizige.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class TestFileUtil {

	public static String readFileToString(String fileName) {
		InputStream inputStream = TestFileUtil.class.getResourceAsStream(fileName);
		return new BufferedReader(new InputStreamReader(inputStream)).lines()
				.collect(Collectors.joining(System.lineSeparator()));
	}

}
