package com.example.pizzaapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.widget.Toast
import com.example.pizzaapp.model.MenuModel
import java.io.ByteArrayOutputStream

class DatabaseHelper ( var context: Context, ): SQLiteOpenHelper(
    context,DATABASE_NAME, null,DATABASE_VERSION
){
    companion object {
    private val DATABASE_NAME = "pizza"
    private val DATABASE_VERSION = 1
    private val TABLE_ACCOUNT = "account"
    private val COLUMN_EMAIL = "email"
    private val COLUMN_NAME = "name"
    private val COLUMN_LEVEL = "level"
    private val COLUMN_PASSWORD = "password"
        private val TABLE_MENU = "menu"
        private val COLUMN_ID_MENU = "idMenu"
        private val COLUMN_NAMA_MENU = "menuName"
        private val COLUMN_PRICE_MENU = "price"
        private val COLUMN_IMAGE = "photo"
    }

    private val CREATE_MENU_TABLE = ("CREATE TABLE " + TABLE_MENU + "("
            + COLUMN_ID_MENU + " INT PRIMARY KEY, " + COLUMN_NAMA_MENU +" TEXT, "
            + COLUMN_PRICE_MENU + " INT, " + COLUMN_IMAGE +" BLOB);")


    private val DROP_MENU_TABLE = "DROP TABLE IF EXISTS $TABLE_MENU"


    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY, "+ COLUMN_NAME +" TEXT, "
            + COLUMN_LEVEL + " TEXT, "+ COLUMN_PASSWORD +" TEXT);")

    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
        p0?.execSQL(CREATE_MENU_TABLE)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        p0?.execSQL(DROP_MENU_TABLE)
        onCreate(p0)
    }

    fun checkLogin(email:String, password:String):Boolean{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(TABLE_ACCOUNT,
            colums,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        return cursorCount > 0

    }

    fun addAccount(email:String, name:String, level:String, password:String) {
        val db = this.readableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LEVEL, level)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_ACCOUNT, null, values)

        if (result == (0).toLong()) {
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Register Success, " +
            "Please Login Using your new account", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
    @SuppressLint("Range")
    fun checkData(email:String):String{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name :String = ""

        val cursor = db.query(TABLE_ACCOUNT,
        colums,
        selection,
        selectionArgs,
        null,
        null,
        null)

        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name

    }

    fun addMenu(menu:MenuModel) {
        val db = this.readableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID_MENU, menu.id)
        values.put(COLUMN_NAMA_MENU, menu.name)
        values.put(COLUMN_PRICE_MENU, menu.price)

        val byteOutputStream = ByteArrayOutputStream()
        val imageInByte:ByteArray
        menu.image.compress(Bitmap.CompressFormat.JPEG, 100,byteOutputStream)
        imageInByte = byteOutputStream.toByteArray()
        values.put(COLUMN_IMAGE, imageInByte)

        val result = db.insert(TABLE_MENU, null, values)

        if (result == (0).toLong()) {
            Toast.makeText(context, "Add menu failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Add menu Success", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
}