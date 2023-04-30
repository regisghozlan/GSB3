package fr.be2.gsb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SQLHelper extends SQLiteOpenHelper {
    //declaration des variables
    public static final String DB_NAME = "GSB.db";
    public static final String DB_TABLE = "FRAIS";
    public static final String ID = "ID"; //ce sera les colonnes de la table frais
    public static final String TYPE = "TYPE";
    public static final String QUANTITE = "QUANTITE";
    public static final String DATE = "DATE";
    public static final String MONTANT = "MONTANT";
    public static final String DATEACTU = "DATEACTU";
    public static final String LIBELLE = "LIBELLE";

    /**
     * Crée une table par une requete SQL
     */
    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE + " TEXT," + QUANTITE + " INTEGER," + DATE
            + " TEXT," + MONTANT + " REAL," + LIBELLE + " TEXT," + DATEACTU + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String CREATE_PARAMETRE = "CREATE TABLE PARAMETRES (ID INTEGER PRIMARY KEY,CODEV TEXT,NOM TEXT,PRENOM TEXT, EMAIL TEXT,PASSWORD TEXT, URLSERVEUR TEXT)";
    private static final String INIT_PARAMETRE = "INSERT INTO PARAMETRES (ID ,CODEV,NOM,PRENOM, EMAIL, URLSERVEUR ) VALUES(1,0,'','','@','https://')";

    /**
     *
     * @param context
     */
    public SQLHelper(Context context) {

        super(context, DB_NAME, null, 1);//permet d'acceder aux membres de la classe mère

    }


    /**
     * constructeur de la classe
     * methode venant de SQLLite et permettant d'executer une requete SQL
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_PARAMETRE);
        sqLiteDatabase.execSQL(INIT_PARAMETRE);


    }

    /**
     * destructeur de la classe
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Insère dans le BDD les données type, quantité, date, montant et libellé renseignées par le visiteur
     * @param type
     * @param quantite
     * @param date
     * @param montant
     * @param libelle
     * @return booleen
     */
    public boolean insertData(String type, Integer quantite, String date, double montant, String libelle) {
        //on cree une variable de type sqLitedatabase pr pouvoir y acceder
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE, type);
        contentValues.put(QUANTITE, quantite);
        contentValues.put(DATE, date);
        contentValues.put(MONTANT, montant);
        contentValues.put(LIBELLE, libelle);
        //insert sert a inserer des donnees, elle insere ds notre table contentValue les contenus
        // des variables que l'utilisateur renseigne
        long result = db.insert(DB_TABLE, null, contentValues);
        return result != -1;
    }

    public void init_parametre() {
        //on cree une variable de type sqLitedatabase pr pouvoir y acceder
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        contentValues.put("CODEV", 0);
        contentValues.put("NOM", "");
        contentValues.put("PRENOM", "");
        contentValues.put("EMAIL", "");
        contentValues.put("URLSERVEUR", "");
        long result = db.insert("PARAMETRES", null, contentValues);
      //  return result != -1;
        return;
    }
    public boolean update_parametre(Integer CodeV, String Nom, String Prenom, String Email, String URL, String MyPassword ) {
        //on cree une variable de type sqLitedatabase pr pouvoir y acceder
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CODEV",CodeV);
        contentValues.put("NOM", Nom);
        contentValues.put("PRENOM", Prenom);
        contentValues.put("EMAIL", Email);
        contentValues.put("URLSERVEUR", URL);
        if (MyPassword.length()>0)
        {
            contentValues.put("PASSWORD", sha1Hash(MyPassword.toString(),CodeV.toString()));

        }
        long result = db.update("PARAMETRES",contentValues,"ID=1",null);
        return result != -1;

    }


    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + DB_TABLE;
        //cursor: type, pointeur: pr parcourir les lignes ds les resultats de la requete. Null car pas de where
        Cursor pointeur = db.rawQuery(query, null);
        return pointeur;

    }

    /**
     * Supprime un frais ayant pour id l'id passé en paramètre
     * @param ID
     * @return booleen
     */
    public boolean deleteData(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(DB_TABLE, "ID=" + ID, null);

        return result != -1;

    }

    /**
     * Interroge la BDD et retourne tous les frais enregistrés
     * @return le curseur contenant les frais
     */
    public Cursor fetchAllFrais() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor Cursor = db.query(DB_TABLE, new String[]{"rowid _id",LIBELLE,
                        ID, DATE, DATEACTU, MONTANT, QUANTITE},
                null, null, null, null, null);

        if (Cursor != null) {
            Cursor.moveToFirst();
        }
        return Cursor;
    }
    public Cursor fetchFrais(String filtre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor Cursor = db.query(DB_TABLE, new String[]{"rowid _id",LIBELLE,
                        ID, DATE, DATEACTU, MONTANT, QUANTITE},
                filtre, null, null, null, null);

        if (Cursor != null) {
            Cursor.moveToFirst();
        }
        return Cursor;
    }

    public Cursor fetch_parametres() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor Cursor = db.rawQuery("Select * from PARAMETRES",null,null);

        return Cursor;
    }

    public SQLHelper open() throws SQLException {

        SQLiteDatabase db =this.getWritableDatabase();
        return this;

    }

    String sha1Hash( String chaine, String cle )
    {
        String hash = null;
        String toHash= chaine + cle;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            //This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );

        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }

}
