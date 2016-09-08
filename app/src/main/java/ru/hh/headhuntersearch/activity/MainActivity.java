package ru.hh.headhuntersearch.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.hh.headhuntersearch.R;
import ru.hh.headhuntersearch.fragment.VacanciesFragment;

public class MainActivity extends AppCompatActivity {

    private VacanciesFragment vacanciesFragment;

    /**
     * May be null if android < 3.0
     */
    @Nullable
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();

        vacanciesFragment = (VacanciesFragment) fragmentManager.findFragmentByTag(VacanciesFragment.TAG);
        if (vacanciesFragment == null) {
            vacanciesFragment = VacanciesFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.content, vacanciesFragment, VacanciesFragment.TAG)
                    .commit();
        }

        handleIntent(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query != null) {
                if (searchView != null) {
                    searchView.setQuery(query, false);
                }
                vacanciesFragment.onNewSearchRequest(query);
            }
        }

    }

}
