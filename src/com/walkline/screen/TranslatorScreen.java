package com.walkline.screen;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.FontManager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;

import com.walkline.app.TranslatorAppConfig;
import com.walkline.translator.TranslatorSDK;
import com.walkline.translator.inf.QueryResult;
import com.walkline.translator.inf.Result;
import com.walkline.util.Enumerations.Languages;
import com.walkline.util.Function;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.MyTextField;
import com.walkline.util.ui.VerticalButtonFieldSet;

public final class TranslatorScreen extends MainScreen
{
	ForegroundManager _foreground = new ForegroundManager(0);
	TranslatorSDK _translator = TranslatorSDK.getInstance();
	MyTextField _editSearch = new MyTextField();
	MyTextField _editResult = new MyTextField();
	ObjectChoiceField _choiceSource;
	ObjectChoiceField _choiceDestination;

    public TranslatorScreen()
    {
    	super (NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | NO_SYSTEM_MENU_ITEMS);

    	String appVer = "v" + ApplicationDescriptor.currentApplicationDescriptor().getVersion();
        setTitle(TranslatorAppConfig.APP_TITLE + " - " + appVer);

		try {
			FontFamily family = FontFamily.forName("BBGlobal Sans");
			Font appFont = family.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
			FontManager.getInstance().setApplicationFont(appFont);
		} catch (ClassNotFoundException e) {}

		createUI();
    }

    private void createUI()
    {
    	XYEdges xyEdges = new XYEdges(3, 3, 3, 3);
        _editSearch.setPadding(xyEdges);
        _editSearch.setMargin(xyEdges);
        _editResult.setPadding(xyEdges);
        _editResult.setMargin(xyEdges);
//        _editResult.setEditable(false);

        _choiceSource = new ObjectChoiceField("源语言：", Languages.choicesLanguages, Languages.DEFAULT_LANGUAGE);
        _choiceDestination = new ObjectChoiceField("目标语言：", Languages.choicesLanguages, Languages.DEFAULT_LANGUAGE);

        VerticalButtonFieldSet vbf = new VerticalButtonFieldSet(USE_ALL_WIDTH);
        ButtonField btnQuery = new ButtonField("查询", ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
        btnQuery.setChangeListener(new FieldChangeListener()
        {
			public void fieldChanged(Field field, int context)
			{
				if (context != FieldChangeListener.PROGRAMMATIC)
				{
					if (!_editSearch.getText().trim().equals(""))
					{
						queryTranslator();
					} else {
						_editSearch.setText("");
						_editSearch.setFocus();
					}
				}
			}
		});

        vbf.add(btnQuery);
        _foreground.add(_editSearch);
        _foreground.add(_editResult);
        _foreground.add(_choiceSource);
        _foreground.add(_choiceDestination);
        _foreground.add(vbf);

        add(_foreground);
    }

	private void queryTranslator()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
    	{
			public void run()
			{
				Hashtable params = new Hashtable();

				params.put("from", Languages.choicesLanguagesValue[_choiceSource.getSelectedIndex()]);
				params.put("to", Languages.choicesLanguagesValue[_choiceDestination.getSelectedIndex()]);
				params.put("q", _editSearch.getText());

				RefreshContentsScreen popupScreen = new RefreshContentsScreen(_translator, params);
				UiApplication.getUiApplication().pushModalScreen(popupScreen);

				QueryResult resultList = popupScreen.getQueryResult();

				if (popupScreen != null) {popupScreen = null;}
				if (resultList != null) {refreshResultList(resultList);}
			}
		});
	}

	private void refreshResultList(QueryResult list)
	{
		Vector resultList = list.getTranslateResult();
		Result result;

		if (resultList.size() <= 0)
		{
			Function.errorDialog("No Result!");
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (int i=0; i<resultList.size(); i++)
		{
			result = (Result) resultList.elementAt(i);
			if (result != null)
			{
				sb.append(result.getDestination());
				if (resultList.size() > 1) {sb.append("\n");}
			}
		}

		_editResult.setText(sb.toString());
	}

	protected boolean keyChar(char key, int status, int time)
    {
		if (getFieldWithFocus() instanceof ForegroundManager)
		{
			switch (key)
			{
    			case Characters.LATIN_CAPITAL_LETTER_L:
    			case Characters.LATIN_SMALL_LETTER_L:
    				//return true;
			}
		}

    	return super.keyChar(key, status, time);
    }

	protected boolean onSavePrompt() {return true;}
}