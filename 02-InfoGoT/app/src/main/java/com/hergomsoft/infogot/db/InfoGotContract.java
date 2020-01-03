package com.hergomsoft.infogot.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class InfoGotContract {

    public static final String CONTENT_AUTHORITY ="com.hergomsoft.imfogot.provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String PATH_BOOK = "book";
    public static final String PATH_APPEARANCE = "appearance";
    public static final String PATH_CHARACTER = "character";
    public static final String PATH_CHARACTERTITLE = "charactertitle";
    public static final String PATH_ALIAS = "alias";
    public static final String PATH_MEMBER = "member";
    public static final String PATH_HOUSE = "house";
    public static final String PATH_BRANCH = "branch";
    public static final String PATH_HOUSETITLE = "housetitle";
    public static final String PATH_SEAT = "seat";
    public static final String PATH_ANCESTRALWEAPON = "ancestralweapon";

    /**
     * Create one class for each table that handles all information regarding the table schema and
     * the URIs related to it.
     */
    public static final class BookEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_BOOK;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_BOOK;

        // Define the table schema
        public static final String TABLE_NAME = "book";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RELEASED= "released";
        public static final String COLUMN_NPAGES = "npages";
        public static final String COLUMN_IDB="idb";

        // Define a function to build a URI to find a specific BOOK by it's identifier
        public static Uri buildBookUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AppearanceEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_APPEARANCE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_APPEARANCE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_APPEARANCE;

        // Define the table schema
        public static final String TABLE_NAME = "Appearance";
        public static final String COLUMN_IDB = "idb";
        public static final String COLUMN_IDC = "idc";

        // Define a function to build a URI to find a specific Appearance by it's identifier
        public static Uri buildAppearanceUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CharacterEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHARACTER).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CHARACTER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CHARACTER;

        // Define the table schema
        public static final String TABLE_NAME = "character";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IDC= "idc";
        public static final String COLUMN_GENDER= "gender";
        public static final String COLUMN_CULTURE= "culture";
        public static final String COLUMN_BORN= "born";
        public static final String COLUMN_DIED= "died";
        public static final String COLUMN_SPOUSE= "spouse";
        public static final String COLUMN_FATHER= "father";
        public static final String COLUMN_MOTHER= "mother";

        // Define a function to build a URI to find a specific Character by it's identifier
        public static Uri buildCharacterUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CharacterTitleEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHARACTERTITLE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CHARACTERTITLE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CHARACTERTITLE;

        // Define the table schema
        public static final String TABLE_NAME = "charactertitle";
        public static final String COLUMN_IDC= "idc";
        public static final String COLUMN_TITLE= "title";

        // Define a function to build a URI to find a specific CharacterTitle by it's identifier
        public static Uri buildCharacterTitleUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AliasEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALIAS).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_ALIAS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ALIAS;

        // Define the table schema
        public static final String TABLE_NAME = "alias";
        public static final String COLUMN_IDC= "idc";
        public static final String COLUMN_ALIAS= "alias";

        // Define a function to build a URI to find a specific Alias by it's identifier
        public static Uri buildAliasUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class MemberEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMBER).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_MEMBER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MEMBER;

        // Define the table schema
        public static final String TABLE_NAME = "member";
        public static final String COLUMN_IDC= "idc";
        public static final String COLUMN_IDH= "idh";

        // Define a function to build a URI to find a specific Member by it's identifier
        public static Uri buildMemberUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class HouseEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_HOUSE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_HOUSE;

        // Define the table schema
        public static final String TABLE_NAME = "house";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IDH= "idh";
        public static final String COLUMN_REGION= "region";
        public static final String COLUMN_WORDS = "words";
        public static final String COLUMN_FOUDED= "founded";
        public static final String COLUMN_DIED= "died";
        public static final String COLUMN_COATOFARMS= "coatofarms";
        public static final String COLUMN_OVERLORD = "overlord";
        public static final String COLUMN_LORD= "lord";
        public static final String COLUMN_HEIR= "heir";
        public static final String COLUMN_FOUNDER= "founder";

        // Define a function to build a URI to find a specific House by it's identifier
        public static Uri buildHouseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class BranchEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANCH).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_BRANCH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_BRANCH;

        // Define the table schema
        public static final String TABLE_NAME = "branch";
        public static final String COLUMN_IDH= "idh";
        public static final String COLUMN_IDHB= "idhb";

        // Define a function to build a URI to find a specific Branch by it's identifier
        public static Uri buildBranchUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class HouseTitleEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSETITLE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_HOUSETITLE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_HOUSETITLE;

        // Define the table schema
        public static final String TABLE_NAME = "housetitle";
        public static final String COLUMN_IDH= "idh";
        public static final String COLUMN_TITLE= "title";

        // Define a function to build a URI to find a specific HouseTitle by it's identifier
        public static Uri buildHouseTitleUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class SeatEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEAT).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_SEAT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_SEAT;

        // Define the table schema
        public static final String TABLE_NAME = "seat";
        public static final String COLUMN_IDH= "idh";
        public static final String COLUMN_SEAT= "seat";

        // Define a function to build a URI to find a specific Seat by it's identifier
        public static Uri buildSeatUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class AncestralWeaponEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ANCESTRALWEAPON).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_ANCESTRALWEAPON;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ANCESTRALWEAPON;

        // Define the table schema
        public static final String TABLE_NAME = "ancestralweapon";
        public static final String COLUMN_IDH= "idh";
        public static final String COLUMN_WEAPON= "weapon";

        // Define a function to build a URI to find a specific AncestralWeapon by it's identifier
        public static Uri buildAncestralWeaponUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
