package com.example.projet_villo;

import java.sql.Connection;
import java.util.ArrayList;

import myconnections.DBConnection;
import classesDB.ClientDB;

import com.example.projet_villo.Connexion;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Connexion extends Activity { // Vérifier si les editText sont vides !

	private Connection con = null;
	private EditText username = null;
	private EditText password = null;
	private Button login;
	private int flag=0;
	private MediaPlayer mPlayer = null;
	private String pseudoTemp="";
	private String passwordTemp="";
	public static final String IDCLIENT = "idClient";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
		username = (EditText) findViewById(R.id.editTextUser);
		password = (EditText) findViewById(R.id.editTextPass);
		login = (Button) findViewById(R.id.buttonLogin);
	}
	
	public void gestInscription(View view) {
		Intent i = new Intent(Connexion.this,Inscription.class);
		// On associe l'identifiant à notre intent
		startActivity(i);
	}

	public void gestLogin(View view) {
        ArrayList<String> erreurs = new ArrayList<String>();

        //PSEUDO
        pseudoTemp = username.getText().toString();
        if(!valide(pseudoTemp)) {
        	erreurs.add("Pseudo vide"); // gestion langue
        }
        //PASSWORD
		passwordTemp = password.getText().toString();
		if(!valide(passwordTemp)) {
			erreurs.add("Password vide"); // Gestion langue
		}
		
		if(erreurs.isEmpty()) {
			MyAccesDB adb = new MyAccesDB(Connexion.this);
			adb.execute();
		}else {
			Toast.makeText(getApplicationContext(), erreurs.toString(),Toast.LENGTH_SHORT).show(); 
		}
		
		playSound(R.raw.login);
	}

	private void playSound(int resId) {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}
		mPlayer = MediaPlayer.create(this, resId);
		mPlayer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean valide(String str) { // vérifie si le string est vide
        return str != null && !str.trim().isEmpty();
    }

	class MyAccesDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat;
		private ProgressDialog pgd = null;
		private ClientDB cl = new ClientDB();

		public MyAccesDB(Connexion pActivity) {

			link(pActivity);
		}

		private void link(Connexion pActivity) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(Connexion.this);
			pgd.setMessage("chargement en cours");
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
				
				cl = ClientDB.rechPseudo(pseudoTemp);
				cl.read();
				if (cl.getPassword().equals(passwordTemp)) {
					resultat = "Redirection..."; // Gestion langue ?
					flag = 1;
				} else {
					resultat = "Mot de passe incorrecte !"; // Gestion langue ?
				} 
			} catch (Exception e) {
				resultat = e.getMessage();
				return false;
			}

			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			Toast.makeText(getApplicationContext(), resultat,Toast.LENGTH_SHORT).show();
			if (flag == 1) {
				login.setBackgroundColor(Color.parseColor("#478A61"));
				Intent i = new Intent(Connexion.this,MenuPrincipal.class);
				i.putExtra(IDCLIENT, cl.getIdclient());
				// On associe l'identifiant à notre intent
				startActivity(i);
			} else {
				login.setBackgroundColor(Color.parseColor("#E74C3C"));
			}

		}
		

	}

}