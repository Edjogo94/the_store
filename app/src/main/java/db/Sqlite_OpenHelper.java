package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.thestore.Product;
import com.thestore.R;

import java.util.ArrayList;
import java.util.List;

public class Sqlite_OpenHelper extends SQLiteOpenHelper {

    private Context context;

    public Sqlite_OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userTableName = context.getResources().getString(R.string.user_table);
        String nombreUsuarioColumn = context.getResources().getString(R.string.c_nombre_usuario);
        String emailColumn = context.getResources().getString(R.string.c_email);
        String nombreTiendaColumn = context.getResources().getString(R.string.c_nombre_tienda);
        String descripcionTiendaColumn = context.getResources().getString(R.string.c_descripcion_tienda);
        String contrasenaColumn = context.getResources().getString(R.string.c_contrasena);
        String createTableQuery = "CREATE TABLE " + userTableName + " (" + nombreUsuarioColumn + " TEXT NOT NULL, " + emailColumn + " TEXT UNIQUE NOT NULL, " + nombreTiendaColumn + " TEXT NOT NULL, " + descripcionTiendaColumn + " TEXT NOT NULL, " + contrasenaColumn + " TEXT NOT NULL" + ");";
        db.execSQL(createTableQuery);

        String productTableName = context.getResources().getString(R.string.products_table);
        String nombreProductoField = context.getResources().getString(R.string.c_nombre_producto);
        String descripcionField = context.getResources().getString(R.string.c_descripcion_producto);
        String categoriaField = context.getResources().getString(R.string.c_categoria);
        String marcaField = context.getResources().getString(R.string.c_marca);
        String inventarioField = context.getResources().getString(R.string.c_inventario);
        String precioField = context.getResources().getString(R.string.c_precio);
        String imagenUrlField = context.getResources().getString(R.string.c_imagen_url);

        String createProductTableQuery = "CREATE TABLE " + productTableName + " (" + nombreProductoField + " TEXT NOT NULL, " + descripcionField + " TEXT NOT NULL, " + categoriaField + " TEXT NOT NULL, " + marcaField + " TEXT NOT NULL, " + inventarioField + " INTEGER CHECK(" + inventarioField + " > 0), " + precioField + " REAL CHECK(" + precioField + " > 0), " + imagenUrlField + " TEXT" + ");";
        db.execSQL(createProductTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertUser(String nombreUsuario, String email, String nombreTienda, String descripcionTienda, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(context.getResources().getString(R.string.c_nombre_usuario), nombreUsuario);
        values.put(context.getResources().getString(R.string.c_email), email);
        values.put(context.getResources().getString(R.string.c_nombre_tienda), nombreTienda);
        values.put(context.getResources().getString(R.string.c_descripcion_tienda), descripcionTienda);
        values.put(context.getResources().getString(R.string.c_contrasena), contrasena);
        long result = db.insert(context.getResources().getString(R.string.user_table), null, values);
        db.close();
        return result;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {context.getResources().getString(R.string.c_email), context.getResources().getString(R.string.c_contrasena)};
        String selection = context.getResources().getString(R.string.c_email) + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(context.getResources().getString(R.string.user_table), columns, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public long insertProduct(String nombreProducto, String descripcion, String categoria, String marca, int inventario, double precio, String imagenUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(context.getResources().getString(R.string.c_nombre_producto), nombreProducto);
        values.put(context.getResources().getString(R.string.c_descripcion_producto), descripcion);
        values.put(context.getResources().getString(R.string.c_categoria), categoria);
        values.put(context.getResources().getString(R.string.c_marca), marca);
        values.put(context.getResources().getString(R.string.c_inventario), inventario);
        values.put(context.getResources().getString(R.string.c_precio), precio);
        values.put(context.getResources().getString(R.string.c_imagen_url), imagenUrl);
        long result = db.insert(context.getResources().getString(R.string.products_table), null, values);
        db.close();
        return result;
    }

    public List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + context.getResources().getString(R.string.products_table), null);

        if (cursor.moveToFirst()) {
            do {
                int nombreProductoIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_nombre_producto));
                String nombreProducto = (nombreProductoIndex >= 0) ? cursor.getString(nombreProductoIndex) : "";

                int descripcionIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_descripcion_producto));
                String descripcion = (descripcionIndex >= 0) ? cursor.getString(descripcionIndex) : "";

                int categoriaIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_categoria));
                String categoria = (categoriaIndex >= 0) ? cursor.getString(categoriaIndex) : "";

                int marcaIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_marca));
                String marca = (marcaIndex >= 0) ? cursor.getString(marcaIndex) : "";

                int inventarioIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_inventario));
                int inventario = (inventarioIndex >= 0) ? cursor.getInt(inventarioIndex) : 0;

                int precioIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_precio));
                double precio = (precioIndex >= 0) ? cursor.getDouble(precioIndex) : 0.0;

                int imagenNombreIndex = cursor.getColumnIndex(context.getResources().getString(R.string.c_imagen_url));
                String imagenNombre = (imagenNombreIndex >= 0) ? cursor.getString(imagenNombreIndex) : "";

                Product product = new Product(nombreProducto, descripcion, categoria, marca, inventario, precio, imagenNombre);
                productList.add(product);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }


    public int updateProduct(String nombreProducto, int nuevoInventario, double nuevoPrecio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(context.getResources().getString(R.string.c_inventario), nuevoInventario);
        values.put(context.getResources().getString(R.string.c_precio), nuevoPrecio);
        String whereClause = context.getResources().getString(R.string.c_nombre_producto) + "=?";
        String[] whereArgs = {nombreProducto};
        int rowsAffected = db.update(context.getResources().getString(R.string.products_table), values, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }

    public int deleteProduct(String nombreProducto) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = context.getResources().getString(R.string.c_nombre_producto) + "=?";
        String[] whereArgs = {nombreProducto};
        int rowsDeleted = db.delete(context.getResources().getString(R.string.products_table), whereClause, whereArgs);
        db.close();
        return rowsDeleted;
    }

    public boolean login(String email, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {context.getResources().getString(R.string.c_email)};
        String selection = context.getResources().getString(R.string.c_email) + "=? AND " + context.getResources().getString(R.string.c_contrasena) + "=?";
        String[] selectionArgs = {email, contrasena};
        Cursor cursor = db.query(context.getResources().getString(R.string.user_table), columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
}
