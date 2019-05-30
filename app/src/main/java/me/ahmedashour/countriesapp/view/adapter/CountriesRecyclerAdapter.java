package me.ahmedashour.countriesapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ahmedashour.countriesapp.R;
import me.ahmedashour.countriesapp.model.Country;
import me.ahmedashour.countriesapp.view.adapter.CountriesRecyclerAdapter.CountriesViewHolder;

public class CountriesRecyclerAdapter extends RecyclerView.Adapter<CountriesViewHolder> {

    private ArrayList<Country> countries;
    private Context context;

    public CountriesRecyclerAdapter(ArrayList<Country> countries, Context context) {
        this.countries = countries;
        this.context = context;
    }

    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, int position) {
        holder.tvCountry.setText(countries.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return countries != null ? countries.size() : 0;
    }

    class CountriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_country)
        TextView tvCountry;

        public CountriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
