package com.janet.farmersclub;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

/**
 * Created by anableila on 7/10/18.
 */

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.MyViewHolder>{
    private List<ClipData.Item> itemList;
    private Context mContext;

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        public TextView farmname, farmdesc;
        public TextView location;

        public MyViewHolder(View itemView) {
            super(itemView);
            farmname = (TextView) itemView.findViewById(R.id.farmname);
            farmdesc =(TextView) itemView.findViewById(R.id.farmdesc);
            location = (TextView) itemView.findViewById(com.janet.farmersclub.R.id.location);
        }
    }

    //constructor for home adapter
    public FarmAdapter(Context context, List<ClipData.Item> itemList){
        this.itemList = itemList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_layout, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClipData.Item item = itemList.get(position);
        holder.farmname.setText(MyFarms.getName());
        holder.farmdesc.setText(MyFarms.getDescription());
        holder.location.setText(MyFarms.getLocation());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
