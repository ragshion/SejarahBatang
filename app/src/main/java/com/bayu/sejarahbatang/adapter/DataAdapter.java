package com.bayu.sejarahbatang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.activities.DetailActivity;
import com.bayu.sejarahbatang.activities.DtActivity;
import com.bayu.sejarahbatang.objek.Sejarah;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<Sejarah> mArrayList;
    private ArrayList<Sejarah> mFilteredList;

    public DataAdapter(ArrayList<Sejarah> arrayList){
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_data, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int i) {

        holder.tv_judul.setText(mFilteredList.get(i).getNama());
        holder.tv_alamat.setText(mFilteredList.get(i).getAlamat());
        holder.tv_jam_buka.setText(mFilteredList.get(i).getJamBuka()+" - "+mFilteredList.get(i).getJamTutup());
        holder.tv_jarak.setText(mFilteredList.get(i).getJarak()+" km");

        String kategori = mFilteredList.get(i).getKategori();
        holder.id_sejarah= mFilteredList.get(i).getIdSejarah();

        if(kategori.equals("bangunan")){
            holder.gambarSejarah.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.bank));
        }else if(kategori.equals("masjid")){
            holder.gambarSejarah.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.mosque));
        }else if(kategori.equals("makam")){
            holder.gambarSejarah.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.cementery));
        }else if(kategori.equals("goa")){
            holder.gambarSejarah.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.cave));
        }


    }

    @Override
    public int getItemCount(){
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Sejarah> filteredList = new ArrayList<>();
                    for (Sejarah artikel: mArrayList) {

                        if (artikel.getNama().toLowerCase().contains(charString) ||
                                artikel.getAlamat().toLowerCase().contains(charString)) {
                            filteredList.add(artikel);

                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Sejarah>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_judul, tv_alamat, tv_jam_buka, tv_jarak;
        private ImageView gambarSejarah;
        String id_sejarah;

        public ViewHolder(View view){
            super(view);

            tv_judul = view.findViewById(R.id.tv_judul);
            tv_alamat = view.findViewById(R.id.tv_alamat);
            tv_jam_buka = view.findViewById(R.id.tv_jam_buka);
            tv_jarak = view.findViewById(R.id.tv_jarak);

            gambarSejarah = view.findViewById(R.id.imageSejarah);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(v.getContext(), DtActivity.class);
                        intent.putExtra("id",id_sejarah);
                        intent.putExtra("judul",tv_judul.getText());
                        v.getContext().startActivity(intent);

                    }
                }
            });

        }
    }
}
