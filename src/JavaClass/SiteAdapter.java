package JavaClass;

import java.util.ArrayList;

import com.example.projet_villo.R;

import classesDB.SiteDB;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SiteAdapter extends BaseAdapter {
	
	// Une liste de sites
	private ArrayList<SiteDB> mListS;
	    	
	//Le contexte dans lequel est présent notre adapter
	private Context mContext;
	    	
	//Un mécanisme pour gérer l'affichage graphique depuis un layout XML
	private LayoutInflater mInflater;
	
	public SiteAdapter(Context context, ArrayList<SiteDB> aListS) {
		  mContext = context;
		  mListS = aListS;
		  mInflater = LayoutInflater.from(mContext);
		}
	
	@Override
	public int getCount() {
		return mListS.size();
	}

	@Override
	public Object getItem(int position) {
		return mListS.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		  LinearLayout layoutItem;
		  //(1) : Réutilisation des layouts
		  if (convertView == null) {
		  	//Initialisation de notre item à partir du  layout XML "personne_layout.xml"
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.site_layout, parent, false);
		  } else {
		  	layoutItem = (LinearLayout) convertView;
		  }
		  
		  //(2) : Récupération des TextView de notre layout      
		  TextView tvNom = (TextView)layoutItem.findViewById(R.id.tvNom);
		  TextView tvAdresse = (TextView)layoutItem.findViewById(R.id.tvAdresse);
		  TextView tvVeloDispo = (TextView)layoutItem.findViewById(R.id.tvVeloDispo);
		  
		//(3) : Renseignement des valeurs       
		  tvNom.setText("- "+mListS.get(position).getNom());
		  tvAdresse.setText(mListS.get(position).getVille());
		  
		 //Compter les places disponibles ^^
		  //1.Recupérer l'id du site en question
		  int idSite = mListS.get(position).getIdSite();
		  //2. appeller la fonction qui va récupérer le nombre de place disponible
		  int nombreVeloReserve = 0;
		  try {
			nombreVeloReserve = mListS.get(position).veloReserve(idSite);
		  } catch (Exception e) {}
		  int veloDispo = mListS.get(position).getVeloMax() - nombreVeloReserve - 3;
		  tvVeloDispo.setText(""+veloDispo);
		  
		//On retourne l'item créé.
		  return layoutItem;

	}
	

}
