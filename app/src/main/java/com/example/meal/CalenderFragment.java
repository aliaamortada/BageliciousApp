package com.example.meal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meal.db.MealDB.MealLocalDataSourceImpl;
import com.example.meal.db.MealDB.MealDataBase;
import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.PlanMeal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/**
 * A Fragment that shows a CalendarView and, beneath it,
 * the list of PlanMeal items stored for the selected date.
 */
public class CalenderFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private MealLocalDataSourceImpl localDataSource;
    private PlannedMealsAdapter adapter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calender, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        recyclerView = root.findViewById(R.id.recyclerViewPlannedMeals);

        localDataSource = MealLocalDataSourceImpl.getInstance(requireContext());

        adapter = new PlannedMealsAdapter(requireContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // initial load for today
        String today = sdf.format(new Date(calendarView.getDate()));
        observeMealsForDate(today);

        // listen to date changes
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d",
                    year, month + 1, dayOfMonth);
            observeMealsForDate(date);
        });

        return root;
    }

    private void observeMealsForDate(String date) {
        localDataSource.getStoredPlannedMeals()
                .observe(getViewLifecycleOwner(), planMeals -> {
                    List<PlanMeal> filtered = new ArrayList<>();
                    for (PlanMeal pm : planMeals) {
                        if (pm.date.equals(date)) {
                            filtered.add(pm);
                        }
                    }
                    adapter.setMeals(filtered);
                });
    }

    // -------------------------------------------------------------------
    // Adapter for RecyclerView: displays each PlanMeal with its image,
    // name, area, and a clickable heart icon to toggle favorite status.
    // -------------------------------------------------------------------
    public static class PlannedMealsAdapter
            extends RecyclerView.Adapter<PlannedMealsAdapter.ViewHolder> {

        private final Context context;
        private List<PlanMeal> meals;

        public PlannedMealsAdapter(Context context, List<PlanMeal> initialMeals) {
            this.context = context;
            this.meals = initialMeals;
        }

        public void setMeals(List<PlanMeal> newMeals) {
            this.meals = newMeals != null ? newMeals : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(context)
                    .inflate(R.layout.item_meal, parent, false);
            return new ViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PlanMeal meal = meals.get(position);

            holder.mealName.setText(meal.getStrMeal());
            holder.mealArea.setText(meal.getStrArea());
            Glide.with(context)
                    .load(meal.getStrMealThumb())
                    .into(holder.mealImage);

            // Check favorite status directly from database
            Executors.newSingleThreadExecutor().execute(() -> {
                boolean isFav = MealDataBase
                        .getInstance(context)
                        .getMealDao()
                        .isFavorite(meal.getIdMeal());
                holder.itemView.post(() ->
                        holder.heartIcon.setImageResource(isFav ? R.drawable.fav : R.drawable.heart)
                );
            });
            // handle toggle exactly as in your examples
            holder.heartIcon.setOnClickListener(v -> {
                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean nowFav = MealDataBase
                            .getInstance(context)
                            .getMealDao()
                            .isFavorite(meal.getIdMeal());
                    if (nowFav) {
                        MealDataBase.getInstance(context)
                                .getMealDao()
                                .deleteFavoriteMeal(new FavMeal(meal));
                        holder.itemView.post(() ->
                                holder.heartIcon.setImageResource(R.drawable.heart)
                        );
                    } else {
                        MealDataBase.getInstance(context)
                                .getMealDao()
                                .insertFavoriteMeal(new FavMeal(meal));
                        holder.itemView.post(() ->
                                holder.heartIcon.setImageResource(R.drawable.fav)
                        );
                    }
                });
            });
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, MealActivity.class);
                intent.putExtra("mealId", meal.getIdMeal());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return meals.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mealImage, heartIcon;
            TextView mealName, mealArea;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mealImage = itemView.findViewById(R.id.mealImageCard1);
                heartIcon = itemView.findViewById(R.id.heartIconCard1);
                mealName  = itemView.findViewById(R.id.mealNameCard1);
                mealArea  = itemView.findViewById(R.id.mealAreaCard1);
            }
        }
    }
}
