package com.walkline.translator;

import java.util.Hashtable;

import localization.TranslatorResource;

import net.rim.device.api.i18n.ResourceBundle;

import com.walkline.translator.dao.TranslatorQueryResult;
import com.walkline.translator.inf.QueryResult;
import com.walkline.util.Function;
import com.walkline.util.json.JSONException;
import com.walkline.util.json.JSONObject;
import com.walkline.util.json.JSONTokener;
import com.walkline.util.network.HttpClient;
import com.walkline.util.network.MyConnectionFactory;

public class TranslatorSDK implements TranslatorResource
{
	private static ResourceBundle _bundle = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

	protected HttpClient _http;
	public static TranslatorSDK getInstance() {return new TranslatorSDK();}
	protected TranslatorSDK() {_http = new HttpClient(new MyConnectionFactory());}

	private QueryResult queryTranslate(JSONObject jsonObject) throws TranslatorException {return new TranslatorQueryResult(this, jsonObject);}
	public QueryResult queryTranslate(String from, String to, String q)
	{
		QueryResult result = null;
		Hashtable params = new Hashtable();

		params.put("q", q);
		params.put("from", from);
		params.put("to", to);
		params.put("appid", TranslatorConfig.client_ID);
		params.put("salt", TranslatorConfig.client_SALT);
		params.put("sign", Function.calculateSign(q));

		JSONObject jsonObject;
		try {
			jsonObject = doRequest(TranslatorConfig.queryTranslate, params);
			result = (jsonObject != null ? queryTranslate(jsonObject) : null);
		} catch (Exception e) {}

		return result;
	}

	private JSONObject doRequest(String api, Hashtable params) throws Exception
	{
		JSONObject result = null;
		StringBuffer responseBuffer = null;

		try {
			responseBuffer = checkResponse(_http.doGet(api, params));
			if ((responseBuffer == null) || (responseBuffer.length() <= 0))
			{
				result = null;
			} else {
				result = new JSONObject(new JSONTokener(new String(responseBuffer.toString().getBytes(), "utf-8")));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} catch (Throwable t) {
			throw new Exception(t.getMessage());
		}

		return result;
	}

	private StringBuffer checkResponse(StringBuffer res) throws TranslatorException
	{
		if ((res != null) && (res.length() > 0))
		{
			if ((res.charAt(0) == '{') && (res.charAt(res.length() - 1) == '}'))
			{
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(new JSONTokener(res.toString()));

					if (!jsonObject.optString("error_code").equals(""))
					{
						String error_msg = jsonObject.optString("error_msg");
						String error_code = jsonObject.optString("error_code");

						if (error_code.equals("52001"))
						{
							Function.errorDialog(getResString(ERROR_CODE_TIMEOUT));
							return null;
						} else if (error_code.equals("52002")) {
							Function.errorDialog(getResString(ERROR_CODE_SYSTEM_ERROR));
							return null;
						} else if (error_code.equals("52003")) {
							Function.errorDialog(getResString(ERROR_CODE_UNAUTHORIZED_USER));
							return null;
						} else if (error_code.equals("54003")) {
							Function.errorDialog(getResString(ERROR_CODE_FREQUENCY_LIMITED));
							return null;
						} else if (error_code.equals("54004")) {
							Function.errorDialog(getResString(ERROR_CODE_BALANCE_NOT_ENOUGH));
							return null;
						} else {
							Function.errorDialog("\u2022 error_code: " + error_code + "\n\u2022 error_msg: " + error_msg);
							return null;
						}
					}
				} catch (JSONException e) {}
			} else {
				throw new UnknownException(res.toString());
			}
		}

		return res;
	}

	private String getResString(int key) {return _bundle.getString(key);}
}