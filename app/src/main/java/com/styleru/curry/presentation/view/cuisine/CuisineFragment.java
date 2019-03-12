package com.styleru.curry.presentation.view.cuisine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.styleru.curry.CurryApplication;
import com.styleru.curry.R;
import com.styleru.curry.domain.cuisine.models.CuisineRecipes;
import com.styleru.curry.presentation.presenter.cuisine.CuisinePresenter;
import com.styleru.curry.presentation.view.cuisine.adapter.CuisineRecyclerOnClick;
import com.styleru.curry.presentation.view.cuisine.adapter.CuisineRecyclerViewAdapter;

import javax.inject.Inject;

import androidx.navigation.Navigation;

/*
 * Фрагмент, на котором отображаются списки с рецептами определенных кухонь мира (первая вкладка нижнего меню)
 */
public class CuisineFragment extends Fragment implements CuisineRecyclerOnClick, CuisineView {

    public static final String ID_KEY = "recipeId";

    private RecyclerView cuisineRecyclerViewOne;
    private RecyclerView cuisineRecyclerViewTwo;
    private RecyclerView cuisineRecyclerViewThree;

    private CuisineRecyclerViewAdapter adapterOne;
    private CuisineRecyclerViewAdapter adapterTwo;
    private CuisineRecyclerViewAdapter adapterThree;

    private ImageButton search;
    private TextView cuisineTitleOne;
    private TextView cuisineTitleTwo;
    private TextView cuisineTitleThree;
    private ProgressBar progressBar;
    private TextView checkInternet;

    @Inject
    protected CuisinePresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initOnClicks();
        init();
        initRecyclers();
    }

    private void initViews(View view) {
        cuisineRecyclerViewOne = view.findViewById(R.id.cuisine_recipes_one);
        cuisineRecyclerViewTwo = view.findViewById(R.id.cuisine_recipes_two);
        cuisineRecyclerViewThree = view.findViewById(R.id.cuisine_recipes_three);
        cuisineTitleOne = view.findViewById(R.id.search_recipe_first_title);
        cuisineTitleTwo = view.findViewById(R.id.search_recipe_second_title);
        cuisineTitleThree = view.findViewById(R.id.search_recipe_third_title);
        search = view.findViewById(R.id.search);
        progressBar = view.findViewById(R.id.cuisine_progress_bar);
        checkInternet = view.findViewById(R.id.cuisine_check_internet);
    }

    private void init() {
        // Проводим инъекцию
        CurryApplication.getCuisineComponent().inject(this);

        presenter.attachView(this);
        presenter.getCuisineRecipesOne();
        presenter.getCuisineRecipesTwo();
        presenter.getCuisineRecipesThree();
    }

    private void initOnClicks(){
        search.setOnClickListener(view -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_cuisineFragment_to_searchFragment);
        });
    }

    private void initRecyclers(){
        adapterOne = new CuisineRecyclerViewAdapter(this);
        cuisineRecyclerViewOne.setAdapter(adapterOne);

        adapterTwo = new CuisineRecyclerViewAdapter(this);
        cuisineRecyclerViewTwo.setAdapter(adapterTwo);

        adapterThree = new CuisineRecyclerViewAdapter(this);
        cuisineRecyclerViewThree.setAdapter(adapterThree);
    }

    /**
     * OnClick метод для адаптера - нажимаем на элемент и идем во фрагмент о рецепте
     * @param id id нажатого элемента, который передается во фрагмент, чтобы там отобразить подробную инфу о рецепте
     */
    @Override
    public void recyclerOnClick(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ID_KEY, id);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_cuisineFragment_to_recipeFragment, bundle);
    }

    /**
     * Обновляем данные в первом адаптере
     * @param cuisineRecipes объект с рецептами, получаемый с сервера
     */
    @Override
    public void setDataOne(CuisineRecipes cuisineRecipes) {
        progressBar.setVisibility(View.GONE);
        cuisineTitleOne.setVisibility(View.VISIBLE);
        cuisineTitleOne.setText(cuisineRecipes.getCuisine());
        adapterOne.updateData(cuisineRecipes.getShortRecipes());    }

    /**
     * Обновляем данные во втором адаптере
     * @param cuisineRecipes объект с рецептами, получаемый с сервера
     */
    @Override
    public void setDataTwo(CuisineRecipes cuisineRecipes) {
        progressBar.setVisibility(View.GONE);
        cuisineTitleTwo.setVisibility(View.VISIBLE);
        cuisineTitleTwo.setText(cuisineRecipes.getCuisine());
        adapterTwo.updateData(cuisineRecipes.getShortRecipes());
    }

    /**
     * Обновляем данные в третьем адаптере
     * @param cuisineRecipes объект с рецептами, получаемый с сервера
     */
    @Override
    public void setDataThree(CuisineRecipes cuisineRecipes) {
        progressBar.setVisibility(View.GONE);
        cuisineTitleThree.setVisibility(View.VISIBLE);
        cuisineTitleThree.setText(cuisineRecipes.getCuisine());
        adapterThree.updateData(cuisineRecipes.getShortRecipes());
    }

    /**
     * Уведомить пользователя об ошибке
     */
    @Override
    public void showError() {
        Toast.makeText(getContext(), getResources().getText(R.string.error), Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        checkInternet.setVisibility(View.VISIBLE);
    }

}