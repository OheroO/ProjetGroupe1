package com.example.projet_villo;

import java.sql.Connection;
import java.util.ArrayList;

import com.example.projet_villo.Connexion.MyAccesDB;

import myconnections.DBConnection;
import classesDB.SiteDB;
import JavaClass.SiteAdapter;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class AfficheSite extends ActionBarActivity {
	private SiteDB s = null;
	private Connection con = null;
	public static final String IDSITE = "idSite";

	private ArrayList<SiteDB> listS = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_site);
		
		MyAccesDB adb = new MyAccesDB(AfficheSite.this);
		adb.execute();
		
	
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_site, menu);
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
	
	class MyAccesDB extends AsyncTask<String, Integer, Boolean> {
		private ProgressDialog pgd = null;
		private String resultat;

		public MyAccesDB(AfficheSite pActivity) {

			link(pActivity);
		}

		private void link(AfficheSite pActivity) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(AfficheSite.this);
			pgd.setMessage("chargement en cours");
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) { // ON PEUT PAS EDITER

			if (con == null) {// premier invocation
				con = new DBConnection().getConnection();
				if (con == null) {
					resultat = "Echec de la connexion";
					return false;
				}
				SiteDB.setConnection(con);
			}
		
			Intent i = getIntent();
			String entiteRech = i.getStringExtra(RechercheSite.NOMENTITE); // Recupère le nom de l'entité
			try {
				listS = SiteDB.rechSites(entiteRech); // Récupère les sites de cette entité
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			
			//Création et initialisation de l'Adapter pour les Sites
		    SiteAdapter adapter = new SiteAdapter(getApplicationContext(), listS);
		    
		    //Récupération du composant ListView
		    ListView list = (ListView)findViewById(R.id.ListView01);
		    
		    list.setAdapter(adapter);
		    
		    list.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					Intent i = getIntent();
					int idClient = i.getIntExtra(Connexion.IDCLIENT, 0);
					SiteDB siteClick=(SiteDB) arg0.getItemAtPosition(arg2);
					int idSite = siteClick.getIdSite();
					i = new Intent(AfficheSite.this,Reservation.class);
					i.putExtra(Connexion.IDCLIENT, idClient);
					i.putExtra(AfficheSite.IDSITE, idSite);
					startActivity(i);
				}

				
			}
					
					);
		}
}
}
