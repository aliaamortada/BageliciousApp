package com.example.meal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meal.db.MealDB.MealDataBase;
import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.network.meal.MealService;
import com.example.meal.model.pojo.meal.MealResponse;
import com.example.meal.MealActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private boolean isViewActive = false;
    private static final String PREFS_NAME = "meal_of_day_prefs";
    private static final String KEY_DATE = "saved_date";
    private static final String KEY_MEAL_ID = "saved_meal_id";

    private RecyclerView recyclerView;
    private MealAdapter adapter;
    private MealDataBase mealDatabase;
    private MealService mealService;

    private ImageView mealImage, heartIcon;
    private TextView mealName, mealArea;

    private Meal mealOfTheDay;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();  // Executor for background tasks

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mealImage = rootView.findViewById(R.id.mealImage);
        heartIcon = rootView.findViewById(R.id.heartIcon);
        mealName = rootView.findViewById(R.id.mealName);
        mealArea = rootView.findViewById(R.id.mealArea);

        recyclerView = rootView.findViewById(R.id.popularMealsRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MealAdapter();
        recyclerView.setAdapter(adapter);

        mealDatabase = MealDataBase.getInstance(requireContext());

        mealDatabase
                .getMealDao()
                .getStoredFavoriteMeals()
                .observe(getViewLifecycleOwner(), favMeals -> {
                    adapter.notifyDataSetChanged();
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealService = retrofit.create(MealService.class);

        loadMealOfTheDay();
        loadTenRandomMeals();
        isViewActive = true;
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewActive = false; // Reset view active flag
    }

    private void loadMealOfTheDay() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
               String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                           .format(new Date());
               String savedDate = prefs.getString(KEY_DATE, null);
               String savedId = prefs.getString(KEY_MEAL_ID, null);
        Call<MealResponse> call = (savedDate != null && savedDate.equals(today) && savedId != null)
                ? mealService.getMealByID(savedId)
                : mealService.lookupSingleRandomMeal();
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body() != null && response.body().getMeals() != null && !response.body().getMeals().isEmpty()) {
                    mealOfTheDay = response.body().getMeals().get(0);

                    mealName.setText(mealOfTheDay.getStrMeal());
                    mealArea.setText(mealOfTheDay.getStrArea());

                    Glide.with(requireContext())
                            .load(mealOfTheDay.getStrMealThumb())
                            .into(mealImage);

                    executorService.execute(() -> {
                        boolean isFav = mealDatabase.getMealDao().isFavorite(mealOfTheDay.getIdMeal());
                        getActivity().runOnUiThread(() -> {
                            if (isViewActive) { // Check isViewActive
                                heartIcon.setImageResource(isFav ? R.drawable.fav : R.drawable.heart);

                                heartIcon.setOnClickListener(v -> {
                                    executorService.execute(() -> {
                                        boolean nowFav = mealDatabase.getMealDao().isFavorite(mealOfTheDay.getIdMeal());
                                        if (nowFav) {
                                            mealDatabase.getMealDao().deleteFavoriteMeal(new FavMeal(mealOfTheDay));
                                            getActivity().runOnUiThread(() -> {
                                                if (isViewActive) { // Check isViewActive
                                                    heartIcon.setImageResource(R.drawable.heart);
                                                }
                                            });
                                        } else {
                                            mealDatabase.getMealDao().insertFavoriteMeal(new FavMeal(mealOfTheDay));
                                            getActivity().runOnUiThread(() -> {
                                                if (isViewActive) { // **ADDED**: Check isViewActive
                                                    heartIcon.setImageResource(R.drawable.fav);
                                                }
                                            });
                                        }
                                    });
                                });
                            }
                        });
                    });

                    mealImage.setOnClickListener(v -> {
                        Intent intent = new Intent(requireContext(), MealActivity.class);
                        intent.putExtra("mealId", mealOfTheDay.getIdMeal());
                        startActivity(intent);
                    });
  /*                  mealDatabase
                            .getMealDao()
                            .getStoredFavoriteMeals()
                            .observe(getViewLifecycleOwner(), favMeals -> {
                                boolean isFavNow = false;
                                for (FavMeal f : favMeals) {
                                    if (f.getIdMeal().equals(mealOfTheDay.getIdMeal())) {
                                        isFavNow = true;
                                        break;
                                    }
                                }
                                heartIcon.setImageResource(isFavNow ? R.drawable.fav : R.drawable.heart);
                            });*/
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {}
        });
    }

    private void loadTenRandomMeals() {
        List<Meal> tempList = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            mealService.lookupSingleRandomMeal().enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    if (response.body() != null && response.body().getMeals() != null) {
                        Meal meal = response.body().getMeals().get(0);
                        tempList.add(meal);
                    }
                    if (counter.incrementAndGet() == 10) {
                        adapter.addMeals(tempList);
                    }
                }

                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {
                    if (counter.incrementAndGet() == 10) {
                        adapter.addMeals(tempList);
                    }
                }
            });
        }
    }

    private class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

        private final List<Meal> mealList = new ArrayList<>();

        void addMeals(List<Meal> meals) {
            int start = mealList.size();
            mealList.addAll(meals);
            notifyItemRangeInserted(start, meals.size());
        }

        @Override
        public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
            return new MealViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MealViewHolder holder, int position) {
            Meal meal = mealList.get(position);

            holder.mealName.setText(meal.getStrMeal());
            holder.mealArea.setText(meal.getStrArea());

            Glide.with(holder.itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .into(holder.mealImage);

            executorService.execute(() -> {
                boolean isFav = mealDatabase.getMealDao().isFavorite(meal.getIdMeal());
                getActivity().runOnUiThread(() -> {
                    holder.heartIcon.setImageResource(isFav ? R.drawable.fav : R.drawable.heart);

                    holder.heartIcon.setOnClickListener(v -> {
                        executorService.execute(() -> {
                            boolean nowFav = mealDatabase.getMealDao().isFavorite(meal.getIdMeal());
                            if (nowFav) {
                                mealDatabase.getMealDao().deleteFavoriteMeal(new FavMeal(meal));
                                getActivity().runOnUiThread(() -> holder.heartIcon.setImageResource(R.drawable.heart));
                            } else {
                                mealDatabase.getMealDao().insertFavoriteMeal(new FavMeal(meal));
                                getActivity().runOnUiThread(() -> holder.heartIcon.setImageResource(R.drawable.fav));
                            }
                        });
                    });
                });
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), MealActivity.class);
                intent.putExtra("mealId", meal.getIdMeal());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mealList.size();
        }

        class MealViewHolder extends RecyclerView.ViewHolder {
            TextView mealName, mealArea;
            ImageView mealImage, heartIcon;

            MealViewHolder(View itemView) {
                super(itemView);
                mealName = itemView.findViewById(R.id.mealNameCard1);
                mealArea = itemView.findViewById(R.id.mealAreaCard1);
                mealImage = itemView.findViewById(R.id.mealImageCard1);
                heartIcon = itemView.findViewById(R.id.heartIconCard1);
            }
        }
    }
}
