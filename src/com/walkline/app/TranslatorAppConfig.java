package com.walkline.app;

import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class TranslatorAppConfig
{
	public static final String APP_TITLE = "BlackBerry Translator";
	public static final String UNDERLINE = "\u0332";
	public static final String BBW_APPID = "58645271";

	//public static final Background bgColor_Gradient=BackgroundFactory.createLinearGradientBackground(Color.GRAY, Color.GRAY, Color.BLACK, Color.BLACK);

	//public static final Border border_popup_Transparent=BorderFactory.createRoundedBorder(new XYEdges(16,16,16,16), Color.BLACK, 200, Border.STYLE_FILLED);
	//public static final Background bg_popup_Transparent=BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 200);

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

//	public static final Font FONT_SONG_TITLE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 1, Ui.UNITS_pt);
//	public static final Font FONT_SONG_ALBUM = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt), Ui.UNITS_pt);
//	public static final Font FONT_MAIN_TITLE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt) + 1, Ui.UNITS_pt);
//
//	public static final Font FONT_ABOUT_HEADLINE = Font.getDefault().derive(Font.BOLD | Font.ITALIC, Font.getDefault().getHeight(Ui.UNITS_pt), Ui.UNITS_pt);
//	public static final Font FONT_ABOUT_SMALL = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt)-1, Ui.UNITS_pt);
//	public static final Font FONT_ABOUT_LARGE = Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight(Ui.UNITS_pt)+1, Ui.UNITS_pt);

	//SKU: 0x823252ddc046c845L (zhihu_daily_for_you_written_by_walkline_wang)
}