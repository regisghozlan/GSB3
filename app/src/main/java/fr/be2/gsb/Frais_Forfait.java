package fr.be2.gsb;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class Frais_Forfait extends MainActivity {

    //declaration des variables
    EditText txtQte1;
    Spinner listeForfait1;
    Button btnAjouter1;
    SQLHelper database; //variable qui permet d'acceder à la classe sqlhelperfraisforfait
    // pr pouvoir acceder a ses methodes
    //declaration d'un tableau avec les montants des frais au forfait
   // String[] Valeurs = getResources().getStringArray(R.array.ValeurForfait)
    String[] Valeurs;
//    Float[] FraisAuForfait=new Float [] {0.0, 110.0, 0.62, 80.0, 25.0};
    TextView maDate;
    TextView id;
    TextView dateActu;
    DatePickerDialog picker;
    Calendar calendrier = Calendar.getInstance(); //on declare une classe d'un calendrier qui existe deja
    int jj=calendrier.get(Calendar.DAY_OF_MONTH);
    int mm=calendrier.get(Calendar.MONTH);
    int aaaa=calendrier.get(Calendar.YEAR);
    double montantCalcule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frais_forfait);
        //on appelle la methode init
        init();
        //on initialise la variable database en instanciant la classe SQLHelper
        database= new SQLHelper(this);

    }
    public static Float[] convertStringArraytoFloatArray(String[] sarray) {
        Float[] intarray = null;/*from   w ww . ja va 2 s  .  c  o  m*/

        if (sarray != null) {
            intarray = new Float[sarray.length];

            try {
                for (int i = 0; i < sarray.length; i++) {
                    intarray[i] = Float.parseFloat(sarray[i]);
                }
            } catch (NumberFormatException e) {

            }
        }

        return intarray;
    }
    /**
     * Initialise les variables pr les relier aux objets graphiques correspondants
     **/
    public void init(){
        txtQte1=findViewById(R.id.txtQte);
        listeForfait1=findViewById(R.id.listeForfait);
        btnAjouter1=findViewById(R.id.btnAjouter);
        maDate=findViewById(R.id.datefrais);
        id = findViewById(R.id.idFrais);
        dateActu=findViewById(R.id.dateActu);
        Valeurs = getResources().getStringArray(R.array.ValeurForfait);
    }

    /**
     * Affiche le calendrier comportant les dates mises à jour
     * @param vv
     */
    public void ShowCal(View vv)
    {
        picker = new DatePickerDialog(Frais_Forfait.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        maDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        //date qu'on recupere une fois qu'on a selectionne
                    }
                },aaaa, mm, jj);//date qui s'affiche sur le calendrier
        picker.show();
    }

    /**
     * Effectue l'action du bouton ajouter, cad vérifie que les champs soient renseiegnés et valides
     * et enregistre le frais dans la bdd en affichant un message de confirmation
     * @param v
     */
    public void MonClick(View v ) {
        switch (v.getId()) {
            case R.id.btnAjouter:
                if (txtQte1.getText().toString().trim().length() == 0 || listeForfait1.getSelectedItem().toString().length() == 0
                        || maDate.getText().toString().trim().length()==0) {
                    //teste si le champ quantite est renseigné ou si le champ type n'est pas vide
                    // et qu'on a selectionne l'une des 4 possibilités et si la date est renseignée
                    afficherMessage("Erreur!", "Champ vide");
                    return;
                } else if (maDate.getText().toString().trim().length()>10 || maDate.getText().toString().trim().length()<8 ) {
                    //test sur la validité du champ date
                    afficherMessage("Erreur!", "Date invalide");
                    return;
                } else if (Integer.parseInt(txtQte1.getText().toString())<1){ //teste si la quantite est au moins 1
                    afficherMessage("Erreur!", "Quantité invalide");
                    return;
                } else {
                    Integer quantite = Integer.parseInt(String.valueOf(txtQte1.getText()));
                    String forfait = listeForfait1.getSelectedItem().toString();
                    int posForfait = listeForfait1.getSelectedItemPosition();
                    montantCalcule = quantite * Float.parseFloat(Valeurs[posForfait]);
                    if (database.insertData(forfait, quantite, null, montantCalcule, forfait)) {
                        afficherMessage("Succès", "Valeur ajoutée. " + "Montant= " + montantCalcule);
                        return;
                    }
                }
                break;
        }
    }

    /**
     * Finit l'action en cours, donc retourne à l'action précédente
     * @param view
     */
    public void clique_retour(View view) {
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}