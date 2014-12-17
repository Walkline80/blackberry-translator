package com.walkline.app;

import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;

public class TranslatorAppConfig
{
	public static final String APP_TITLE = "Translator for you";
	public static final String UNDERLINE = "\u0332";
	public static final String BBW_APPID = "58645271";

	public static int[] preferredTransportTypes = {
													TransportInfo.TRANSPORT_TCP_WIFI,
													TransportInfo.TRANSPORT_BIS_B,
													TransportInfo.TRANSPORT_TCP_CELLULAR,
													TransportInfo.TRANSPORT_WAP2
												  };
	public static int[] disallowedTransportTypes = {
													TransportInfo.TRANSPORT_MDS,
													TransportInfo.TRANSPORT_WAP
												   };

	public static final Font FONT_SONG_TITLE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 1, Ui.UNITS_pt);
	public static final Font FONT_SONG_ALBUM = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt), Ui.UNITS_pt);
	public static final Font FONT_MAIN_TITLE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 1, Ui.UNITS_pt);

	//SKU: 0xefdc1d2dd58d7c3L (translator_for_you_written_by_walkline_wang)
}