package com.hergomsoft.infogot.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class InfoGoTProvider extends ContentProvider {
    // Use an int for each URI we will run, this represents the different queries
    private static final int BOOK = 10;
    private static final int BOOK_ID = 11;
    private static final int APPEARANCE = 20;
    private static final int APPEARANCE_ID = 21;
    private static final int CHARACTER = 30;
    private static final int CHARACTER_ID = 31;
    private static final int CHARACTERTITLE = 40;
    private static final int CHARACTERTITLE_ID = 41;
    private static final int ALIAS = 50;
    private static final int ALIAS_ID = 51;
    private static final int MEMBER = 60;
    private static final int MEMBER_ID = 61;
    private static final int HOUSE = 70;
    private static final int HOUSE_ID = 71;
    private static final int HOUSETITLE = 80;
    private static final int HOUSETITLE_ID= 81;
    private static final int SEAT = 90;
    private static final int SEAT_ID = 91;
    private static final int ANCESTRALWEAPON = 100;
    private static final int ANCESTRALWEAPON_ID = 101;
    private static final int BRANCH = 110;
    private static final int BRANCH_ID = 111;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher(){
        String content = InfoGotContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, InfoGotContract.PATH_BOOK, BOOK);
        matcher.addURI(content, InfoGotContract.PATH_BOOK + "/#", BOOK_ID);
        matcher.addURI(content, InfoGotContract.PATH_APPEARANCE, APPEARANCE);
        matcher.addURI(content, InfoGotContract.PATH_APPEARANCE + "/#", APPEARANCE_ID);
        matcher.addURI(content, InfoGotContract.PATH_CHARACTER, CHARACTER);
        matcher.addURI(content, InfoGotContract.PATH_CHARACTER + "/#", CHARACTER_ID);
        matcher.addURI(content, InfoGotContract.PATH_CHARACTERTITLE, CHARACTERTITLE);
        matcher.addURI(content, InfoGotContract.PATH_CHARACTERTITLE + "/#", CHARACTERTITLE_ID);
        matcher.addURI(content, InfoGotContract.PATH_ALIAS, ALIAS);
        matcher.addURI(content, InfoGotContract.PATH_ALIAS + "/#", ALIAS_ID);
        matcher.addURI(content, InfoGotContract.PATH_MEMBER, MEMBER);
        matcher.addURI(content, InfoGotContract.PATH_MEMBER + "/#", MEMBER_ID);
        matcher.addURI(content, InfoGotContract.PATH_HOUSE, HOUSE);
        matcher.addURI(content, InfoGotContract.PATH_HOUSE + "/#", HOUSE_ID);
        matcher.addURI(content, InfoGotContract.PATH_HOUSETITLE, HOUSETITLE);
        matcher.addURI(content, InfoGotContract.PATH_HOUSETITLE + "/#", HOUSETITLE_ID);
        matcher.addURI(content, InfoGotContract.PATH_SEAT, SEAT);
        matcher.addURI(content, InfoGotContract.PATH_SEAT + "/#", SEAT_ID);
        matcher.addURI(content, InfoGotContract.PATH_ANCESTRALWEAPON, ANCESTRALWEAPON);
        matcher.addURI(content, InfoGotContract.PATH_ANCESTRALWEAPON + "/#", ANCESTRALWEAPON_ID);
        matcher.addURI(content, InfoGotContract.PATH_BRANCH, BRANCH);
        matcher.addURI(content, InfoGotContract.PATH_BRANCH + "/#", BRANCH_ID);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case BOOK:
                return InfoGotContract.BookEntry.CONTENT_TYPE;
            case BOOK_ID:
                return InfoGotContract.BookEntry.CONTENT_ITEM_TYPE;
            case APPEARANCE:
                return InfoGotContract.AppearanceEntry.CONTENT_TYPE;
            case APPEARANCE_ID:
                return InfoGotContract.AppearanceEntry.CONTENT_ITEM_TYPE;
            case CHARACTER:
                return InfoGotContract.CharacterEntry.CONTENT_TYPE;
            case CHARACTER_ID:
                return InfoGotContract.CharacterEntry.CONTENT_ITEM_TYPE;
            case CHARACTERTITLE:
                return InfoGotContract.CharacterTitleEntry.CONTENT_TYPE;
            case CHARACTERTITLE_ID:
                return InfoGotContract.CharacterTitleEntry.CONTENT_ITEM_TYPE;
            case ALIAS:
                return InfoGotContract.AliasEntry.CONTENT_TYPE;
            case ALIAS_ID:
                return InfoGotContract.AliasEntry.CONTENT_ITEM_TYPE;
            case MEMBER:
                return InfoGotContract.MemberEntry.CONTENT_TYPE;
            case MEMBER_ID:
                return InfoGotContract.MemberEntry.CONTENT_ITEM_TYPE;
            case HOUSE:
                return InfoGotContract.HouseEntry.CONTENT_TYPE;
            case HOUSE_ID:
                return InfoGotContract.HouseEntry.CONTENT_ITEM_TYPE;
            case HOUSETITLE:
                return InfoGotContract.HouseTitleEntry.CONTENT_TYPE;
            case HOUSETITLE_ID:
                return InfoGotContract.HouseTitleEntry.CONTENT_ITEM_TYPE;
            case SEAT:
                return InfoGotContract.SeatEntry.CONTENT_TYPE;
            case SEAT_ID:
                return InfoGotContract.SeatEntry.CONTENT_ITEM_TYPE;
            case ANCESTRALWEAPON:
                return InfoGotContract.AncestralWeaponEntry.CONTENT_TYPE;
            case ANCESTRALWEAPON_ID:
                return InfoGotContract.AncestralWeaponEntry.CONTENT_ITEM_TYPE;
            case BRANCH:
                return InfoGotContract.BranchEntry.CONTENT_TYPE;
            case BRANCH_ID:
                return InfoGotContract.BranchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor retCursor;
        long _id;
        switch(sUriMatcher.match(uri)){
            case BOOK:
                retCursor = db.query(InfoGotContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.BookEntry.TABLE_NAME, projection, InfoGotContract.BookEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case APPEARANCE:
                retCursor = db.query(InfoGotContract.AppearanceEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case APPEARANCE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.AppearanceEntry.TABLE_NAME, projection, InfoGotContract.AppearanceEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case CHARACTER:
                retCursor = db.query(InfoGotContract.CharacterEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CHARACTER_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.CharacterEntry.TABLE_NAME, projection, InfoGotContract.CharacterEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case CHARACTERTITLE:
                retCursor = db.query(InfoGotContract.CharacterTitleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CHARACTERTITLE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.CharacterTitleEntry.TABLE_NAME, projection, InfoGotContract.CharacterTitleEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case ALIAS:
                retCursor = db.query(InfoGotContract.AliasEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ALIAS_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.AliasEntry.TABLE_NAME, projection, InfoGotContract.AliasEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case MEMBER:
                retCursor = db.query(InfoGotContract.MemberEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEMBER_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.MemberEntry.TABLE_NAME, projection, InfoGotContract.MemberEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case HOUSE:
                retCursor = db.query(InfoGotContract.HouseEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HOUSE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.HouseEntry.TABLE_NAME, projection, InfoGotContract.HouseEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case HOUSETITLE:
                retCursor = db.query(InfoGotContract.HouseTitleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HOUSETITLE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.HouseTitleEntry.TABLE_NAME, projection, InfoGotContract.HouseTitleEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case SEAT:
                retCursor = db.query(InfoGotContract.SeatEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SEAT_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.SeatEntry.TABLE_NAME, projection, InfoGotContract.SeatEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case ANCESTRALWEAPON:
                retCursor = db.query(InfoGotContract.AncestralWeaponEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ANCESTRALWEAPON_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.AncestralWeaponEntry.TABLE_NAME, projection, InfoGotContract.AncestralWeaponEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            case BRANCH:
                retCursor = db.query(InfoGotContract.BranchEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BRANCH_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(InfoGotContract.BranchEntry.TABLE_NAME, projection, InfoGotContract.BranchEntry._ID + " = ?", new String[]{String.valueOf(_id)}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case BOOK:
                _id = db.insert(InfoGotContract.BookEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.BookEntry.buildBookUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case APPEARANCE:
                _id = db.insert(InfoGotContract.AppearanceEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = InfoGotContract.AppearanceEntry.buildAppearanceUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case CHARACTER:
                _id = db.insert(InfoGotContract.CharacterEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.CharacterEntry.buildCharacterUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case CHARACTERTITLE:
                _id = db.insert(InfoGotContract.CharacterTitleEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = InfoGotContract.CharacterTitleEntry.buildCharacterTitleUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case ALIAS:
                _id = db.insert(InfoGotContract.AliasEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.AliasEntry.buildAliasUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case MEMBER:
                _id = db.insert(InfoGotContract.MemberEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = InfoGotContract.MemberEntry.buildMemberUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case HOUSE:
                _id = db.insert(InfoGotContract.HouseEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.HouseEntry.buildHouseUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case HOUSETITLE:
                _id = db.insert(InfoGotContract.HouseTitleEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = InfoGotContract.HouseTitleEntry.buildHouseTitleUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case SEAT:
                _id = db.insert(InfoGotContract.SeatEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.SeatEntry.buildSeatUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case ANCESTRALWEAPON:
                _id = db.insert(InfoGotContract.AncestralWeaponEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = InfoGotContract.AncestralWeaponEntry.buildAncestralWeaponUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            case BRANCH:
                _id = db.insert(InfoGotContract.BranchEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri =  InfoGotContract.BranchEntry.buildBranchUri(_id);
                else
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){
            case BOOK:
                rows = db.delete(InfoGotContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case APPEARANCE:
                rows = db.delete(InfoGotContract.AppearanceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CHARACTER:
                rows = db.delete(InfoGotContract.CharacterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CHARACTERTITLE:
                rows = db.delete(InfoGotContract.CharacterTitleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ALIAS:
                rows = db.delete(InfoGotContract.AliasEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMBER:
                rows = db.delete(InfoGotContract.MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HOUSE:
                rows = db.delete(InfoGotContract.HouseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HOUSETITLE:
                rows = db.delete(InfoGotContract.HouseTitleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SEAT:
                rows = db.delete(InfoGotContract.SeatEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ANCESTRALWEAPON:
                rows = db.delete(InfoGotContract.AncestralWeaponEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BRANCH:
                rows = db.delete(InfoGotContract.BranchEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case BOOK:
                rows = db.update(InfoGotContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case APPEARANCE:
                rows = db.update(InfoGotContract.AppearanceEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CHARACTER:
                rows = db.update(InfoGotContract.CharacterEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CHARACTERTITLE:
                rows = db.update(InfoGotContract.CharacterTitleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ALIAS:
                rows = db.update(InfoGotContract.AliasEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMBER:
                rows = db.update(InfoGotContract.MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HOUSE:
                rows = db.update(InfoGotContract.HouseEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HOUSETITLE:
                rows = db.update(InfoGotContract.HouseTitleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SEAT:
                rows = db.update(InfoGotContract.SeatEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ANCESTRALWEAPON:
                rows = db.update(InfoGotContract.AncestralWeaponEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case BRANCH:
                rows = db.update(InfoGotContract.BranchEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
