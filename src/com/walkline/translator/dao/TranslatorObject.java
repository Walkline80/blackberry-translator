package com.walkline.translator.dao;

import com.walkline.translator.TranslatorException;
import com.walkline.translator.TranslatorSDK;
import com.walkline.util.json.JSONObject;

public class TranslatorObject implements com.walkline.translator.inf.Object
{
	protected TranslatorSDK translator;
	protected JSONObject jsonObject;

	public TranslatorObject(TranslatorSDK pTranslator, JSONObject pJsonObject) throws TranslatorException
	{
		if ((pTranslator == null) || (pJsonObject == null))
		{
			throw new TranslatorException("Unable to create TranslatorObject.");
		}

		translator = pTranslator;
		jsonObject = pJsonObject;
	}
}