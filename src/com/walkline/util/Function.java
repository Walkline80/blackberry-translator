package com.walkline.util;

import java.io.UnsupportedEncodingException;

import localization.TranslatorResource;

import com.walkline.translator.TranslatorConfig;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Function implements TranslatorResource
{
	private static ResourceBundle _bundle = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeAndWait(new Runnable()
        {
            public void run() {Dialog.alert(message);} 
        });
    }

    public static void showExitDialog()
    {
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
				String[] yesno = getResStringArray(DIALOG_YES_NO);
				Dialog showDialog = new Dialog(getResString(DIALOG_QUIT), yesno, null, 1, Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 0);

				showDialog.doModal();
				if (showDialog.getSelectedValue() == 0) {System.exit(0);}
			}
		});
    }

    public static String calculateSign(String q)
    {
    	StringBuffer md5String = new StringBuffer();
    	String md5 = "";

    	try {
    		md5String.append(TranslatorConfig.client_ID).append(new String(q.getBytes("utf-8"))).append(TranslatorConfig.client_SALT).append(TranslatorConfig.client_SECRET);
			md5 = Digest.md5Hash(md5String.toString());
		} catch (UnsupportedEncodingException e) {}

    	return md5.toLowerCase();
    }

    private static String getResString(int key) {return _bundle.getString(key);}
	private static String[] getResStringArray(int key) {return _bundle.getStringArray(key);}
}