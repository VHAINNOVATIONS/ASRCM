package gov.va.med.srcalc.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is devoted to translating any information that comes from Vista
 * to element values present on the web pages (values that come from the srcalc database).
 */
public class VistaFieldTranslation {
	public static final Map<String, String> TRANSLATION_MAP;
	static {
		final Map<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("M", "Male");
		tempMap.put("F", "Female");
		TRANSLATION_MAP = Collections.unmodifiableMap(tempMap);
	}
}
