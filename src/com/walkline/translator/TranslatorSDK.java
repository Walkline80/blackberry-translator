package com.walkline.translator;

import java.util.Hashtable;

import com.walkline.translator.dao.TranslatorQueryResult;
import com.walkline.translator.inf.QueryResult;
import com.walkline.util.Function;
import com.walkline.util.json.JSONException;
import com.walkline.util.json.JSONObject;
import com.walkline.util.json.JSONTokener;
import com.walkline.util.network.HttpClient;
import com.walkline.util.network.MyConnectionFactory;

public class TranslatorSDK
{
	protected HttpClient _http;

	public static TranslatorSDK getInstance() {return new TranslatorSDK();}

	protected TranslatorSDK() {_http = new HttpClient(new MyConnectionFactory());}

	private QueryResult queryTranslate(JSONObject jsonObject) throws TranslatorException {return new TranslatorQueryResult(this, jsonObject);}
	public QueryResult queryTranslate(String from, String to, String q)
	{
		QueryResult result = null;
		Hashtable params = new Hashtable();

		params.put("from", from);
		params.put("to", to);
		params.put("client_id", TranslatorConfig.client_ID);
		params.put("q", q);

		JSONObject jsonObject;
		try {
			jsonObject = doRequest(TranslatorConfig.queryTranslate, params);
			result = (jsonObject != null ? queryTranslate(jsonObject) : null);
		} catch (Exception e) {}

		return result;
	}

	private JSONObject doRequest(String api, Hashtable args) throws Exception
	{
		JSONObject result = null;
		StringBuffer responseBuffer = null;

		try {
			responseBuffer = checkResponse(_http.doGet(api));

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
						String from = jsonObject.optString("from");
						String to = jsonObject.optString("to");
						String query = jsonObject.optString("query");

						if (error_code.equals("52001"))
						{
							Function.errorDialog("TIMEOUT：超时（52001）【请调整文本字符长度】");
							return null;
						} else if (error_code.equals("52002")) {
							Function.errorDialog("SYSTEM ERROR：翻译系统错误（52002）");
							return null;
						} else if (error_code.equals("52003")) {
							Function.errorDialog("UNAUTHORIZED USER：未授权的用户（52003）【请检查是否将api key输入错误】");
							return null;
						}

						throw new TranslatorException("\u2022 error_code: " + error_code +
													  "\n\u2022 error_msg: " + error_msg +
													  "\n\u2022 from: " + from +
													  "\n\u2022 to: " + to +
													  "\n\u2022 query: " + query
													 );
					}
				} catch (JSONException e) {}
			} else {
				throw new UnknownException(res.toString());
			}
		}

		return res;
	}
}