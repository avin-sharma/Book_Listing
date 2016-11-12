package com.avinsharma.booklisting;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    String search;
    ProgressBar progressBar;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        search = getIntent().getExtras().getString("search");
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        empty = (TextView) findViewById(R.id.empty);
        getLoaderManager().initLoader(0,null,this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(getApplicationContext(),search);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        progressBar.setVisibility(View.GONE);
        updateUi(books);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        updateUi(new ArrayList<Book>());
    }

    private void updateUi(ArrayList<Book> books) {

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView) findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakes
        BookAdapter adapter = new BookAdapter(this,books);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            empty.setText("No data found");
        }
        else{
            empty.setText("No network");
        }
        listView.setEmptyView(empty);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(adapter);
    }

    private static class BookLoader extends AsyncTaskLoader<ArrayList<Book>>{
        String search;
        public BookLoader(Context context, String search) {
            super(context);
            this.search = search;
        }

        @Override
        public ArrayList<Book> loadInBackground() {
            return QueryUtils.fetchSearchResults(search);
        }
    }
}
