package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private RecyclerView recyclerView;
    private GridMovieAdapter gridAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public GridFragment() {
        // construtor vazio para ser instanciado automaticamente
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        // Aviso que tenho um menu de opções a ser acrescentado
        setHasOptionsMenu(true);

        // Com inflate eu consigo montar a hierarquia de layout
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        rootView.setTag(TAG);

        recyclerView = rootView.findViewById(R.id.movie_grid);

        // verifica se as preferências (de sort) já foram salvas
        this.getMoviesFromTMDb(getSortingPref(), 1);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_grid_frag, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //case para avaliar qual opção selecionada
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                // atualiza a preferência de ordenação do usuário
                updateSharedPrefs(getString(R.string.sort_by_pop));
                // atualiza a view
                getMoviesFromTMDb(getSortingPref(), 1);
                return true;
            case R.id.sort_rating:
                updateSharedPrefs(getString(R.string.sort_by_vote_avg));
                getMoviesFromTMDb(getSortingPref(), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Verifica se há conexão com a internet e executa a chamada à API do TMDb.
     * Caso não haja conexão, exibe um Toast para o usuário informando.
     *
     * @param sortMethod o tipo de ordenação a ser utilizado (popularity ou user-rating)
     */
    private void getMoviesFromTMDb(String sortMethod, int pageNumber) {
        if (isNetworkAvailable()) {
            // Recupera a chave para pergar dados do TMDb
            String apiKey = getString(R.string.key_themoviedb);

            // Chamada da AsyncTask para recuperar os dados
            MovieFetcherAsyncTask movieTask = new MovieFetcherAsyncTask(apiKey);
            movieTask.execute(sortMethod, String.valueOf(pageNumber));
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
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Retorna o método de ordenação da lista de filmes. Se o usuário
     * não tiver escolhido nenhum, retorna o padrão: popularidade.
     * @return O método de ordenação
     */
    private String getSortingPref() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        return prefs.getString(getString(R.string.pref_sort_method),
                getString(R.string.sort_by_pop));
    }

    /**
     * Salva o método de Sort escolhido pelo usuário
     * @param sortingMethod O método de ordenação escolhido
     */
    private void updateSharedPrefs(String sortingMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_sort_method), sortingMethod);
        editor.apply();
    }

    /**
     * Inner class de asynctask para realizar a busca e parser do JSON de filmes.
     * Apesar de não ser reutilizável, ela evita que haja memory leaks, ficando mais fácil de gerenciar nessa simples aplicação.
     */
    public class MovieFetcherAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        private String movieAPIKey;

        MovieFetcherAsyncTask(String apiKey) {
            super();
            this.movieAPIKey = apiKey;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr;

            try {
                // Criando a URL de acesso à API de filmes
                URL url = getApiUrl(params);

                // Abre a coneção com a API
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Transformando o inputstream de dados em uma string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Inserindo \n para facilitar a leitura
                    stringBuilder.append(line + "\n");
                }

                if (stringBuilder.length() == 0) {
                    // Sem dados recebidos
                    return null;
                }
                movieJsonStr = stringBuilder.toString();

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

        /**
         * Monta a URL da API TMDb baseado no método de ordenação escolhido
         * @param parameters parâmetros a serem usados na URL
         * @return a URL de busca dos filme, de acordo com a ordenação escolhida
         * @throws MalformedURLException
         * #TODO implementar infinite scroll passando o número da página a ser buscada pela api
         */
        private URL getApiUrl(String[] parameters) throws MalformedURLException {
            final String TMDB_BASE_URL = getString(R.string.tmdb_base_url);
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String NO_ADULTS_PARAM = "include_adult";
            final String PAGE_PARAM = "page";

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, parameters[0])
                    .appendQueryParameter(NO_ADULTS_PARAM, "false")
                    .appendQueryParameter(API_KEY_PARAM, this.movieAPIKey)
                    .build();

            return new URL(builtUri.toString());
        }

        /**
         * Transforma o JSON de filmes recebidos em entidades bem formadas
         * @param moviesListJson A String JSON de filmes
         * @return A lista de entidades Movie
         * @throws JSONException
         */
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
