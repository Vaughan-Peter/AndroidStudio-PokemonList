package com.codelab.basics;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "DB_Name.db";

    // If you change the database schema, you must increment the database version.
    private static final String TABLE_NAME = "pokemon";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String _ID = "_ID";
    private static final String _COL_1 = "str_col";
    private static final String _COL_2 = "num_col";
    private static final String link = "link_col";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR(256), " +
                    "pokedex INTEGER, moves VARCHAR(256), level INTERGER, access_count INTERGER, links VARCHAR(500), image_col BLOB)";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void buildDB(){
        SQLiteDatabase db = getWritableDatabase();
        if(!isTable()){
            db.execSQL(SQL_CREATE_TABLE);
            createTableRows(db);
        }
    }

    private boolean isTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.query(true,
                "sqlite_master",
                new String[]{"name"},
                "type=? AND name=?",
                new String[]{"table", TABLE_NAME},
                null, null, null,
                "1");
        return (c.getCount() > 0);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBClass", "DB onCreate() " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
        Log.d("DBClass", "DB onCreate()");
        createTableRows(db);
    }

    public void createTableRows(SQLiteDatabase db){
        byte[] image = null;
        ContentValues values = new ContentValues();
        values.put("name", "Mewtwo");
        values.put("pokedex", 150);
        values.put("moves", "Shadow Punch, Psychic, Shadow Ball, Shadow Rush");
        values.put("level", 92);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/mewtwo.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Arceus");
        values.put("pokedex", 493);
        values.put("moves", "Rend, Psychic, Shadow Ball, Dynamic Punch");
        values.put("level", 72);
        values.put("access_count", 1);
        values.put("links","https://img.pokemondb.net/artwork/large/arceus.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Rayquaza");
        values.put("pokedex", 384);
        values.put("moves", "Ascend, Earthquake, Fly, Fire Blast");
        values.put("level", 84);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/rayquaza.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Groundon");
        values.put("pokedex", 383);
        values.put("moves", "Rollout, Earthquake, Flamethrower, Fire Blast");
        values.put("level", 64);
        values.put("access_count", 0);
        values.put("links", "https://img.pokemondb.net/artwork/large/groudon.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Kyogre");
        values.put("pokedex", 382);
        values.put("moves", "Body Slam, Surf, Thunder, Hydro Pump");
        values.put("level", 100);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/kyogre.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Ho-oh");
        values.put("pokedex", 250);
        values.put("moves", "Sacred Fire, Flamethrower, Fly, Fire Blast");
        values.put("level", 40);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/ho-oh.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Eevee");
        values.put("pokedex", 133);
        values.put("moves","Charm, Confuse Ray, Double Slap, Agility");
        values.put("level", 5);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/eevee.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);

        values.put("name", "Celebi");
        values.put("pokedex", 251);
        values.put("moves", "Metronome, Physic, Leaf Blade, Double Slap");
        values.put("level", 57);
        values.put("access_count", 0);
        values.put("links","https://img.pokemondb.net/artwork/large/celebi.jpg");
        values.put("image_col", image);
        db.insert(TABLE_NAME, null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.d("DBClass", "DB onUpgrade() to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void deleteDataTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_TABLE);
    }

    @Override
    public int count() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.v("DBClass", "getCount=" + cnt);
        return cnt;
    }

    public void accessCount(int value, long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("access_count", value+1);
        db.update(TABLE_NAME, values, "id = ?", new String[]{Long.toString(id)});
        Log.v("UPDATE","Update count of pokemon\n\n\n");
    }

    @Override
    public int save(DataModel dataModel) {
        Log.v("DBClass", "add=>  " + dataModel.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", dataModel.getName());
        values.put("pokedex", dataModel.getPokedex());
        values.put("links", dataModel.getGetLink());
        values.put("image_col", dataModel.getImage_col());
        //insert and close
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();

        // debug output to see what we're doing
        dump();

        return 0;
    }

    @Override
    public int update(DataModel dataModel) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    private Random r = new Random();

    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<DataModel>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build and add it to list
        DataModel item;
        if (cursor.moveToFirst()) {
            do {
                //maybe change last night to string
                item = new DataModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getBlob(7));
                temp.add(item);
            } while (cursor.moveToNext());
        }

        Log.v("DBClass", "Find All This => " + temp.toString());
        return temp;
    }

    @Override
    public String getNameById(Long id) {
        return null;
    }

    // Dump the DB as a test
    private void dump() {
        Log.v("DBClass", "HELLO TRASH PEOPLE!!\n\n\n");
    }  // oops, never got around to this...but find-all is dump-ish

    public int findFavourite(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor C = db.rawQuery("SELECT id, access_count FROM "+ TABLE_NAME +" ORDER BY access_count DESC LIMIT 1", null);
        C.moveToFirst();
        //C.close();
        return C.getInt(0);
        //return 6;
    }

    public void updateImage(String name, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_col", image);
        db.update(TABLE_NAME, values, "name" + " = ?", new String[]{name});
    }

    public byte[] findImage(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imageBytes = null;
        String query = "SELECT image_col FROM " + TABLE_NAME + " WHERE name = ?";
        Cursor cursor = null;

        cursor = db.rawQuery(query, new String[]{name});
        if (cursor != null && cursor.moveToFirst()){
            imageBytes = cursor.getBlob(0);
        }
        if(cursor != null){
            cursor.close();
        }

        return imageBytes;
    }
}