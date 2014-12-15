package com.walkline.util;

public class Enumerations
{
	public static class RefreshActions
	{
		public static final int SONGSLIST = 0;
		public static final int SONGDETAILS = 1;
		public static final int ALBUMSLIST = 2;
		public static final int ALBUMDETAILS = 3;
		public static final int PLAYLIST = 4;
		//public static final int THEMES = 5;
		//public static final int THEMESTORIES = 6;
	}

	public static class Toplists
	{
		public static final int NEW = 0;
		public static final int HOT = 1;

		public static final String[] categories = {"新歌榜", "热歌榜"};
		public static final String[] playlistIds = {"3779629", "3778678"};
	}

	public static class ShortcutsKey
	{
		public static final String[] choicesShortcutKeys = {"", "E", "F", "G", "I", "J", "K", "Q", "R", "W", "X", "Y", "Z"};
		
		public static final int DEFAULT_KEY = 12;
	}
}