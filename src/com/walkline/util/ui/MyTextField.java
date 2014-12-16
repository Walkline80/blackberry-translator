package com.walkline.util.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class MyTextField extends AutoTextEditField
{
	public MyTextField()
	{
		super("", null, DEFAULT_MAXCHARS, USE_ALL_HEIGHT | USE_ALL_WIDTH | JUMP_FOCUS_AT_END);

		XYEdges edge = new XYEdges(4, 4, 4, 4);
		Border borderNormal = BorderFactory.createRoundedBorder(edge, 0xBBBBBB, Border.STYLE_SOLID);
		Border borderFocus = BorderFactory.createRoundedBorder(edge, 0x0A9000, Border.STYLE_SOLID);
		Background background = BackgroundFactory.createSolidBackground(Color.WHITE);

		this.setBackground(background);
		this.setBorder(VISUAL_STATE_NORMAL, borderNormal);
		this.setBorder(VISUAL_STATE_FOCUS, borderFocus);
	}

	protected void layout(int width, int height)
	{
		super.layout(width, height);
		super.setExtent(width, Font.getDefault().getHeight() * 4);
	}

	protected void displayFieldFullMessage() {}

    public void setDirty(boolean dirty) {}

    public void setMuddy(boolean muddy) {}
}