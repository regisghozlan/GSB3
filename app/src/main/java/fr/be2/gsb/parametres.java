package fr.be2.gsb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class parametres extends MainActivity {
    SQLHelper database; //variable qui permet d'acceder à la classe sqlhelperfraisforfait
EditText Nom1;
EditText Prenom1;
EditText CodeV1;
EditText URL1, MyPassword;
EditText Email1;
Button Valider1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        database= new SQLHelper(this);
        init();

    }

    public void init(){
        Cursor Param = database.fetch_parametres();
        Param.moveToFirst();
        //Param.getInt(Param.getColumnIndex("CodeV1"));
        CodeV1= findViewById(R.id.CODEV);
        CodeV1.setText(Param.getString(     Param.getColumnIndex("CODEV")));
        Nom1=findViewById(R.id.NOM);
        Nom1.setText(Param.getString(Param.getColumnIndex("NOM")));
        Prenom1=findViewById(R.id.PRENOM);
        Prenom1.setText(Param.getString(Param.getColumnIndex("PRENOM")));
        Email1=findViewById(R.id.EMAIL);
        Email1.setText(Param.getString(Param.getColumnIndex("EMAIL")));
        URL1=findViewById(R.id.URLSERVEUR);
        URL1.setText(Param.getString(Param.getColumnIndex("URLSERVEUR")));
        MyPassword=findViewById(R.id.Password);
        Valider1=findViewById(R.id.VALIDER);
        }

    public void MonClickParam(View v ) {
        switch (v.getId()) {
            case R.id.VALIDER:
                if (Nom1.getText().toString().trim().length() == 0 ||
                        CodeV1.getText().toString().length() == 0
                        || Email1.getText().toString().trim().length()==0 ||
                Prenom1.getText().toString().trim().length() == 0){
                    //teste si le champ quantite est renseigné ou si le champ type n'est pas vide
                    // et qu'on a selectionne l'une des 4 possibilités et si la date est renseignée
                    afficherMessage("Erreur!", "Champ vide" + database.sha1Hash("",CodeV1.getText().toString()));
                    return;

                } else {
                    Integer CodeV = Integer.parseInt(String.valueOf(CodeV1.getText()));
                    String Nom = Nom1.getText().toString();
                    String Email = Email1.getText().toString();
                    String Prenom = Prenom1.getText().toString();
                    String URL = URL1.getText().toString();
                    String Password= MyPassword.getText().toString();
                    if (database.update_parametre(CodeV,Nom, Prenom,Email,URL,Password)) {
                        afficherMessage("Succès", "Parametres modifiés");
                        return;
                    }
                }
                break;
        }
    }

}