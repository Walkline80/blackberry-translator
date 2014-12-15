package com.walkline.translator.dao;

import java.util.Vector;

import com.walkline.translator.TranslatorException;
import com.walkline.translator.TranslatorSDK;
import com.walkline.translator.inf.QueryResult;
import com.walkline.translator.inf.Result;
import com.walkline.util.Function;
import com.walkline.util.json.JSONArray;
import com.walkline.util.json.JSONException;
import com.walkline.util.json.JSONObject;

public class TranslatorQueryResult extends TranslatorObject implements QueryResult
{
	private String _from = "";
	private String _to = "";
	private Vector _result_list = new Vector();
	private int _result_count = 0;

	public TranslatorQueryResult(TranslatorSDK translator, JSONObject jsonObject) throws TranslatorException
	{
		super(translator, jsonObject);

		JSONObject queryResultObject = jsonObject;
		if (queryResultObject != null)
		{
			_from = queryResultObject.optString("from");
			_to = queryResultObject.optString("to");

			JSONArray resultsArray = queryResultObject.optJSONArray("trans_result");
			if (resultsArray != null)
			{
				JSONObject resultObject;

				_result_count = resultsArray.length();
				for (int i=0; i<_result_count; i++)
				{
					try {
						resultObject = (JSONObject) resultsArray.get(i);

						Result result = new TranslatorResult(translator, resultObject);
						if (result != null) {_result_list.addElement(result);}
					} catch (JSONException e) {Function.errorDialog(e.toString());}
				}
			}
		}
	}

	public String getFrom() {return _from;}

	public String getTo() {return _to;}

	public Vector getTranslateResult() {return _result_list;}

	public int getTranslateResultCount() {return _result_count;}
}