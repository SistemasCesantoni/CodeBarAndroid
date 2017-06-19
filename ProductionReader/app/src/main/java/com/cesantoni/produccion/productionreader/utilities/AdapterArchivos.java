package com.cesantoni.produccion.productionreader.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cesantoni.produccion.productionreader.R;
import com.cesantoni.produccion.productionreader.dao.ModeloListaArchivos;

import java.util.ArrayList;

/**
 * Created by liel on 08/06/2017.
 */

    public class AdapterArchivos extends ArrayAdapter<ModeloListaArchivos> {

        private final Context context;
        private final ArrayList<ModeloListaArchivos> modelsArrayList;

        public AdapterArchivos(Context context, ArrayList<ModeloListaArchivos> modelsArrayList) {

            super(context, R.layout.item_list_files, modelsArrayList);

            this.context = context;
            this.modelsArrayList = modelsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = null;
            if(!modelsArrayList.get(position).isGroupHeader()){
                rowView = inflater.inflate(R.layout.item_list_files, parent, false);

                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);

                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
            }
            return rowView;
        }
    }
