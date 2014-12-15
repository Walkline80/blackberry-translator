package com.walkline.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

public class StringUtility
{
	public static String encodeUrlParameters(Hashtable parameters)
	{
		if (parameters == null) {return "";}

		StringBuffer sb = new StringBuffer();
		boolean first = true;

		Enumeration keys = parameters.keys();
		while(keys.hasMoreElements())
		{
			if (first)
			{
				first = false;
			} else {
			    sb.append("&");
			}

			String _key = (String) keys.nextElement();
			String _value = (String) parameters.get(_key);

			if (_value != null)
			{
			    try {
					sb.append(new String(_key.getBytes(), "UTF-8") + "=" + new String(_value.getBytes(), "utf-8"));
				} catch (UnsupportedEncodingException e) {Function.errorDialog(e.toString());}
			}
		}

		return sb.toString();
	}
}