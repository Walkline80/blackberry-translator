package com.walkline.screen;

import java.util.Hashtable;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.walkline.translator.TranslatorSDK;
import com.walkline.translator.inf.QueryResult;

public class RefreshContentsScreen extends PopupScreen
{
	private Thread thread = null;
	private TranslatorSDK _translator;
	private QueryResult _queryResult = null;

	public RefreshContentsScreen(TranslatorSDK translator, final Hashtable params)
	{
		super(new VerticalFieldManager());

		_translator = translator;

		add(new LabelField("Please wait....", Field.FIELD_HCENTER));

		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				thread = new Thread(new QueryTranslatorRunnable(params));
				thread.start();				
			}
		});
	}

	class QueryTranslatorRunnable implements Runnable
	{
		private String from = "";
		private String to = "";
		private String q = "";

		public QueryTranslatorRunnable(Hashtable params)
		{
			from = (String) params.get("from");
			to = (String) params.get("to");
			q = (String) params.get("q");
		}

		public void run()
		{
			_queryResult = _translator.queryTranslate(from, to, q);

			Application.getApplication().invokeLater(new Runnable() {public void run() {onClose();}});
		}
	}

	public QueryResult getQueryResult() {return _queryResult;}

	public boolean onClose()
	{
		if (thread != null)
		{
			try {
				thread.interrupt();
				thread = null;
			} catch (Exception e) {}
		}

		try {
			UiApplication.getUiApplication().popScreen(this);	
		} catch (Exception e) {}

		return true;
	}

	protected boolean keyChar(char key, int status, int time)
	{
		if (key == Characters.ESCAPE)
		{
			onClose();

			return true;
		}

		return super.keyChar(key, status, time);
	}
} 