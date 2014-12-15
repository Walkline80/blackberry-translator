package com.walkline.screen;

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
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.container.MainScreen;

import com.walkline.app.TranslatorApp;
import com.walkline.app.TranslatorAppConfig;
import com.walkline.translator.TranslatorSDK;
import com.walkline.translator.inf.QueryResult;
import com.walkline.translator.inf.Result;
import com.walkline.util.Enumerations.RefreshActions;
import com.walkline.util.Function;
import com.walkline.util.ui.ForegroundManager;
import com.walkline.util.ui.ListStyleButtonField;
import com.walkline.util.ui.ListStyleButtonSet;
import com.walkline.util.ui.MyTextField;

public final class TranslatorScreen extends MainScreen
{
	ForegroundManager _foreground = new ForegroundManager(0);
	ListStyleButtonSet _listSet = new ListStyleButtonSet();
	ListStyleButtonField _item;
	TranslatorSDK _translator = TranslatorSDK.getInstance();
	MyTextField _editSearch = new MyTextField();
	MyTextField _editResult = new MyTextField();

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

        _editSearch.setPadding(3, 3, 3, 3);
        _editSearch.setMargin(10, 3, 3, 3);
        _editResult.setPadding(3, 3, 3, 3);
        _editResult.setMargin(10, 3, 3, 3);
        add(_editSearch);
        add(_editResult);

        //_foreground.add(_toplistSet);
        //add(_foreground);
    }

	private void searchSongs(final String keyword)
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
    	{
			public void run()
			{
				RefreshContentsScreen popupScreen = new RefreshContentsScreen(_translator, keyword, RefreshActions.SONGSLIST);
				UiApplication.getUiApplication().pushModalScreen(popupScreen);

				QueryResult songsList = popupScreen.getQueryResult();

				if (popupScreen != null) {popupScreen = null;}
				if (songsList != null) {refreshSongsList(songsList);}
			}
		});
	}

	private void refreshSongsList(QueryResult list)
	{
		Vector songsList = list.getTranslateResult();
		Result song;

		if (songsList.size() <= 0)
		{
			Function.errorDialog("No Result!");
			return;
		}

		if (_listSet.getManager() == null) {_foreground.add(_listSet);}
		if (_listSet.getFieldCount() > 0) {_listSet.deleteAll();}

		for (int i=0; i<songsList.size(); i++)
		{
			song = (Result) songsList.elementAt(i);
			if (song != null)
			{
				_item = new ListStyleButtonField(song);
				_listSet.add(_item);
			}
		}
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