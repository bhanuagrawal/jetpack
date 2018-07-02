package agrawal.bhanu.thoughtworks.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.thoughtworks.pojo.BeerPOJO;
import agrawal.bhanu.thoughtworks.R;

public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.beerViewHolder> {

    private final Context context;
    private ArrayList<BeerPOJO> beers;

    public ArrayList<BeerPOJO> getBeers() {
        return beers;
    }

    public void setBeers(ArrayList<BeerPOJO> beers) {
        this.beers = beers;
        notifyDataSetChanged();
    }

    public class beerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView beerNameTV;
        public TextView beerAlcTV;
        public TextView beerABCTv;
        public ImageView beerIconIV;
        public ConstraintLayout parentLayout;

        public beerViewHolder(View view) {
            super(view);
            beerNameTV = (TextView) view.findViewById(R.id.beer_name);
            beerIconIV = (ImageView) view.findViewById(R.id.beer_icon);
            beerAlcTV = (TextView) view.findViewById(R.id.beer_alcohal);
            beerABCTv = (TextView) view.findViewById(R.id.beer_abc);

            parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }
    }


    public BeersAdapter(Context context, ArrayList<BeerPOJO> beers) {
        this.context = context;
        this.beers = beers;
    }

    @Override
    public beerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_beer, parent, false);

        return new beerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(beerViewHolder holder, final int position) {

        holder.beerNameTV.setText(beers.get(position).getName());
        holder.beerAlcTV.setText("Alc. Content: " + beers.get(position).getAbv());
        holder.beerABCTv.setText(beers.get(position).getStyle());
        //sholder.beerIconIV.setImageDrawable(beers.get(position));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }
}
