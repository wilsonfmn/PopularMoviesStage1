package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para fazer a transição de dados para a UI (GridView).
 * Precisa ter um ViewHolder associado.
 * @Overrides: onCreateViewHolder, chamado sempre que o ViewHolder for criado.
 * @Overrides: onBindViewHolder, chamado sempre que a conexão entre dados e UI é feita
 *
 * Created by willf on 25/03/2018.
 */
public class GridMovieAdapter extends RecyclerView.Adapter<GridMovieAdapter.GridMovieHolder> {

    List<Movie> movieGrid;

    public GridMovieAdapter(List<Movie> movieList) {
        this.movieGrid = movieList;
    }

    @Override
    public int getItemCount() {
        return this.movieGrid.size();
    }

    @Override
    public void onBindViewHolder(GridMovieHolder gridMovieHolder, int position) {
        Movie movie = movieGrid.get(position);

        gridMovieHolder.posterTitle.setText(movie.getTitle());
        gridMovieHolder.posterImage.setImageURI(Uri.parse(movie.getPosterPath()));
    }

    @Override
    public GridMovieHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.grid_item_movie, viewGroup, false);
        return new GridMovieHolder(itemView);
    }

    public static class GridMovieHolder extends RecyclerView.ViewHolder {

        protected ImageView posterImage;
        protected TextView posterTitle;

        public GridMovieHolder(View view) {
            super(view);
            posterImage = (ImageView) view.findViewById(R.id.poster_image_id);
            posterTitle = (TextView) view.findViewById(R.id.poster_label_id);
        }

    }

}
