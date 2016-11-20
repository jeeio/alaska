package io.jee.alaska.util;

public class StringUtils {
	
	public static String trimCurtailWhitespace(String text){
		if(text==null || text.equals("")){
			return "";
		}
		text = text.replaceAll("[\\u0020\\u00A0]+", " ");
		return text.trim();
	}

	/**
	 * <b>截取指定字节长度的字符串，不能返回半个汉字</b> 例如： 如果网页最多能显示17个汉字，那么 length 则为 34
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getSubString(String orignal, int length) {
		int count = 0;
		int offset = 0;
		char[] c = orignal.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				return orignal.substring(0, i + 1);
			}
			if ((count == length + 1 && offset == 2)) {
				return orignal.substring(0, i);
			}
		}
		return orignal;
	}

}
