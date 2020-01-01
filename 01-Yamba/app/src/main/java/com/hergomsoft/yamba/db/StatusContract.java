package com.hergomsoft.yamba.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class StatusContract {

    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "status";
    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";

    // Constantes del Content Provider
    // content://com.hergomsoft.yamba.providers.StatusProvider/status
    public static final String AUTHORITY = "com.hergomsoft.yamba.providers.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int STATUS_ITEM = 1;
    public static final int STATUS_DIR = 2;

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String USER = "user";
        public static final String SCREEN_NAME = "screen_name";
        public static final String MESSAGE = "message";
        public static final String CREATED_AT = "created_at";
        public static final String FAVORITED = "is_favorited";
        public static final String RETWEETED = "is_retweeted";
        public static final String FAVORITES_COUNT = "favorites_count";
        public static final String RETWEETS_COUNT = "retweets_count";
    }

}