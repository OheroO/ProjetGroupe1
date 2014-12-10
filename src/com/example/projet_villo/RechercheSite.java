package com.example.projet_villo;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.sql.Connection;
import java.util.ArrayList;

import com.example.projet_villo.Inscription.MyAccesDB;

import myconnections.DBConnection;
import classesDB.ClientDB;
import classesDB.EntiteDB;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RechercheSite extends ActionBarActivity {
	private String nomEntite ="";
	private EditText editRecherche;
	private Button bRecherche;
	private Connection con=null;
	public static final String NOMENTITE = "nomEntite";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recherchesite);
		
		editRecherche = (EditText) findViewById(R.id.editTextSite);
		bRecherche = (Button) findViewById(R.id.bRecherche);
		
	} 
	
	public void gestRechercheSite(View v) {
        ArrayList<String> erreurs = new ArrayList<String>();
        
		//NOMENTITE
        nomEntite = editRecherche.getText().toString(); 
		if(!valide(nomEntite)) {
			erreurs.add("Entité vide"); // langue ?
		}
		else if(!nomEntite.matches("\\p{L}*(-\\p{L}*)*")) {
			erreurs.add("Entité incorrect"); // langue ?
		}
		
		if(erreurs.isEmpty()) {
			MyAccesDB adb = new MyAccesDB(RechercheSite.this);
			adb.execute();
		}
		else {
			Toast.makeText(getApplicationContext(), erreurs.toString(),Toast.LENGTH_SHORT).show(); 
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reserver, menu);
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

		public MyAccesDB(RechercheSite pActivity) {

			link(pActivity);
		}

		private void link(RechercheSite pActivity) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(RechercheSite.this);
			pgd.setMessage("chargement en cours"); // langue ? 
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
				EntiteDB.setConnection(con);
			}
			
			try {
				EntiteDB entiteArray = EntiteDB.rechEntite(nomEntite);
			} catch (Exception e) {
				String erreur = e.getMessage().substring(0, 9);
				if(erreur.equals("ORA-99999"))  {
					resultat = "Entité inconnu"; // langue
				}
				
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			Toast.makeText(getApplicationContext(), resultat,Toast.LENGTH_SHORT).show();
			if(result) {
				Intent i = getIntent();
				int idClient = i.getIntExtra(Connexion.IDCLIENT, 0);
			    i = new Intent(RechercheSite.this,AfficheSite.class);
				i.putExtra(Connexion.IDCLIENT,idClient);
				i.putExtra(RechercheSite.NOMENTITE,nomEntite);
				startActivity(i);
			}
			
		}
	}
	
	
	
	
}

