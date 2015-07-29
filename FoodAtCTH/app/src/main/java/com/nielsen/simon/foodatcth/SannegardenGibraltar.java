package com.nielsen.simon.foodatcth;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nielsen.simon.foodatcth.adapters.PizzaMenuAdapter;
import com.nielsen.simon.foodatcth.database.DbHandler;

import java.util.ArrayList;

/**
 * Created by Simon on 2015-07-20.
 */
public class SannegardenGibraltar extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Pizza> menu;
    private DbHandler dbHandler;

    private RecyclerView menuRecyclerView;
    private PizzaMenuAdapter menuAdapter;
    private RecyclerView.LayoutManager menuLayoutManager;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sannegarden_gibraltar);

        toolbar = (Toolbar) findViewById(R.id.pizza_app_bar);
        setSupportActionBar(toolbar);

        //Set up basic menu ------------------------------------------------------

        menuRecyclerView = (RecyclerView) findViewById(R.id.MenuRecyclerView);

        menuAdapter = new PizzaMenuAdapter();
        menuRecyclerView.setAdapter(menuAdapter);
        //menuRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        menuLayoutManager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(menuLayoutManager);
        // End set up basic menu -------------------------------------------------

        dbHandler = new DbHandler(this);
        menu = dbHandler.getPizzaMenu(DbHandler.PizzaMenu.SANNE_GIBRALTAR);
        menuAdapter.setMenu(menu);
    }

    public void onBackPressed() {
        if(isSearchOpened) {
            closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pizza, menu);
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open
            editSearch.setText("");
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title
            findViewById(R.id.backSearch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeSearch();
                }
            });

            editSearch = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });
            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    menu = dbHandler.getPizzaMenuSearchResult(DbHandler.PizzaMenu.SANNE_GIBRALTAR, editSearch.getText().toString());
                    menuAdapter.setMenu(menu);
                    menuAdapter.notifyDataSetChanged();
                }
            });

            editSearch.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear));
            isSearchOpened = true;
        }
    }

    private void hideKeyboard(){
        //hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }

    private void closeSearch(){
        Log.v("search", "In close search");
        ActionBar action = getSupportActionBar(); //get the actionbar
        action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
        action.setDisplayShowTitleEnabled(true); //show the title in the action bar

        //hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

        //add the search icon in the action bar
        mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

        menu = dbHandler.getPizzaMenu(DbHandler.PizzaMenu.SANNE_GIBRALTAR);
        menuAdapter.setMenu(menu);
        menuAdapter.notifyDataSetChanged();
        Log.v("search", "After close search get db");

        isSearchOpened = false;
    }

}
