package com.walkline.util.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class MyTextField extends VerticalFieldManager
{
    private static final int fieldHeight = Font.getDefault().getHeight() * 3;
    private AutoTextEditField textField = null;

    public MyTextField()
    {
        super(Manager.NO_VERTICAL_SCROLL);

        XYEdges edge = new XYEdges(4, 4, 4, 4);
		Border borderNormal = BorderFactory.createRoundedBorder(edge, 0xBBBBBB, Border.STYLE_SOLID);
		Border borderFocus = BorderFactory.createRoundedBorder(edge, 0x0A9000, Border.STYLE_SOLID);
		Background background = BackgroundFactory.createSolidBackground(Color.WHITE);

		setBackground(background);
		setBorder(VISUAL_STATE_NORMAL, borderNormal);
		setBorder(VISUAL_STATE_FOCUS, borderFocus);

        VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
        textField = new AutoTextEditField("", null, AutoTextEditField.DEFAULT_MAXCHARS, AutoTextEditField.JUMP_FOCUS_AT_END | AutoTextEditField.NO_SWITCHING_INPUT);

        vfm.add(textField);
        add(vfm);
    }

    public void sublayout(int width, int height)
    {
        super.sublayout(width, fieldHeight);
        setExtent(width, fieldHeight);
    }

    public void setEditable(boolean editable) {textField.setEditable(editable);}

    public String getText() {return textField.getText();}

    public void setText(String text) {textField.setText(text);}
}