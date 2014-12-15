package com.walkline.translator.dao;

import com.walkline.translator.TranslatorException;
import com.walkline.translator.TranslatorSDK;
import com.walkline.translator.inf.Result;
import com.walkline.util.json.JSONObject;

public class TranslatorResult extends TranslatorObject implements Result
{
	private String _source = "";
	private String _destination = "";

	public TranslatorResult(TranslatorSDK translator, JSONObject jsonObject) throws TranslatorException
	{
		super(translator, jsonObject);

		JSONObject resultObject = jsonObject;
		if (resultObject != null)
		{
			_source = resultObject.optString("src");
			_destination = resultObject.optString("dst");
		}
	}

	public String getSource() {return _source;}

	public String getDestination() {return _destination;}
}