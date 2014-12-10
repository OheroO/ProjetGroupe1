package com.example.projet_villo;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import com.example.projet_villo.Connexion.MyAccesDB;

import myconnections.DBConnection;
import classesDB.ClientDB;
import classesDB.ReservationDB;
import classesDB.SiteDB;
import classesDB.VeloDB;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Reservation extends ActionBarActivity {
	private Connection con = null;
	private EditText nombreVelo = null;
	private int nombreVeloVoulu = 0;
	private Button reserver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);
		
		nombreVelo = (EditText) findViewById(R.id.editTextNombreVelo);
		reserver = (Button) findViewById(R.id.reserver);
	}
	
	public void gestReserver(View v) {
        ArrayList<String> erreurs = new ArrayList<String>();

        if(!nombreVelo.getText().toString().matches("[0-9]")) {
			erreurs.add("Valeur incorrect"); 
		 } else {
			 try {
				 nombreVeloVoulu = Integer.parseInt(nombreVelo.getText().toString());
				 } catch(NumberFormatException nfe) {
					 erreurs.add("Erreur parsing");
				}
		 }
		
        if(erreurs.isEmpty()) {
        	MyAccesDB adb = new MyAccesDB(Reservation.this);
    		adb.execute();
        }
        else {
			Toast.makeText(getApplicationContext(), erreurs.toString(),Toast.LENGTH_SHORT).show(); 
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reservation, menu);
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
		private String resultat;
		private ProgressDialog pgd = null;

		public MyAccesDB(Reservation pActivity) {
			link(pActivity);
		}

		private void link(Reservation pActivity) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(Reservation.this);
			pgd.setMessage("chargement en cours"); // Langue
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			if (con == null) {
				con = new DBConnection().getConnection();
				if (con == null) {
					resultat = "Echec de la connexion"; // Langue
					return false;
				}
				ClientDB.setConnection(con);
				SiteDB.setConnection(con);
				VeloDB.setConnection(con);
				ReservationDB.setConnection(con);
			}
			
			ArrayList<VeloDB> veloDispo = new ArrayList<VeloDB>();
			
			Intent i = getIntent();
			int idClient = i.getIntExtra(Connexion.IDCLIENT, 0);
			int idSite = i.getIntExtra(AfficheSite.IDSITE, 0);
			
			try {
				veloDispo = VeloDB.rechVelosDispo(idSite);
				if(veloDispo.size() > nombreVeloVoulu && !veloDispo.isEmpty()) {
					for(int j=0;j<nombreVeloVoulu;j++) {
						ReservationDB res = new ReservationDB(idClient,veloDispo.get(j).getIdVelo(),new java.sql.Date(System.currentTimeMillis()), "en cours");
						res.create();
						System.out.println("ION EST ICI");
					}
				}
				else {
					resultat="Pas assez de vélo disponible";				}
			}  catch (Exception e) {}
			
			System.out.println(""+veloDispo);
			System.out.println(""+veloDispo.size()+" ok"+nombreVeloVoulu+" "+new java.sql.Date(System.currentTimeMillis()));
			return true;
		
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			if(result) Toast.makeText(getApplicationContext(), "Réservation effectuée",Toast.LENGTH_SHORT).show(); 


		}
		

	}
}
