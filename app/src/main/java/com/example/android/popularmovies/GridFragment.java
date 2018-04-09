package com.example.android.popularmovies;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willf on 26/03/2018.
 */

public class GridFragment extends Fragment {

    private static final String TAG = GridFragment.class.getSimpleName();

    protected RecyclerView recyclerView;
    protected GridMovieAdapter gridAdapter;
    protected RecyclerView.LayoutManager layoutManager;

    public GridFragment() {
        // construtor vazio para ser instanciado automaticamente
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        // Com inflate eu consigo montar a hierarquia de layout
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        rootView.setTag(TAG);

        recyclerView = rootView.findViewById(R.id.movie_grid);

        this.getMoviesFromTMDb("");
        return rootView;
    }

    /**
     * If device has Internet the magic happens when app launches. The app will start the process
     * of collecting data from the API and present it to the user.
     * <p/>
     * If the device has no connectivity it will display a Toast explaining that app needs
     * Internet to work properly.
     *
     * @param sortMethod tmdb API method for sorting movies
     */
    private void getMoviesFromTMDb(String sortMethod) {
        if (isNetworkAvailable()) {
            // Recupera a chave para pergar dados do TMDb
            String apiKey = getString(R.string.key_themoviedb);

            // Chamada da AsyncTask para recuperar os dados
            MovieFetcherAsyncTask movieTask = new MovieFetcherAsyncTask(apiKey);
            movieTask.execute(sortMethod);
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verifica se há conexão à internet
     *
     * @return True se há conexão, false caso contrário.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Inner class de asynctask para realizar a busca e parser do JSON de filmes.
     * Apesar de não ser reutilizável, ela evita que haja memory leaks, ficando mais fácil de gerenciar nessa simples aplicação.
     */
    public class MovieFetcherAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        private String movieAPIKey;

        public MovieFetcherAsyncTask(String apiKey) {
            super();
            this.movieAPIKey = apiKey;
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr;

            try {
                // Criando a URL de acesso à API de filmes
                String[] urlParameters = new String[2];
                urlParameters[0] = "popularity.desc";
                URL url = getApiUrl(urlParameters);

                // Abre a coneção com a API
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Transformando o inputstream de dados em uma string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Inserindo \n para facilitar a leitura
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Sem dados recebidos
                    return null;
                }
                movieJsonStr = buffer.toString();

                Log.v(TAG, "Movie JSON String" + movieJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // Erro de acesso à API
                return null;
            } finally{
                // término da leitura, fechando a conexão
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return movieListJSONReader(movieJsonStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                // Atualizando a view com os filmes recebidos
                gridAdapter = new GridMovieAdapter(movies);

                // inicializando o GridLayouManager com a list de filmes
                layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(gridAdapter);
            }
        }

        private URL getApiUrl(String[] parameters) throws MalformedURLException {
            final String TMDB_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, parameters[0])
                    .appendQueryParameter(API_KEY_PARAM, this.movieAPIKey)
                    .build();

            return new URL(builtUri.toString());
        }

        private List<Movie> movieListJSONReader(String moviesListJson) throws JSONException {
            // JSON tags
            final String TAG_ID = "id";
            final String TAG_ORIGINAL_TITLE = "original_title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_RESULTS = "results";
            final String TAG_TITLE = "title";
            final String TAG_VOTE_AVG = "vote_average";
            final String TAG_VOTE_COUNT = "vote_count";
            final String TAG_POPULARITY = "popularity";

            // Pegando o arrayJson que contém as informações recebidas
            JSONObject moviesJson = new JSONObject(moviesListJson);
            JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

            // Criando a lista de Movies a ser devolvida
            List<Movie> movieList = new ArrayList<>();

            //Percorre o arrayJSON e popula os dados extraídos
            for (int i = 0; i < resultsArray.length(); i++) {
                Movie movieSample = new Movie();

                JSONObject movieInfo = resultsArray.getJSONObject(i);

                // Armazenando os dados do JSON
                movieSample.setMovieId(movieInfo.getInt(TAG_ID));
                movieSample.setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
                movieSample.setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
                movieSample.setOverview(movieInfo.getString(TAG_OVERVIEW));
                movieSample.setTitle(movieInfo.getString(TAG_TITLE));
                movieSample.setVoteAVG(movieInfo.getDouble(TAG_VOTE_AVG));
                movieSample.setVoteCount(movieInfo.getInt(TAG_VOTE_COUNT));
                movieSample.setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
                movieSample.setPopularity(movieInfo.getDouble(TAG_POPULARITY));

                movieList.add(movieSample);
            }

            return movieList;
        }

    }

}
