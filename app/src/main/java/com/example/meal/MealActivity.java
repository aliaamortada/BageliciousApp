package com.example.meal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meal.db.MealDB.MealDataBase;
import com.example.meal.model.pojo.ingrediant.Ingrediant;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.model.pojo.meal.MealResponse;
import com.example.meal.network.meal.MealService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealActivity extends AppCompatActivity {

    private ImageView mealImage;
    private ImageButton btnClose, btnFavorite;
    private RecyclerView recyclerViewMealInfo;

    private MealService mealService;
    private MealDataBase mealDatabase;

    private Meal currentMeal;
    private MealInfoAdapter mealInfoAdapter;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        // Views
        mealImage = findViewById(R.id.mealImage);
        btnClose = findViewById(R.id.btnClose);
        btnFavorite = findViewById(R.id.btnFavorite);
        recyclerViewMealInfo = findViewById(R.id.recyclerViewMealInfo);

        // Setup DB and Retrofit
        mealDatabase = MealDataBase.getInstance(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealService = retrofit.create(MealService.class);

        // Adapters
        mealInfoAdapter = new MealInfoAdapter();
        recyclerViewMealInfo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMealInfo.setAdapter(mealInfoAdapter);

        ingredientAdapter = new IngredientAdapter();

        // Listeners
        btnClose.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            if (currentMeal == null) return;
            Executors.newSingleThreadExecutor().execute(() -> {
                boolean isFav = mealDatabase.getMealDao().isFavorite(currentMeal.getIdMeal());
                if (isFav) {
                    mealDatabase.getMealDao().deleteMeal(currentMeal);
                    runOnUiThread(() -> btnFavorite.setImageResource(R.drawable.heart));
                } else {
                    mealDatabase.getMealDao().insertMeal(currentMeal);
                    runOnUiThread(() -> btnFavorite.setImageResource(R.drawable.fav));
                }
            });
        });

        String mealId = getIntent().getStringExtra("mealId");
        if (mealId != null) {
            loadMealDetails(mealId);
        }
    }

    private void loadMealDetails(String id) {
        mealService.getMealByID(id).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body() != null && !response.body().getMeals().isEmpty()) {
                    currentMeal = response.body().getMeals().get(0);
                    displayMealData(currentMeal);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                // Error handling
            }
        });
    }

    private void displayMealData(Meal meal) {
        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);

        mealInfoAdapter.setMeal(meal,meal.getIngredientsList());
        ingredientAdapter.setIngredients(meal.getIngredientsList());

        Executors.newSingleThreadExecutor().execute(() -> {
            boolean isFav = mealDatabase.getMealDao().isFavorite(meal.getIdMeal());
            runOnUiThread(() -> btnFavorite.setImageResource(isFav ? R.drawable.fav : R.drawable.heart));
        });
    }

    // ======================= MealInfoAdapter =======================
    public static class MealInfoAdapter extends RecyclerView.Adapter<MealInfoAdapter.MealInfoViewHolder> {

        private Meal meal;

        private List<Ingrediant> ingredients = new ArrayList<>();

        public void setMeal(Meal meal, List<Ingrediant> ingredients) {
            this.meal = meal;
            this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
            notifyDataSetChanged();
        }


        @Override
        public MealInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_details, parent, false);
            return new MealInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MealInfoViewHolder holder, int position) {
            if (meal == null) return;
            holder.title.setText(meal.getStrMeal());
            holder.category.setText(meal.getStrCategory());
            holder.area.setText(meal.getStrArea());
            holder.instructions.setText(meal.getStrInstructions());
            holder.bindVideo(meal.getStrYoutube());

            holder.bindIngredients(ingredients);
        }

        @Override
        public int getItemCount() {
            return meal == null ? 0 : 1;
        }

        static class MealInfoViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerViewIngredients;
            IngredientAdapter ingredientAdapter;
            TextView title, category, area, instructions;
            WebView webView;

            MealInfoViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.txtMealName);
                category = itemView.findViewById(R.id.txtMealCategory);
                area = itemView.findViewById(R.id.txtMealArea);
                instructions = itemView.findViewById(R.id.txtInstructionsDetail);

                webView = itemView.findViewById(R.id.webViewMeal);

                // Initialize recyclerViewIngredients here, as it is in the meal_details layout
                recyclerViewIngredients = itemView.findViewById(R.id.recyclerViewIngredients);
                ingredientAdapter = new IngredientAdapter();
                recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerViewIngredients.setAdapter(ingredientAdapter);
            }
            void bindIngredients(List<Ingrediant> ingredients) {
                if (ingredientAdapter != null) {
                    if (ingredients != null) {
                        ingredientAdapter.setIngredients(ingredients);
                    } else {
                        Log.e("MealActivity", "Mismatch between ingredients and measures size or data is null.");
                    }
                } else {
                    // Log the error if the adapter is null
                    Log.e("MealActivity", "IngredientAdapter is null. Unable to bind ingredients.");
                }
            }
            void bindVideo(String videoUrl) {
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    String embedUrl = videoUrl.replace("watch?v=", "embed/");
                    webView.setVisibility(View.VISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadData(
                            "<iframe width=\"100%\" height=\"100%\" src=\"" + embedUrl + "\" frameborder=\"0\" allowfullscreen></iframe>",
                            "text/html", "utf-8"
                    );
                } else {
                    webView.setVisibility(View.GONE);
                }
            }

        }
    }

    // ======================= IngredientAdapter =======================
    public static class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

        private List<Ingrediant> ingredients = new ArrayList<>();
        public void setIngredients(List<Ingrediant> ingredients) {
            this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
          Log.d("test","adasd"+ingredients.size());
            notifyDataSetChanged();
        }

        @Override
        public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingrediants_details, parent, false);
            return new IngredientViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IngredientViewHolder holder, int position) {
            // Ensure that the position is valid and data exists
//            Log.d("IngredientAdapter", "Position: " + position + ", Ingredient: " + ingredients.get(position).getIdIngredient() + ", Measure: " + ingredients.get(position).getStrMesure());

            if (position < ingredients.size()) {
                Ingrediant ingredient = ingredients.get(position);
                holder.name.setText(ingredients.get(position).getStrIngredient());
                holder.measure.setText(ingredients.get(position).getStrMesure());

                // Construct image URL based on the ingredient name
                String imageUrl = "https://www.themealdb.com/images/ingredients/"
                        + ingredient.getStrIngredient().trim().replace(" ", "%20")
                        + "-Small.png";

                // Load image using Glide
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.imageIngredient);
            }

        }


        @Override
        public int getItemCount() {
            return ingredients.size(); // Ensure the correct number of ingredients are being shown
        }

        static class IngredientViewHolder extends RecyclerView.ViewHolder {
            ImageView imageIngredient;
            TextView name, measure;

            IngredientViewHolder(View itemView) {
                super(itemView);
                imageIngredient = itemView.findViewById(R.id.imgIngredient);
                name = itemView.findViewById(R.id.txtIngredientName);
                measure = itemView.findViewById(R.id.txtIngredientAmount);
            }
        }
    }
}
