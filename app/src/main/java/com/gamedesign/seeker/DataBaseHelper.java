package com.gamedesign.seeker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;

import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zixiao on 11/1/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "games.db";

    // Table Names
    public static final String CLUE_TABLE_NAME = "clues_table"; // Clues table store all information needed for the game
    public static final String GAME_TABLE_NAME = "games_table"; // Game table store game name with responding game id
    public static final String GAME_CLUE_TABLE = "clue_game"; // Indicate clue belong to which game

    // Common column name
    private static final String KEY_ID = "_id";

    // GAME table column names
    public static final String GAME_COLUMN_ID = "_id";
    public static final String GAME_COLUMN_NAME = "game_name";

    // CLUE table column names
    public static final String CLUE_COLUMN_ID = "_id";
    public static final String CLUE_COLUMN_HINT = "hint";
    public static final String CLUE_COLUMN_IMAGE_PATH = "image_path";
    public static final String CLUE_COLUMN_SPOT_NAME = "name";
    public static final String CLUE_COLUMN_SPOT_ADDR = "address";
    public static final String CLUE_COLUMN_SPOT_ORDER = "number";
    public static final String CLUE_COLUMN_SPOT_LAT = "latitude";
    public static final String CLUE_COLUMN_SPOT_LONG = "longitude";

    // GAME_CLUE table column names
    private static final String GAME_ID = "game_id";
    private static final String CLUE_ID = "clue_id";

    // Table create statements
    // clue table create statement
    private static final String CLUE_TABLE_CREATE =
            "CREATE TABLE " + CLUE_TABLE_NAME + " ( " +
                    CLUE_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    CLUE_COLUMN_HINT + " TEXT," +
                    CLUE_COLUMN_IMAGE_PATH + " TEXT," +
                    CLUE_COLUMN_SPOT_NAME + " TEXT," +
                    CLUE_COLUMN_SPOT_ADDR + " TEXT," +
                    CLUE_COLUMN_SPOT_ORDER + " INTEGER," +
                    CLUE_COLUMN_SPOT_LAT + " TEXT," +
                    CLUE_COLUMN_SPOT_LONG + " TEXT" + ")";

    // game table create statement
    private static final String GAME_TABLE_CREATE =
            "CREATE TABLE " + GAME_TABLE_NAME + " ( " +
                    GAME_COLUMN_ID + " INTEGER PRIMARY KEY," +
                    GAME_COLUMN_NAME + " TEXT" + ")";

    // game to clue table create statement
    private static final String GAME_CLUE_TABLE_CREATE =
            "CREATE TABLE " + GAME_CLUE_TABLE + " ( " +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    CLUE_ID + " INTEGER," +
                    GAME_ID + " INTEGER" + ")";



    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CLUE_TABLE_CREATE);
        db.execSQL(GAME_TABLE_CREATE);
        db.execSQL(GAME_CLUE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts" + GAME_CLUE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS contacts" + CLUE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS contacts" + GAME_TABLE_NAME);
        onCreate(db);
    }

    // ========= CLUE table methods =============================================================
    public long insertClue (Clue clue, long game_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLUE_COLUMN_HINT, clue.getHint());
        values.put(CLUE_COLUMN_IMAGE_PATH, clue.getImage_path());
        values.put(CLUE_COLUMN_SPOT_NAME, clue.getSpot_name());
        values.put(CLUE_COLUMN_SPOT_ADDR, clue.getSpot_addr());
        values.put(CLUE_COLUMN_SPOT_ORDER, clue.getSpot_order());
        values.put(CLUE_COLUMN_SPOT_LAT, clue.getSpot_latitude());
        values.put(CLUE_COLUMN_SPOT_LONG, clue.getSpot_longitude());

        // build connection with its game
        long id = db.insert(CLUE_TABLE_NAME, null, values);
        createClueToGame(id, game_id);

        return id;
    }

    public int updateClue (Clue clue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLUE_COLUMN_HINT, clue.getHint());
        values.put(CLUE_COLUMN_IMAGE_PATH, clue.getImage_path());
        values.put(CLUE_COLUMN_SPOT_NAME, clue.getSpot_name());
        values.put(CLUE_COLUMN_SPOT_ADDR, clue.getSpot_addr());
        values.put(CLUE_COLUMN_SPOT_ORDER, clue.getSpot_order());
        values.put(CLUE_COLUMN_SPOT_LAT, clue.getSpot_latitude());
        values.put(CLUE_COLUMN_SPOT_LONG, clue.getSpot_longitude());

       return db.update(CLUE_TABLE_NAME, values, CLUE_COLUMN_ID + " = ?", new String[] {Integer.toString(clue.getId())});
    }

    public int deleteClue (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CLUE_TABLE_NAME, CLUE_COLUMN_ID + " = ?", new String[] { Integer.toString(id)});
    }

    public Clue getClue (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CLUE_TABLE_NAME + " WHERE " + CLUE_COLUMN_ID + " = " + id, null);

        if (cursor != null) cursor.moveToFirst();

        Clue c = new Clue ();
        c.setId(cursor.getInt(cursor.getColumnIndex(this.CLUE_COLUMN_ID)));
        c.setHint(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_HINT)));
        c.setImage_path(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_IMAGE_PATH)));
        c.setSpot_name(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_NAME)));
        c.setSpot_addr(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ADDR)));
        c.setSpot_order(cursor.getInt(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ORDER)));
        c.setSpot_latitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LAT)));
        c.setSpot_longitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LONG)));

        return c;
    }

    public List<Clue> getAllClues() {
        List<Clue> clues = new ArrayList<Clue>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                this.CLUE_TABLE_NAME,
                null, null, null, null, null, null);
        cursor.moveToFirst(); // redirect to the first entry

        while (cursor.isAfterLast() == false) {
            Clue c = new Clue();
            c.setHint(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_HINT)));
            c.setImage_path(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_IMAGE_PATH)));
            c.setSpot_name(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_NAME)));
            c.setSpot_addr(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ADDR)));
            c.setSpot_order(cursor.getInt(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ORDER)));
            c.setSpot_latitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LAT)));
            c.setSpot_longitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LONG)));

            clues.add(c);
            cursor.moveToNext();
        }

        return clues;
    }

    public List<Clue> getAllCluesByGame (String game_name) {
        List<Clue> clues = new ArrayList<Clue>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " +
                CLUE_TABLE_NAME + " ct, " +
                GAME_TABLE_NAME + " gt, " +
                GAME_CLUE_TABLE + " gct WHERE " +
                "gt." + GAME_COLUMN_NAME + " = " + game_name +
                "AND gct." + GAME_ID + " = " + "gt." + GAME_COLUMN_ID +
                "AND gct." + CLUE_ID + " = =" + "ct." + CLUE_COLUMN_ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst(); // redirect to the first entry

        while (cursor.isAfterLast() == false) {
            Clue c = new Clue();
            c.setId(cursor.getInt(cursor.getColumnIndex(this.CLUE_COLUMN_ID)));
            c.setHint(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_HINT)));
            c.setImage_path(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_IMAGE_PATH)));
            c.setSpot_name(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_NAME)));
            c.setSpot_addr(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ADDR)));
            c.setSpot_order(cursor.getInt(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_ORDER)));
            c.setSpot_latitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LAT)));
            c.setSpot_longitude(cursor.getString(cursor.getColumnIndex(this.CLUE_COLUMN_SPOT_LONG)));

            clues.add(c);
            cursor.moveToNext();
        }

        return clues;
    }

    public int getCluesCount () {
        SQLiteDatabase db = this.getReadableDatabase();
        int num = (int) DatabaseUtils.queryNumEntries(db, CLUE_TABLE_NAME);
        return num;
    }

    // ========= GAME table methods =============================================================
    public long insertGame(Game g) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_COLUMN_NAME, g.getGame_name());

        // build connection with its game
        long id = db.insert(GAME_TABLE_NAME, null, values);

        return id;
    }

    public Game getGame (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + GAME_TABLE_NAME + " WHERE " + CLUE_COLUMN_ID + " = " + id, null);

        if (cursor != null) cursor.moveToFirst();

        Game g = new Game ();
        g.setId(cursor.getInt(cursor.getColumnIndex(this.GAME_COLUMN_ID)));
        g.setGame_name(cursor.getString(cursor.getColumnIndex(this.GAME_COLUMN_NAME)));

        return g;
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<Game>();
        String selecQuery = "SELECT * FROM " + GAME_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selecQuery, null);

        try {
            if (cursor.moveToFirst()) // redirect to the first entry
            {
                do {
                    Game g = new Game();
                    g.setId(cursor.getInt(cursor.getColumnIndex(GAME_COLUMN_ID)));
                    g.setGame_name(cursor.getString(cursor.getColumnIndex(GAME_COLUMN_NAME)));

                    games.add(g);

                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return games;
    }

    public int updateGame (Game g) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_COLUMN_NAME, g.getGame_name());

        return db.update(GAME_TABLE_NAME, values, GAME_COLUMN_ID + " = ?", new String[] {Integer.toString(g.getId())});

    }

    public int deleteGame (Game g, boolean delete_all_clues) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (delete_all_clues) {
            List<Clue> clues = getAllCluesByGame(g.getGame_name());
            for (Clue c : clues) {
                deleteClue(c.getId());
            }
        }

        return db.delete(GAME_TABLE_NAME, GAME_COLUMN_ID + " = ?", new String[] { Integer.toString(g.getId())});
    }


    // ========= GAME_CLUE table methods =============================================================
    public long createClueToGame(long game_id, long clue_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GAME_ID, game_id);
        values.put(CLUE_ID, clue_id);

        return db.insert(GAME_CLUE_TABLE, null, values); // return id in game_clue table row
    }

    public int updateClueToGame (long game_id, long clue_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_ID, game_id);

        return db.update(GAME_CLUE_TABLE, values, KEY_ID + " = ?", new String[] {String.valueOf(clue_id)});

    }

    public void deleteClueToGame(long clue_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GAME_CLUE_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(clue_id) });
    }


    public void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) db.close();
    }
}

