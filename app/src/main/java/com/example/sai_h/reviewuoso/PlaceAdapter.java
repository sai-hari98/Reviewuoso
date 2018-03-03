package com.example.sai_h.reviewuoso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sai_h on 03-03-2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{
    private Context mContext;
    private List<Placeresult> places;

    public PlaceAdapter(Context mContext, List<Placeresult> places) {
        this.mContext = mContext;
        this.places = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_layout,null);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Placeresult p = places.get(position);
        holder.t1.setText(p.getName());
        holder.t2.setText(p.getVicinity());
        holder.t3.setText(p.getRating());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder{
            TextView t1,t2,t3;
         public PlaceViewHolder(View itemView) {
             super(itemView);
             t1 = itemView.findViewById(R.id.name);
             t2 = itemView.findViewById(R.id.vicinity);
             t3 = itemView.findViewById(R.id.rating);
         }
     }
}
