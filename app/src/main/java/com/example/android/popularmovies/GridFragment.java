package com.example.android.popularmovies;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willf on 26/03/2018.
 */

public class GridFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_grid);

        // criando e populando a lista de filmes
        List<Movie> movieList = generateFodderMovies();
        // Setando adapter
        gridAdapter = new GridMovieAdapter(movieList);

        // inicializando o GridLayouManager com a list de filmes
        layoutManager = new GridLayoutManager(getActivity(), movieList.size());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gridAdapter);

        //RecyclerView recList = (RecyclerView) container.findViewById(R.id.movie_grid);
        //recList.setHasFixedSize(true);
        //LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //recList.setLayoutManager(llm);

        return rootView;
    }

    private List<Movie> generateFodderMovies() {
        List<Movie> mahMovies = new ArrayList<>();

        Drawable mahFakePoster = getResources().getDrawable(R.drawable.ic_launcher_foreground);

        Movie fakeMovie1 = new Movie();
        fakeMovie1.setMovieId(1);
        fakeMovie1.setOriginalTitle("Titulo estrangeiro 1");
        fakeMovie1.setTitle("Titulo Traduzido 1");
        fakeMovie1.setPosterPath("");
        fakeMovie1.setFakeMoviePoster(mahFakePoster);
        mahMovies.add(fakeMovie1);

        Movie fakeMovie2 = new Movie();
        fakeMovie2.setMovieId(2);
        fakeMovie2.setOriginalTitle("Titulo estrangeiro 2");
        fakeMovie2.setTitle("Titulo Traduzido 2");
        fakeMovie2.setPosterPath("");
        fakeMovie2.setFakeMoviePoster(mahFakePoster);
        mahMovies.add(fakeMovie2);

        Movie fakeMovie3 = new Movie();
        fakeMovie3.setMovieId(3);
        fakeMovie3.setOriginalTitle("Titulo estrangeiro 3");
        fakeMovie3.setTitle("Titulo Traduzido 3");
        fakeMovie3.setPosterPath("");
        fakeMovie3.setFakeMoviePoster(mahFakePoster);
        mahMovies.add(fakeMovie3);

        Movie fakeMovie4 = new Movie();
        fakeMovie4.setMovieId(4);
        fakeMovie4.setOriginalTitle("Titulo estrangeiro 4");
        fakeMovie4.setTitle("Titulo Traduzido 4");
        fakeMovie4.setPosterPath("");
        fakeMovie4.setFakeMoviePoster(mahFakePoster);
        mahMovies.add(fakeMovie4);

        Movie fakeMovie5 = new Movie();
        fakeMovie5.setMovieId(5);
        fakeMovie5.setOriginalTitle("Titulo estrangeiro 5");
        fakeMovie5.setTitle("Titulo Traduzido 5");
        fakeMovie5.setPosterPath("");
        fakeMovie5.setFakeMoviePoster(mahFakePoster);
        mahMovies.add(fakeMovie5);

        return mahMovies;
    }

}
