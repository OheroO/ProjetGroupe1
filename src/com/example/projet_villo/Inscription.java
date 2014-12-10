package com.example.projet_villo;

import java.sql.Connection;
import java.util.ArrayList;

import myconnections.DBConnection;
import classesDB.ClientDB;

import com.example.projet_villo.Connexion.MyAccesDB;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Inscription extends ActionBarActivity {

	private Connection con = null;
	private EditText pseudo = null;
	private EditText password = null;
	private EditText email = null;
	private EditText nom = null;
	private EditText prenom = null;
	private Button inscription;
	private String resultat;
	private ClientDB cli = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		pseudo = (EditText) findViewById(R.id.editPseudo);
		password = (EditText) findViewById(R.id.editPassword);
		email = (EditText) findViewById(R.id.editEmail);
		nom = (EditText) findViewById(R.id.editNom);
		prenom = (EditText) findViewById(R.id.editPrenom);
		inscription = (Button) findViewById(R.id.buttonInscription);

	}

	public void gestInscription(View view) {
        ArrayList<String> erreurs = new ArrayList<String>();

        // Pseudo 
		String pseudoTemp = pseudo.getText().toString();
		if(!valide(pseudoTemp)) {
			erreurs.add("Pseudo vide"); // Message d'erreurs dans différentes langues !
		}
		// Password
		String passwordTemp = password.getText().toString();
		if(!valide(passwordTemp)) {
			erreurs.add("Mot de passe vide");
		}
		//Email
		String emailTemp = email.getText().toString();
		if(!valide(emailTemp)) {
			erreurs.add("Email vide");
		}
		else if (!emailTemp.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")) {
			erreurs.add("Email incorrect");
		}
		// Nom
		String nomTemp = nom.getText().toString();
		if(!valide(nomTemp)) {
			erreurs.add("Nom vide");
		} else if (!nomTemp.matches("\\p{L}*(-\\p{L}*)*")) {
			erreurs.add("Nom incorrect");
		}
		// Prenom
		String prenomTemp = prenom.getText().toString();
		if(!valide(prenomTemp)) {
			erreurs.add("Prenom vide");
		} else if (!prenomTemp.matches("\\p{L}*(-\\p{L}*)*")) {
			erreurs.add("Prenom incorrect");
		}

		if(erreurs.isEmpty()) {
			cli = new ClientDB(pseudoTemp, passwordTemp, emailTemp, nomTemp,prenomTemp);

			MyAccesDB adb = new MyAccesDB(Inscription.this);
			adb.execute();
		}
		else {
			Toast.makeText(getApplicationContext(), erreurs.toString(),Toast.LENGTH_SHORT).show(); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inscription, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean valide(String str) { // vérifie si le string est vide
        return str != null && !str.trim().isEmpty();
    }

	class MyAccesDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat;
		private ProgressDialog pgd = null;

		public MyAccesDB(Inscription pActivity) {

			link(pActivity);
		}

		private void link(Inscription pActivity) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(Inscription.this);
			pgd.setMessage("chargement en cours"); // Langue ?
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if (con == null) {
				con = new DBConnection().getConnection();
				if (con == null) {
					resultat = "Echec de la connexion";
					return false;
				}
				ClientDB.setConnection(con);
			}

			try {
				cli.create();
				resultat = "Inscription complète !"; // Changement de langue ?
			} catch (Exception e) {
				String erreur = e.getMessage().substring(0, 9);
				if(erreur.equals("ORA-00001")) {
					resultat = getResources().getString(R.string.contrainteUnique);
				}
				return false;
			}

			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			Toast.makeText(getApplicationContext(), resultat,Toast.LENGTH_SHORT).show();
			if (result) {
				Intent i = new Intent(Inscription.this,MenuPrincipal.class);
				i.putExtra(Connexion.IDCLIENT, cli.getIdclient());
				// On associe l'identifiant à notre intent
				startActivity(i);
			}
		}
	}
}
