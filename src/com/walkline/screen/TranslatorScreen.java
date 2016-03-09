package com.walkline.screen;

import java.util.Hashtable;
import java.util.Vector;

import localization.TranslatorResource;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Clipboard;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.FontManager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Menu;
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

public final class TranslatorScreen extends MainScreen implements TranslatorResource
{
	private static ResourceBundle _bundle = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

	ForegroundManager _foreground = new ForegroundManager(0);
	TranslatorSDK _translator = TranslatorSDK.getInstance();
	MyTextField _editSearch = new MyTextField();
	MyTextField _editResult = new MyTextField();
	ObjectChoiceField _choiceSource;
	ObjectChoiceField _choiceDestination;

    public TranslatorScreen()
    {
    	super (NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | NO_SYSTEM_MENU_ITEMS);

    	setDefaultClose(false);

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
    	XYEdges edges = new XYEdges(3, 3, 3, 3);
        _editSearch.setPadding(edges);
        _editSearch.setMargin(edges);
        _editResult.setPadding(edges);
        _editResult.setMargin(edges);
        _editResult.setEditable(false);

        String[] choicesLanguages = getResStringArray(CONFIG_FROM_TO_LANGUAGES);
        _choiceSource = new ObjectChoiceField(getResString(UI_SOURCE_LANGUAGE), choicesLanguages, Languages.DEFAULT_LANGUAGE);
        _choiceDestination = new ObjectChoiceField(getResString(UI_DESTINATION_LANGUAGE), choicesLanguages, Languages.DEFAULT_LANGUAGE);

        VerticalButtonFieldSet vbf = new VerticalButtonFieldSet(USE_ALL_WIDTH);
        ButtonField btnQuery = new ButtonField(getResString(UI_QUERY), ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
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
			Function.errorDialog(getResString(MESSAGE_NO_RESULT));
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

	private String getResString(int key) {return _bundle.getString(key);}
	private String[] getResStringArray(int key) {return _bundle.getStringArray(key);}

	MenuItem menuQuery = new MenuItem(_bundle, MENU_QUERY, 100, 10)
	{
		public void run()
		{
			if (!_editSearch.getText().trim().equals(""))
			{
				queryTranslator();
			} else {
				_editSearch.setText("");
				_editSearch.setFocus();
			}
		}
	};

	MenuItem menuCopy = new MenuItem(_bundle, MENU_COPY, 100, 20)
	{
		public void run()
		{
			Clipboard cb = Clipboard.getClipboard();
			cb.put(_editResult.getText());
		}
	};

	MenuItem menuPaste = new MenuItem(_bundle, MENU_PASTE, 100, 30)
	{
		public void run()
		{
			Clipboard cb = Clipboard.getClipboard();
			_editSearch.setText((String) cb.get());
		}
	};

	MenuItem menuExit = new MenuItem(_bundle, MENU_EXIT, 10000, 1000)
    {
    	public void run() {Function.showExitDialog();}
    };

    protected void makeMenu(Menu menu, int instance)
    {
    	if (_editSearch.isFocus())
    	{
    		menu.add(menuQuery);
    		//menu.add(menuPaste);
    		menu.addSeparator();
    	} else if (_editResult.isFocus()) {
    		menu.add(menuCopy);
    		menu.addSeparator();
    	}

    	menu.add(menuExit);

    	super.makeMenu(menu, instance);
    }

	protected boolean keyChar(char key, int status, int time)
    {
		if (!_foreground.getFieldWithFocus().equals(_editSearch))
		{
			switch (key)
			{
				case Characters.LATIN_CAPITAL_LETTER_Q:
				case Characters.LATIN_SMALL_LETTER_Q:
					Function.showExitDialog();
					return true;
			}
		}

    	return super.keyChar(key, status, time);
    }

    public boolean onClose()
    {
    	System.exit(0);
    	return true;
    }

	protected boolean onSavePrompt() {return true;}
}