package com.walkline.util;

import java.io.UnsupportedEncodingException;

import com.walkline.translator.TranslatorConfig;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Function
{
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
				String[] yesno = {"是 (Y\u0332)", "否 (N\u0332)"};
				Dialog showDialog = new Dialog("确认退出？", yesno, null, 1, Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 0);

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
}