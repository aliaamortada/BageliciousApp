package com.example.meal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meal.db.MealDB.MealLocalDataSourceImpl;
import com.example.meal.model.pojo.meal.FavMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * A Fragment that displays the user's favorite meals in a grid.
 */
public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;

    public FavouriteFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Initialize RecyclerView and its adapter
        recyclerView = root.findViewById(R.id.favouritesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new FavouriteAdapter();
        recyclerView.setAdapter(adapter);

        // Observe the LiveData list of FavMeal from Room database
        MealLocalDataSourceImpl.getInstance(requireContext())
                .getStoredFavoriteMeals()
                .observe(getViewLifecycleOwner(), new Observer<List<FavMeal>>() {
                    @Override
                    public void onChanged(List<FavMeal> favMeals) {
                        // Update adapter when data changes
                        adapter.setFavMeals(favMeals);
                    }
                });

        return root;
    }

    /**
     * RecyclerView.Adapter to bind FavMeal data into view items.
     */
    private class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavViewHolder> {
        private List<FavMeal> favMeals = new ArrayList<>();

        /**
         * Update the list of favorites and refresh the view.
         */
        void setFavMeals(List<FavMeal> list) {
            this.favMeals = list != null ? list : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate item layout
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_meal, parent, false);
            return new FavViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
            // Bind FavMeal data to views
            FavMeal meal = favMeals.get(position);
            holder.txtName.setText(meal.getStrMeal());
            holder.txtArea.setText(meal.getStrArea());
            Glide.with(holder.itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .into(holder.imgMeal);
            //show filled heart and make it clickable to unfavorite
            holder.heartIcon.setImageResource(R.drawable.fav); // always favorite here
            holder.heartIcon.setOnClickListener(v -> {
                // Remove from Room
                Executors.newSingleThreadExecutor().execute(() -> {
                    MealLocalDataSourceImpl.getInstance(
                            holder.itemView.getContext()
                    ).deleteFavoriteMeal(meal);
                    // Update UI on main thread
                    holder.itemView.post(() -> {
                        favMeals.remove(position);
                        notifyItemRemoved(position);
                    });
                });
            });
            // Launch MealActivity when a card is tapped
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), MealActivity.class);
                intent.putExtra("mealId", meal.getIdMeal());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            // Return the number of favorite meals
            return favMeals.size();
        }

        /**
         * ViewHolder that holds view references for a single favorite meal item.
         */
        class FavViewHolder extends RecyclerView.ViewHolder {
            ImageView imgMeal, heartIcon;
            TextView txtName, txtArea;

            FavViewHolder(View itemView) {
                super(itemView);
                imgMeal = itemView.findViewById(R.id.mealImageCard1);
                heartIcon = itemView.findViewById(R.id.heartIconCard1);
                txtName = itemView.findViewById(R.id.mealNameCard1);
                txtArea = itemView.findViewById(R.id.mealAreaCard1);
            }
        }
    }
}
