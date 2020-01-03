package com.hergomsoft.infogot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates the Movie database used for this application.
 *
 * Created by adammcneilly on 9/19/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "infoGoT.db";

    /**
     * Default constructor.
     * @param context The application context using this database.
     */
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is first created.
     * @param db The database being created, which all SQL statements will be executed on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        addBookTable(db);
        addAppearanceTable(db);
        addCharacterTable(db);
        addCharacterTitleTable(db);
        addAliasTable(db);
        addMemberTable(db);
        addHouseTable(db);
        addHouseTitlesTable(db);
        addSeatTable(db);
        addAncestralWeaponTable(db);
        addBranchTable(db);
    }

    /**
     * Called whenever DATABASE_VERSION is incremented. This is used whenever schema changes need
     * to be made or new tables are added.
     * @param db The database being updated.
     * @param oldVersion The previous version of the database. Used to determine whether or not
     *                   certain updates should be run.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addBookTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.BookEntry.TABLE_NAME + " (" +
                        InfoGotContract.BookEntry.COLUMN_IDB + " INTEGER PRIMARY KEY, " +
                        InfoGotContract.BookEntry.COLUMN_NAME + " TEXT, "+
                        InfoGotContract.BookEntry.COLUMN_RELEASED+ "TEXT, "+
                        InfoGotContract.BookEntry.COLUMN_NPAGES+"INTEGER);"
        );
    }

    private void addAppearanceTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.AppearanceEntry.TABLE_NAME + " (" +
                        InfoGotContract.AppearanceEntry.COLUMN_IDB + " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.BookEntry.TABLE_NAME+"("+InfoGotContract.BookEntry.COLUMN_IDB+ ") ON DELETE CASCADE, "+
                        InfoGotContract.AppearanceEntry.COLUMN_IDC + " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.CharacterEntry.TABLE_NAME+"("+InfoGotContract.CharacterEntry.COLUMN_IDC+ ") ON DELETE CASCADE);"
        );
    }

    private void addCharacterTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" +
                        InfoGotContract.CharacterEntry.COLUMN_IDC + " INTEGER PRIMARY KEY, " +
                        InfoGotContract.CharacterEntry.COLUMN_NAME + " TEXT, " +
                        InfoGotContract.CharacterEntry.COLUMN_GENDER + " TEXT, " +
                        InfoGotContract.CharacterEntry.COLUMN_CULTURE + " TEXT, " +
                        InfoGotContract.CharacterEntry.COLUMN_BORN + "TEXT, " +
                        InfoGotContract.CharacterEntry.COLUMN_DIED + "TEXT, " +
                        InfoGotContract.CharacterEntry.COLUMN_SPOUSE + "INTEGER, " +
                        InfoGotContract.CharacterEntry.COLUMN_FATHER + "INTEGER, " +
                        InfoGotContract.CharacterEntry.COLUMN_MOTHER + "INTEGER, " +
                        "FOREIGN KEY (" + InfoGotContract.CharacterEntry.COLUMN_SPOUSE+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ ")," +
                        "FOREIGN KEY (" + InfoGotContract.CharacterEntry.COLUMN_FATHER+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ ")," +
                        "FOREIGN KEY (" + InfoGotContract.CharacterEntry.COLUMN_MOTHER+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ "));"
        );
    }

    private void addCharacterTitleTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.CharacterTitleEntry.TABLE_NAME + " (" +
                        InfoGotContract.CharacterTitleEntry.COLUMN_IDC + " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.CharacterEntry.TABLE_NAME+"("+InfoGotContract.CharacterEntry.COLUMN_IDC+ ") ON DELETE CASCADE, "+
                        InfoGotContract.CharacterTitleEntry.COLUMN_TITLE + " TEXT PRIMARY KEY);"
        );
    }

    private void addAliasTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.AliasEntry.TABLE_NAME + " (" +
                        InfoGotContract.AliasEntry.COLUMN_IDC+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.CharacterEntry.TABLE_NAME+"("+InfoGotContract.CharacterEntry.COLUMN_IDC+ ") ON DELETE CASCADE, "+
                        InfoGotContract.AliasEntry.COLUMN_ALIAS+ " TEXT PRIMARY KEY);"
        );
    }

    private void addMemberTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.MemberEntry.TABLE_NAME + " (" +
                        InfoGotContract.MemberEntry.COLUMN_IDH+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE, "+
                        InfoGotContract.MemberEntry.COLUMN_IDC + " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.CharacterEntry.TABLE_NAME+"("+InfoGotContract.CharacterEntry.COLUMN_IDC+ ") ON DELETE CASCADE);"
        );
    }

    private void addHouseTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.HouseEntry.TABLE_NAME + " (" +
                        InfoGotContract.HouseEntry.COLUMN_IDH + " INTEGER PRIMARY KEY, " +
                        InfoGotContract.HouseEntry.COLUMN_NAME + " TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_REGION + " TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_WORDS + " TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_FOUDED + "TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_DIED + "TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_COATOFARMS + "TEXT, " +
                        InfoGotContract.HouseEntry.COLUMN_OVERLORD + "INTEGER, " +
                        InfoGotContract.HouseEntry.COLUMN_LORD + "INTEGER, " +
                        InfoGotContract.HouseEntry.COLUMN_HEIR + "INTEGER, " +
                        InfoGotContract.HouseEntry.COLUMN_FOUNDER + "INTEGER, " +
                        "FOREIGN KEY (" + InfoGotContract.HouseEntry.COLUMN_OVERLORD+ ") " +
                        "REFERENCES " + InfoGotContract.HouseEntry.TABLE_NAME + " (" + InfoGotContract.HouseEntry.COLUMN_IDH+ ")," +
                        "FOREIGN KEY (" + InfoGotContract.HouseEntry.COLUMN_LORD+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ ")," +
                        "FOREIGN KEY (" + InfoGotContract.HouseEntry.COLUMN_HEIR+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ ")," +
                        "FOREIGN KEY (" + InfoGotContract.HouseEntry.COLUMN_FOUNDER+ ") " +
                        "REFERENCES " + InfoGotContract.CharacterEntry.TABLE_NAME + " (" + InfoGotContract.CharacterEntry.COLUMN_IDC+ "));"
        );
    }

    private void addHouseTitlesTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.HouseTitleEntry.TABLE_NAME + " (" +
                        InfoGotContract.HouseTitleEntry.COLUMN_IDH+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE, "+
                        InfoGotContract.HouseTitleEntry.COLUMN_TITLE+ " TEXT PRIMARY KEY);"
        );
    }

    private void addSeatTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.SeatEntry.TABLE_NAME + " (" +
                        InfoGotContract.SeatEntry.COLUMN_IDH+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE, "+
                        InfoGotContract.SeatEntry.COLUMN_SEAT+ " TEXT PRIMARY KEY);"
        );
    }

    private void addAncestralWeaponTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.AncestralWeaponEntry.TABLE_NAME + " (" +
                        InfoGotContract.AncestralWeaponEntry.COLUMN_IDH+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE, "+
                        InfoGotContract.AncestralWeaponEntry.COLUMN_WEAPON+ " TEXT PRIMARY KEY);"
        );
    }

    private void addBranchTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + InfoGotContract.BranchEntry.TABLE_NAME + " (" +
                        InfoGotContract.BranchEntry.COLUMN_IDH+ " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE, "+
                        InfoGotContract.BranchEntry.COLUMN_IDHB + " INTEGER PRIMARY KEY " +
                        "REFERENCES " +InfoGotContract.HouseEntry.TABLE_NAME+"("+InfoGotContract.HouseEntry.COLUMN_IDH+ ") ON DELETE CASCADE);"
        );
    }
}