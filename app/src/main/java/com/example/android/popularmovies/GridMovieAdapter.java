package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

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

    private List<Movie> movieGrid;

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
        gridMovieHolder.posterImage.setTag(movie);

        // Carrego as imagens com o Picasso, colocando uma renderização de loading e de erro
        Context context = gridMovieHolder.getContext();
        Picasso.get()
                .load(movie.getPosterPath())
                .resize(context.getResources().getInteger(R.integer.posterWidth),
                        context.getResources().getInteger(R.integer.posterHeight))
                .into(gridMovieHolder.posterImage);
    }

    @Override
    public GridMovieHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.grid_item_movie, viewGroup, false);
        return new GridMovieHolder(itemView, this.getItem(position));
    }

    public static class GridMovieHolder extends RecyclerView.ViewHolder {

        protected ImageView posterImage;
        protected TextView posterTitle;

        public GridMovieHolder(View cardView, Movie movie) {
            super(cardView);
            posterImage = (ImageView) cardView.findViewById(R.id.poster_image_id);
            posterTitle = (TextView) cardView.findViewById(R.id.poster_label_id);
            cardView.setOnClickListener(new ImageView.OnClickListener(){
                public void onClick(View cardView) {
                    ImageView posterView = cardView.findViewById(R.id.poster_image_id);
                    Movie selectedMovie = (Movie) posterView.getTag();

                    Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                    intent.putExtra(getContext().getResources().getString(R.string.movie_parameter), selectedMovie);

                    getContext().startActivity(intent);
                }
            });
        }

        public Context getContext() {
            return posterTitle.getContext();
        }

    }

    /**
     * Recupera o item da lista de filmes
     * @param position a posição do item a ser retornado
     * @return o filme selecionado
     */
    public Movie getItem(int position) {
        return this.movieGrid.get(position);
    }

}
