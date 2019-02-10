package com.bayu.sejarahbatang.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.objek.Koment;

import java.util.ArrayList;


public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {
    private ArrayList<Koment> mFilteredList;

    public KomentarAdapter(ArrayList<Koment> arrayList){
        mFilteredList = arrayList;
    }

    @Override
    public KomentarAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_komentar, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KomentarAdapter.ViewHolder holder, int i) {

        holder.tv_alamat.setText(mFilteredList.get(i).getAlamat());
        holder.tv_nama.setText(mFilteredList.get(i).getNama());
        holder.tv_komentar.setText(mFilteredList.get(i).getKomentar());
        holder.tv_tgl.setText(mFilteredList.get(i).getTglKomen());
        holder.tv_tanggap.setText(mFilteredList.get(i).getTanggapan());

        if(mFilteredList.get(i).getTampilkan().equalsIgnoreCase("0")){
            holder.tv_admin.setVisibility(View.GONE);
            holder.adminDivider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
        return mFilteredList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nama, tv_komentar, tv_tgl,tv_alamat, tv_tanggap;
        TextView tv_admin;
        View adminDivider;


        public ViewHolder(View view){
            super(view);

            tv_nama = view.findViewById(R.id.tv_km_nama);
            tv_alamat= view.findViewById(R.id.tv_km_alamat);
            tv_komentar = view.findViewById(R.id.tv_km_isi);
            tv_tgl = view.findViewById(R.id.tv_km_tanggal);
            tv_tanggap = view.findViewById(R.id.tv_tanggapan);
            tv_admin = view.findViewById(R.id.tv_admin);
            adminDivider = view.findViewById(R.id.admin_divider);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){


//                        String nomor = "tel:"+tv_no_telp.getText().toString().trim();
//                        Intent manggilIntent = new Intent(Intent.ACTION_CALL);
//                        manggilIntent.setData(Uri.parse(nomor));
//                        if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                            return;
//                        }
//                        v.getContext().startActivity(manggilIntent);

                    }
                }
            });
        }
    }
}

