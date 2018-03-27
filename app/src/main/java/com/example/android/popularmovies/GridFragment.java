package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public GridFragment() {
        // construtor vazio para ser instanciado automaticamente
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        // Com inflate eu consigo montar a hierarquia de layout
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        // criando e populando a lista de filmes
        List<Movie> movieList = new ArrayList<>();

        movieList = generateFodderMovies();

        RecyclerView recList = (RecyclerView) container.findViewById(R.id.movie_grid);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(container.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        return rootView;
    }

    private List<Movie> generateFodderMovies() {
        List<Movie> mahMovies = new ArrayList<>();

        Movie fakeMovie1 = new Movie();
        fakeMovie1.setMovieId(1);
        fakeMovie1.setOriginalTitle("Titulo estrangeiro 1");
        fakeMovie1.setTitle("Titulo Traduzido 1");
        fakeMovie1.setPosterPath("");

    }

}
