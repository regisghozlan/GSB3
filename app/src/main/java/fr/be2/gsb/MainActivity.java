package fr.be2.gsb;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Execute l'action des différents boutons de la page d'accueil
     * @param view
     */
    public void onClick (View view) {
        if (view.getId() == R.id.btnfraisforfait){
            Intent intent = new Intent(getApplicationContext(), Frais_Forfait.class); //intent est une variable
            // de type intent(classe) pr faire passer d'1 classe à l'autre
            startActivity(intent);
        }

        if (view.getId() == R.id.btnfraishorsforfait){
            Intent intent = new Intent(getApplicationContext(), Frais_Hors_Forfait.class);
            startActivity(intent);
        }


        if (view.getId() == R.id.btnsynthese){
            Intent intent = new Intent(getApplicationContext(), Synthese_Mois.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.btnparamtres){
            Intent intent = new Intent(getApplicationContext(), parametres.class);
            startActivity(intent);
        }


    }

    /**
     * Affiche un message comportant un titre et un contenu,  via une boite de dialogue
     * @param titre
     * @param message
     */
    public void afficherMessage(String titre, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this); //classe qui constuit une boite de dialogue
        builder.setCancelable(true); //pr que la boite de dialogue soit refermable
        builder.setTitle(titre);
        builder.setMessage(message);
        builder.show();

    }

}
