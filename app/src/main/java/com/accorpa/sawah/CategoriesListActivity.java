package com.accorpa.sawah;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.models.Category;

public class CategoriesListActivity extends BaseActivity implements RecycleAdapterListener {


    private String cityID;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Category[] categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(position % 3 == 0) return 2;
                else return 1;
            }
        };

        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
//        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);




        cityID = (String) getIntent().getSerializableExtra(DataHandler
                .getInstance(this.getApplicationContext()).CITY_ID_KEY);
//
//        mListView = (GridView) findViewById(R.id.list);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
//                Category selectedCatgory = (Category) mListView.getAdapter().getItem(position);
//
//                NavigationHandler.getInstance().startPlacesListActivity(CategoriesListActivity.this,
//                        selectedCatgory.getCategoryID(), cityID);
//            }
//        });
        DataHandler.getInstance(getApplicationContext()).requestCategoriesArray(this);
        showProgress(true);
    }



    public void receiveCategorizesList(Category[] categoryList) {

        this.categories = categoryList;
        mAdapter = new CategoriesRecycleAdapter(this, this, categoryList);
        mRecyclerView.setAdapter(mAdapter);
        showProgress(false);

    }

    @Override
    public void itemSelected(Object object) {
        NavigationHandler.getInstance().startPlacesListActivity(CategoriesListActivity.this,
                ((Category) object).getCategoryID(), cityID, ((Category) object).getName());
    }

    @Override
    protected String getToolbarTitle() {
        return DataHandler.getInstance(this).getDefaultCity().getCityNameAR();
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.search_back_tool_bar;
    }

    protected int getLayoutResourceId() {
        return R.layout.fragment_place_list;
    }

}
