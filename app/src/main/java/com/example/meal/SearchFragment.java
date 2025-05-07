package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meal.model.pojo.area.AreaResponse;
import com.example.meal.model.pojo.area.Area;
import com.example.meal.model.pojo.category.CategoryResponse;
import com.example.meal.model.pojo.category.Category;
import com.example.meal.model.pojo.ingrediant.Ingrediant;
import com.example.meal.model.pojo.ingrediant.IngrediantResponse;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.model.pojo.meal.MealResponse;
import com.example.meal.network.area.AreaService;
import com.example.meal.network.category.CategoryService;
import com.example.meal.network.ingrediant.IngrediantService;
import com.example.meal.network.meal.MealService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * SearchFragment supports four tabs: Meal, Category, Area, Ingredient.
 * - Meal: user searches by name.
 * - Category/Area/Ingredient: show full list immediately on tab select.
 */
public class SearchFragment extends Fragment {

    private TabLayout tabLayout;
    private SearchView searchView;

    private RecyclerView rvResults;
    private SearchAdapter adapter;


    // Retrofit services
    private MealService mealService;
    private CategoryService categoryService;
    private AreaService areaService;
    private IngrediantService ingrediantService;

    // Data sources
    private final List<String> mealNames       = new ArrayList<>();
    private final List<Category> categories    = new ArrayList<>();
    private  List<Area> areaNames         = new ArrayList<>();
    private final List<String> ingredientNames = new ArrayList<>();
    private final List<Area> areaList = new ArrayList<>();

    // Map area name -> drawable resource

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        searchView = root.findViewById(R.id.searchView);
        rvResults = root.findViewById(R.id.rvSearchResults);

        setupAreaList();
        adapter = new SearchAdapter();
        rvResults.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvResults.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealService       = retrofit.create(MealService.class);
        categoryService   = retrofit.create(CategoryService.class);
        areaService       = retrofit.create(AreaService.class);
        ingrediantService = retrofit.create(IngrediantService.class);

        loadCategories();
        loadIngredients();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchView.setQuery("", false); // clear search text
                searchView.clearFocus();        // remove focus so keyboard hides

                switch (tab.getPosition()) {
                    case 0: // Meal
                        adapter.setItems(mealNames);
                        searchView.setQueryHint("Search by meal...");
                        break;
                    case 1: // Category
                        adapter.setItems(categories);
                        searchView.setQueryHint("Search by category...");
                        break;
                    case 2: // Area
                        loadAreas();
                        searchView.setQueryHint("Search by area...");
                        break;
                    case 3: // Ingredient
                        adapter.setItems(ingredientNames);
                        searchView.setQueryHint("Search by ingredient...");
                        break;
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        tabLayout.getTabAt(0).select();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearch(newText.trim());
                return true;
            }
        });



        return root;
    }
    private void fetchMealsByCategory(String category) {
        mealService.getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override public void onResponse(Call<MealResponse> call, Response<MealResponse> resp) {
                if (resp.body() != null && resp.body().getMeals() != null) {
                    adapter.setMeals(resp.body().getMeals());
                }
            }
            @Override public void onFailure(Call<MealResponse> call, Throwable t) {}
        });
    }

    private void fetchMealsByArea(String area) {
        mealService.getMealsByArea(area).enqueue(new Callback<MealResponse>() {
            @Override public void onResponse(Call<MealResponse> call, Response<MealResponse> resp) {
                if (resp.body() != null && resp.body().getMeals() != null) {
                    adapter.setMeals(resp.body().getMeals());
                }
            }
            @Override public void onFailure(Call<MealResponse> call, Throwable t) {}
        });
    }

    private void fetchMealsByIngredient(String ingredient) {
        mealService.getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override public void onResponse(Call<MealResponse> call, Response<MealResponse> resp) {
                if (resp.body() != null && resp.body().getMeals() != null) {
                    adapter.setMeals(resp.body().getMeals());
                }
            }
            @Override public void onFailure(Call<MealResponse> call, Throwable t) {}
        });
    }

    private void handleSearch(String query) {
        int selectedTab = tabLayout.getSelectedTabPosition();

        if (query.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            return;
        }

        if (selectedTab == 0) {
            performMealSearch(query);
        } else if (selectedTab == 1) {
            List<Category> filtered = new ArrayList<>();
            for (Category c : categories) {
                if (c.getStrCategory().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(c);
                }
            }
            adapter.setItems(filtered);
        } else if (selectedTab == 2) {
            List<Area> filtered = new ArrayList<>();
            for (Area a : areaList) {
                if (a.getStrArea().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(a);
                }
            }
            adapter.setItems(filtered);
        } else if (selectedTab == 3) {
            List<String> filtered = new ArrayList<>();
            for (String ing : ingredientNames) {
                if (ing.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(ing);
                }
            }
            adapter.setItems(filtered);
        }
    }

    private void setupAreaList() {
        areaList.add(new Area("Egyptian", R.drawable.egypt_large));
        areaList.add(new Area("American", R.drawable.america_large));
        areaList.add(new Area("British", R.drawable.british_large));
        areaList.add(new Area("Canadian", R.drawable.canada_large));
        areaList.add(new Area("Chinese", R.drawable.china_large));
        areaList.add(new Area("Croatian", R.drawable.croatia_large));
        areaList.add(new Area("Dutch", R.drawable.netherlands_large));
        areaList.add(new Area("Filipino", R.drawable.phillippines_large));
        areaList.add(new Area("French", R.drawable.france_large));
        areaList.add(new Area("Greek", R.drawable.greece_large));
        areaList.add(new Area("Indian", R.drawable.india_large));
        areaList.add(new Area("Irish", R.drawable.ireland_large));
        areaList.add(new Area("Italian", R.drawable.italy_large));
        areaList.add(new Area("Jamaican", R.drawable.jamaica_large));
        areaList.add(new Area("Japanese", R.drawable.japan_large));
        areaList.add(new Area("Kenyan", R.drawable.kenya_large));
        areaList.add(new Area("Malaysian", R.drawable.malaysia_large));
        areaList.add(new Area("Mexican", R.drawable.mexico_large));
        areaList.add(new Area("Moroccan", R.drawable.morocco_large));
        areaList.add(new Area("Polish", R.drawable.poland_large));
        areaList.add(new Area("Portuguese", R.drawable.portugal_large));
        areaList.add(new Area("Russian", R.drawable.russia_large));
        areaList.add(new Area("Spanish", R.drawable.spain_large));
        areaList.add(new Area("Thai", R.drawable.thailand_large));
        areaList.add(new Area("Tunisian", R.drawable.tunisia_large));
        areaList.add(new Area("Turkish", R.drawable.turkey_large));
        areaList.add(new Area("Ukrainian", R.drawable.ukraine_flag));
        areaList.add(new Area("Uruguayan", R.drawable.uruguay_large));
        areaList.add(new Area("Vietnamese", R.drawable.vietnam_large));
    }


    private void loadCategories() {
        categoryService.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> resp) {
                if (resp.body() != null && resp.body().getCategories() != null) {
                    categories.clear();
                    categories.addAll(resp.body().getCategories());
                    if (tabLayout.getSelectedTabPosition() == 1) {
                        adapter.setItems(categories);
                    }
                }
            }
            @Override public void onFailure(Call<CategoryResponse> c, Throwable t) {}
        });
    }

    private void loadAreas() {
        Log.i("LoadingAreas","hello");
        adapter.setItems(areaList);
    }

    private void loadIngredients() {
        ingrediantService.getAllIngredients().enqueue(new Callback<IngrediantResponse>() {
            @Override public void onResponse(Call<IngrediantResponse> call, Response<IngrediantResponse> resp) {
                if (resp.body() != null && resp.body().getIngredients() != null) {
                    ingredientNames.clear();
                    for (Ingrediant i : resp.body().getIngredients()) {
                        ingredientNames.add(i.getStrIngredient());
                    }
                    if (tabLayout.getSelectedTabPosition() == 3) {
                        adapter.setItems(ingredientNames);
                    }
                }
            }
            @Override public void onFailure(Call<IngrediantResponse> c, Throwable t) {}
        });
    }

    private void performMealSearch(String query) {
        // 🔸 Change: List now holds Meal objects instead of just names
        List<Meal> meals = new ArrayList<>();

        if (query.isEmpty()) {
            adapter.setMeals(meals);  // 🔸 Updated to pass Meal list
            return;
        }

        mealService.getMealByName(query).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> resp) {
                if (resp.body() != null && resp.body().getMeals() != null) {
                    meals.addAll(resp.body().getMeals());  // 🔸 Add full Meal objects
                }
                adapter.setMeals(meals);  // 🔸 Updated to pass Meal list
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                adapter.setMeals(meals);  // 🔸 Updated to pass empty Meal list
            }
        });
    }


    /**
     * Our generic adapter holds either:
     *  - List<String> (meals / areas / ingredients)
     *  - List<Category> (categories)
     */
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VH> {
        private List<?> items = new ArrayList<>();
        private boolean showingMeals = false;
        private final List<Meal> mealResults = new ArrayList<>();

        void setItems(List<?> data) {
            items = data;
            showingMeals = (data != null && !data.isEmpty() && data.get(0) instanceof Meal);
            notifyDataSetChanged();
        }

        void setMeals(List<Meal> meals) {
            mealResults.clear();
            mealResults.addAll(meals);
            showingMeals = true;
            notifyDataSetChanged();
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_selected_list, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int pos) {
            int tab = tabLayout.getSelectedTabPosition();

            if (showingMeals) {
                Meal meal = mealResults.get(pos);
                holder.txtItemName.setText(meal.getStrMeal());
                Glide.with(holder.imgItem.getContext())
                        .load(meal.getStrMealThumb())
                        .into(holder.imgItem);

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), MealActivity.class);
                    intent.putExtra("mealId", meal.getIdMeal());
                    startActivity(intent);
                });
            } else {
                holder.itemView.setOnClickListener(null); // remove click
                if (tab == 1) {
                    Category cat = (Category) items.get(pos);
                    holder.txtItemName.setText(cat.getStrCategory());
                    Glide.with(holder.imgItem.getContext())
                            .load(cat.getStrCategoryThumb())
                            .into(holder.imgItem);
                    holder.itemView.setOnClickListener(v -> fetchMealsByCategory(cat.getStrCategory()));
                } else if (tab == 2) {
                    Area area = (Area) items.get(pos);
                    holder.txtItemName.setText(area.getStrArea());
                    holder.imgItem.setImageResource(area.getIdAreaImg());
                    holder.itemView.setOnClickListener(v -> fetchMealsByArea(area.getStrArea()));
                } else if (tab == 3) {
                    String ing = (String) items.get(pos);
                    holder.txtItemName.setText(ing);
                    String url = "https://www.themealdb.com/images/ingredients/"
                            + ing.replace(" ", "%20") + "-Small.png";
                    Glide.with(holder.imgItem.getContext()).load(url).into(holder.imgItem);
                    holder.itemView.setOnClickListener(v -> fetchMealsByIngredient(ing));
                }
            }
        }

        @Override
        public int getItemCount() {
            return showingMeals ? mealResults.size() : (items != null ? items.size() : 0);
        }

        class VH extends RecyclerView.ViewHolder {
            ImageView imgItem;
            TextView txtItemName;
            VH(View itemView) {
                super(itemView);
                imgItem     = itemView.findViewById(R.id.imgItem);
                txtItemName = itemView.findViewById(R.id.txtItemName);
            }
        }
    }
}
