package com.walkline.util.ui;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.KeypadListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.ButtonField;

import com.walkline.app.MusicAppConfig;
import com.walkline.music.inf.Album;
import com.walkline.music.inf.Artist;
import com.walkline.music.inf.Song;
import com.walkline.music.inf.SongDetails;
import com.walkline.music.inf.Tracks;

public class ListStyleButtonField extends Field
{
    public static final int DRAWPOSITION_TOP = 0;
    public static final int DRAWPOSITION_BOTTOM = 1;
    public static final int DRAWPOSITION_MIDDLE = 2;
    public static final int DRAWPOSITION_SINGLE = 3;

    private static final int CORNER_RADIUS = 16;

    private static final int HPADDING = Display.getWidth() <= 320 ? 4 : 6;

    private static int COLOR_BACKGROUND = 0xFFFFFF;
    private static int COLOR_BORDER = 0xBBBBBB;
    private static int COLOR_BACKGROUND_FOCUS = 0x186DEF;

    private MyLabelField _labelTitle;
    private MyLabelField _labelName;
    //private MyLabelField _labelArtist;
    private MyLabelField _labelAlbum;
    private int _id;
    private Album _album;
    private String _mp3_url;
    private Tracks _song;
    private int _duration;

    private Font FONT_MAIN_TITLE;
    private Font FONT_SONG_TITLE;
    private Font FONT_SONG_ALBUM;
    private int _rightOffset;
    private int _leftOffset;
    private int _labelHeight;
    private int _drawPosition = -1;

    public ListStyleButtonField(String title)
    {
    	super(USE_ALL_WIDTH | Field.FOCUSABLE);

    	_labelTitle = new MyLabelField(title);

    	setFontSize();
    }

    public ListStyleButtonField(Song song)
    {
        super(USE_ALL_WIDTH | Field.FOCUSABLE);

        Artist artist = (Artist) song.getArtistsList().elementAt(0);
		_labelName = new MyLabelField(song.getName() + " - " + artist.getName());
        //_labelArtist = new MyLabelField(artist.getName());
        _labelAlbum = new MyLabelField(song.getAlbum().getName());
        _id = song.getId();

        setFontSize();
    }

    public ListStyleButtonField(Tracks song)
    {
        super(USE_ALL_WIDTH | Field.FOCUSABLE);

        //Artist artist = (Artist) song.getArtistsList().elementAt(0);
		_labelName = new MyLabelField(song.getName() + " - " + song.getArtist().getName());
        //_labelArtist = new MyLabelField(artist.getName());
        _labelAlbum = new MyLabelField(song.getAlbum().getName());
        //_id = song.getId();
        _mp3_url = song.getMp3Url();
        _duration = song.getDuration();
        _song = song;

        setFontSize();
    }

    public ListStyleButtonField(Album album)
    {
        super(USE_ALL_WIDTH | Field.FOCUSABLE);

        String artist = (String) album.getArtist().getName();
		_labelName = new MyLabelField(album.getName());
        //_labelArtist = new MyLabelField(artist.getName());
        _labelAlbum = new MyLabelField(artist);
        _album = album;
        //_id = song.getId();

        setFontSize();
    }

    public int getSongId() {return _id;}

    public Album getAlbum() {return _album;}

    public String getMp3Url() {return _mp3_url;}

    public int getDuration() {return _duration;}

    public void setDrawPosition(int drawPosition) {_drawPosition = drawPosition;}

    public void layout(int width, int height)
    {
    	_leftOffset = HPADDING;
        _rightOffset = HPADDING;

        if (_labelTitle != null)
        {
        	_labelTitle.layout(width - _leftOffset - _rightOffset, height);
        	_labelHeight = _labelTitle.getHeight() + HPADDING;
        }

        if (_labelName != null)
        {
        	_labelName.layout(width - _leftOffset - _rightOffset, height);
            _labelHeight = _labelName.getHeight() + HPADDING;
        }

        /*
        if (_labelArtist != null)
        {
        	_labelArtist.layout(width - _leftOffset - _rightOffset, height);
        	_labelHeight += _labelArtist.getHeight() + HPADDING;
        }
        */

        if (_labelAlbum != null)
        {
        	_labelAlbum.layout(width - _leftOffset - _rightOffset, height);
        	_labelHeight += _labelAlbum.getHeight();
        }

        setExtent(width, _labelHeight + 10);
    }

    private void setFontSize()
    {
    	FONT_MAIN_TITLE = MusicAppConfig.FONT_MAIN_TITLE;
    	FONT_SONG_TITLE = MusicAppConfig.FONT_SONG_TITLE;
    	FONT_SONG_ALBUM = MusicAppConfig.FONT_SONG_ALBUM;

    	if (_labelTitle != null) {_labelTitle.setFont(FONT_MAIN_TITLE);}
    	if (_labelName != null) {_labelName.setFont(FONT_SONG_TITLE);}
    	if (_labelAlbum != null) {_labelAlbum.setFont(FONT_SONG_ALBUM);}
    }

    protected void paint(Graphics g)
    {
    	if (_labelTitle != null)
    	{
    		try
    		{
    			g.setFont(FONT_MAIN_TITLE);
    			g.pushRegion(_leftOffset, (getHeight() - _labelTitle.getHeight()) / 2, _labelTitle.getWidth(), _labelTitle.getHeight(), 0, 0);
    			_labelTitle.paint(g);
    		} finally {g.popContext();}
    	}

    	if (_labelName != null)
    	{
            try
            {
            	g.setFont(FONT_SONG_TITLE);
           		g.pushRegion(_leftOffset, HPADDING, _labelName.getWidth(), _labelName.getHeight(), 0, 0);	
            	_labelName.paint(g);
            } finally {g.popContext();}
    	}

    	/*
    	if (_labelArtist != null)
    	{
        	try
        	{
        		g.pushRegion(_leftOffset, _labelName.getHeight() + 2 * HPADDING, _labelArtist.getWidth(), _labelArtist.getHeight(), 0, 0);
        		_labelArtist.paint(g);
        	} finally {g.popContext();}
    	}
    	*/

    	if (_labelAlbum != null)
    	{
        	try
        	{
        		g.setFont(FONT_SONG_ALBUM);
        		g.setColor(g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? Color.WHITE : Color.GRAY);
        		g.pushRegion(_leftOffset, getHeight() - _labelAlbum.getHeight() - HPADDING, _labelAlbum.getWidth(), _labelAlbum.getHeight(), 0, 0);
        		_labelAlbum.paint(g);
        	} finally {g.popContext();}
    	}
    }

    protected void paintBackground(Graphics g)
    {
        if(_drawPosition < 0)
        {
            super.paintBackground(g);
            return;
        }

        int oldColour = g.getColor();
        int background = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? COLOR_BACKGROUND_FOCUS : COLOR_BACKGROUND;

        try {
            if(_drawPosition == 0)
            {
                // Top
                g.setColor(background);
                g.fillRoundRect(0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, 0, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            } else if(_drawPosition == 1)
            {
                // Bottom 
                g.setColor(background);
                g.fillRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
            } else if(_drawPosition == 2)
            {
                // Middle
                g.setColor(background);
                g.fillRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, -CORNER_RADIUS, getWidth(), getHeight() + 2 * CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            } else {
                // Single
                g.setColor(background);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(COLOR_BORDER);
                g.drawRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            }
        } finally {g.setColor(oldColour);}
    }

    protected void drawFocus(Graphics g, boolean on)
    {
        if(_drawPosition >= 0)
        {
            boolean oldDrawStyleFocus = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
            try {
                if(on)
                {
                	g.setColor(Color.WHITE);
                    g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
                }
                paintBackground(g);
                paint(g);
            } finally {
                g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus);
            }
        }
    }

    protected boolean keyChar(char character, int status, int time) 
    {
    	switch (character)
    	{
			case Characters.ENTER:
	            clickButton();
	            return true;
        }

        return super.keyChar(character, status, time);
    }

    protected boolean navigationUnclick(int status, int time) 
    {
    	if ((status & KeypadListener.STATUS_FOUR_WAY) == KeypadListener.STATUS_FOUR_WAY)
    	{
        	clickButton();
        	return true;
    	}

    	return super.navigationClick(status, time);
    }

    protected boolean trackwheelClick(int status, int time)
    {
    	if ((status & KeypadListener.STATUS_TRACKWHEEL) == KeypadListener.STATUS_TRACKWHEEL)
    	{
       		clickButton();
       		return true;
    	}

    	return super.trackwheelClick(status, time);
    }

    protected boolean invokeAction(int action) 
    {
    	switch(action)
    	{
    		case ACTION_INVOKE:
           		clickButton();
           		return true;
    	}

    	return super.invokeAction(action);
    }

    protected boolean touchEvent(TouchEvent message)
    {
        int x = message.getX(1);
        int y = message.getY(1);

        if (x < 0 || y < 0 || x > getExtent().width || y > getExtent().height) {return false;}

        switch (message.getEvent())
        {
            case TouchEvent.UNCLICK:
           		clickButton();
           		return true;
        }

        return super.touchEvent(message);
    }

    public void clickButton() {fieldChangeNotify(0);}

    public void setDirty(boolean dirty) {}
    public void setMuddy(boolean muddy) {}
}