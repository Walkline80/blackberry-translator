package com.walkline.app;

import com.walkline.screen.TranslatorScreen;

import net.rim.device.api.ui.UiApplication;

public class TranslatorApp extends UiApplication
{
    public static void main(String[] args)
    {
        TranslatorApp theApp = new TranslatorApp();       
        theApp.enterEventDispatcher();
    }

    public TranslatorApp() {pushScreen(new TranslatorScreen());}    
}