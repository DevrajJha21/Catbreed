package com.example.catbreed.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catbreed.R;
import com.example.catbreed.databinding.CatBreedRowBinding;
import com.example.catbreed.ui.main.model.BreedsDatum;

import java.util.ArrayList;
import java.util.List;

public class CatBreedAdapter extends RecyclerView.Adapter<CatBreedAdapter.ViewHolder> implements Filterable {
   private ArrayList<BreedsDatum> breedList ;
    private ArrayList<BreedsDatum> fullBreedList;
    public CatBreedAdapter(ArrayList<BreedsDatum> breedList) {
        this.breedList = breedList;
        fullBreedList= new ArrayList<>(breedList);
    }
    public void addBreedDataList(List<BreedsDatum> breedList) {
        if (!this.breedList.isEmpty()) {
            this.breedList.clear();
            this.fullBreedList.clear();
        }
        this.breedList.addAll(breedList);
        this.fullBreedList.addAll(breedList);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.cat_breed_row,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(breedList.get(position));
    }

    @Override
    public int getItemCount() {
        return breedList.size();
    }

    @Override
    public Filter getFilter() {
        return searchedFilter;
    }
    // added searching by name
    private Filter searchedFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<BreedsDatum> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullBreedList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BreedsDatum item : fullBreedList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            breedList.clear();
            breedList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        CatBreedRowBinding binding;

        public ViewHolder(CatBreedRowBinding itemView) {

            super(itemView.getRoot());
            this.binding=itemView;
        }
        public void bindData(BreedsDatum breedsDatum){
            CatBridViewModel catBridViewModel=new CatBridViewModel();
            if (breedsDatum.getImage()!=null)
            catBridViewModel.setImageUrl(breedsDatum.getImage().getUrl());
            catBridViewModel.setId(breedsDatum.getId());
            catBridViewModel.setName(breedsDatum.getName());
            catBridViewModel.setDescription(breedsDatum.getDescription());
            binding.setViewModel(catBridViewModel);
        }
    }


}


