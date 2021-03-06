package com.pbp.tubes.uas.richhotel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pbp.tubes.uas.richhotel.Dao.KamarDAO;
import com.pbp.tubes.uas.richhotel.Dao.TransaksiDAO;
import com.pbp.tubes.uas.richhotel.Edit.EditReservasiUser;
import com.pbp.tubes.uas.richhotel.Fragment.DetailKamarFragmentUser;
import com.pbp.tubes.uas.richhotel.R;

import java.util.List;
import java.util.stream.Collectors;

public class ReservasiUserRecyclerAdapter extends RecyclerView.Adapter <ReservasiUserRecyclerAdapter.RoomViewHolder> implements Filterable {

    private List<TransaksiDAO> dataList;
    private List<TransaksiDAO> filteredDataList;
    private Context context;

    public ReservasiUserRecyclerAdapter(List<TransaksiDAO> dataList, Context context) {
        this.dataList = dataList;
        this.filteredDataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservasiUserRecyclerAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_adapter_reservasi_user, parent, false);
        return new ReservasiUserRecyclerAdapter.RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasiUserRecyclerAdapter.RoomViewHolder holder, int position) {
        final TransaksiDAO transaksi = filteredDataList.get(position);

        holder.twNamaPemesan.setText(transaksi.getNama());
        holder.twAlamatPemesan.setText(transaksi.getAlamat());
        holder.twPilihanKamar.setText(transaksi.getPilihan_kamar());
        holder.twTanggalCheckIn.setText(transaksi.getTglCheckIn());
        holder.twTglCheckOut.setText(transaksi.getTglCheckOut());

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditReservasiUser.class);
                i.putExtra("idTransaksi", transaksi.getId());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        private TextView twNamaPemesan, twIdPemesan, twAlamatPemesan, twPilihanKamar, twTanggalCheckIn, twTglCheckOut;
        private LinearLayout mParent;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            twNamaPemesan = itemView.findViewById(R.id.twNamaPemesanr);
            twAlamatPemesan = itemView.findViewById(R.id.twAlamatr);
            twPilihanKamar = itemView.findViewById(R.id.twPilihanKamarr);
            twTanggalCheckIn = itemView.findViewById(R.id.twCheckInr);
            twTglCheckOut = itemView.findViewById(R.id.twCheckOutr);
            mParent = itemView.findViewById(R.id.linearLayoutr);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence charSequence) {

                filteredDataList = charSequence == null ? dataList :
                        dataList.stream().filter(data -> data.getNama().toLowerCase().contains(charSequence.toString().toLowerCase())).collect(Collectors.toList());

                FilterResults results = new FilterResults();
                results.values = filteredDataList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (List<TransaksiDAO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
