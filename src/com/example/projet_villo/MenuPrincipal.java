package com.example.projet_villo;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends ActionBarActivity {
	private Button deconnexion;
	private Button reserver;
	private Button localiser;
	private Button mesReservations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menuprincipal);
		
		deconnexion = (Button) findViewById(R.id.deconnexion);
		reserver = (Button) findViewById(R.id.reserver);
		localiser = (Button) findViewById(R.id.localiser);
		mesReservations = (Button) findViewById(R.id.mesReservations);
	}
	
	public void gestDeconnexion(View view) {
		Intent i = new Intent(MenuPrincipal.this,Connexion.class);
		startActivity(i); // Pourquoi quand on fait précédent on revient au menu ?
	}
	
	public void gestReserver(View view) {
		Intent i = getIntent();
		int idClient = i.getIntExtra(Connexion.IDCLIENT, 0);
	    i = new Intent(MenuPrincipal.this,RechercheSite.class);
		i.putExtra(Connexion.IDCLIENT,idClient);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_principal, menu);
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

	}
