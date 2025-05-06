package com.example.meal;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
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
    private TextInputEditText etSearch;
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
    private final List<Area> areaNames         = new ArrayList<>();
    private final List<String> ingredientNames = new ArrayList<>();
    // Map area name -> drawable resource
    private final Map<String,Integer> areaFlagMap = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        etSearch  = root.findViewById(R.id.etSearch);
        rvResults = root.findViewById(R.id.rvSearchResults);

        setupAreaFlagMap();
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
        loadAreas();
        loadIngredients();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                etSearch.setText("");
                switch (tab.getPosition()) {
                    case 0: // Meal
                        adapter.setItems(mealNames);
                        etSearch.setHint("Search by meal...");
                        break;
                    case 1: // Category
                        adapter.setItems(categories);
                        etSearch.setHint("Search by category...");
                        break;
                    case 2: // Area
                        adapter.setItems(areaNames);
                        etSearch.setHint("Search by area...");
                        break;
                    case 3: // Ingredient
                        adapter.setItems(ingredientNames);
                        etSearch.setHint("Search by ingredient...");
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        tabLayout.getTabAt(0).select();

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performMealSearch(etSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        return root;
    }
    // Build areaFlagMap using your Area names directly—no Country class needed
    private void setupAreaFlagMap() {
        areaFlagMap.clear();
        areaFlagMap.put("Egyptian",    R.drawable.egypt_large);
        areaFlagMap.put("American",    R.drawable.america_large);
        areaFlagMap.put("British",     R.drawable.british_large);
        areaFlagMap.put("Canadian",    R.drawable.canada_large);
        areaFlagMap.put("Chinese",     R.drawable.china_large);
        areaFlagMap.put("Croatian",    R.drawable.croatia_large);
        areaFlagMap.put("Dutch",       R.drawable.netherlands_large);
        areaFlagMap.put("Filipino",    R.drawable.phillippines_large);
        areaFlagMap.put("French",      R.drawable.france_large);
        areaFlagMap.put("Greek",       R.drawable.greece_large);
        areaFlagMap.put("Indian",      R.drawable.india_large);
        areaFlagMap.put("Irish",       R.drawable.ireland_large);
        areaFlagMap.put("Italian",     R.drawable.italy_large);
        areaFlagMap.put("Jamaican",    R.drawable.jamaica_large);
        areaFlagMap.put("Japanese",    R.drawable.japan_large);
        areaFlagMap.put("Kenyan",      R.drawable.kenya_large);
        areaFlagMap.put("Malaysian",   R.drawable.malaysia_large);
        areaFlagMap.put("Mexican",     R.drawable.mexico_large);
        areaFlagMap.put("Moroccan",    R.drawable.morocco_large);
        areaFlagMap.put("Polish",      R.drawable.poland_large);
        areaFlagMap.put("Portuguese",  R.drawable.portugal_large);
        areaFlagMap.put("Russian",     R.drawable.russia_large);
        areaFlagMap.put("Spanish",     R.drawable.spain_large);
        areaFlagMap.put("Thai",        R.drawable.thailand_large);
        areaFlagMap.put("Tunisian",    R.drawable.tunisia_large);
        areaFlagMap.put("Turkish",     R.drawable.turkey_large);
        areaFlagMap.put("Ukrainian",   R.drawable.ukraine_flag);
        areaFlagMap.put("Uruguayan",   R.drawable.uruguay_large);
        areaFlagMap.put("Vietnamese",  R.drawable.vietnam_large);
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
        areaService.getAllAreas().enqueue(new Callback<AreaResponse>() {
            @Override public void onResponse(Call<AreaResponse> call, Response<AreaResponse> resp) {
                if (resp.body() != null && resp.body().getAreas() != null) {
                    Log.d("AREA_DEBUG", "Loaded areas: " + resp.body().getAreas().size());
                    areaNames.clear();
                    areaNames.addAll(resp.body().getAreas());
                    if (tabLayout.getSelectedTabPosition() == 2) {
                        adapter.setItems(areaNames);
                    }
                }
            }
            @Override public void onFailure(Call<AreaResponse> c, Throwable t) {}
        });
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
        mealNames.clear();
        if (query.isEmpty()) {
            adapter.setItems(mealNames);
            return;
        }
        mealService.getMealByName(query).enqueue(new Callback<MealResponse>() {
            @Override public void onResponse(Call<MealResponse> call, Response<MealResponse> resp) {
                if (resp.body() != null && resp.body().getMeals() != null) {
                    for (Meal m : resp.body().getMeals()) {
                        mealNames.add(m.getStrMeal());
                    }
                }
                adapter.setItems(mealNames);
            }
            @Override public void onFailure(Call<MealResponse> call, Throwable t) {
                adapter.setItems(mealNames);
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

        void setItems(List<?> data) {
            items = data;
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

            if (tab == 1) {
                // Category tab
                Category cat = (Category) items.get(pos);
                holder.txtItemName.setText(cat.getStrCategory());
                Glide.with(holder.imgItem.getContext())
                        .load(cat.getStrCategoryThumb())
                        .into(holder.imgItem);
            } else if (tab == 2) {
                // Area tab
                Area area = (Area) items.get(pos);
                holder.txtItemName.setText(area.getStrArea());
                Integer flagRes = areaFlagMap.get(area.getStrArea());
                Glide.with(holder.imgItem.getContext())
                        .load(flagRes)
                        .into(holder.imgItem);
//                if (flagRes != null) {
//                    holder.imgItem.setImageResource(flagRes);
//                }
            } else {
                // Meals (tab 0) or Ingredients (tab 3)
                String name = (String) items.get(pos);
                holder.txtItemName.setText(name);
                if (tab == 3) {
                    String url = "https://www.themealdb.com/images/ingredients/"
                            + name.replace(" ", "%20") + "-Small.png";
                    Glide.with(holder.imgItem.getContext()).load(url).into(holder.imgItem);
                }
            }
        }

        @Override public int getItemCount() {
            if(items != null) {
                return items.size();
            } else {
                return 0;
            }
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
