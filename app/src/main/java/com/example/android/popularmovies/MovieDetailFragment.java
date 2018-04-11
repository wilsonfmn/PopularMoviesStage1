package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    public MovieDetailFragment() {
        // nada
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        // Com inflate eu consigo montar a hierarquia de layout
        View linearView = inflater.inflate(R.layout.detail_body_fragment, container, false);
        linearView.setTag(TAG);

        ImageView posterView = linearView.findViewById(R.id.poster_detail_view);

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.movie_parameter));

        Picasso.get()
                .load(movie.getPosterPath())
                .resize(getResources().getInteger(R.integer.posterWidth),
                        getResources().getInteger(R.integer.posterHeight))
                .into(posterView);

        TextView releaseDateView = linearView.findViewById(R.id.release_date_text);
        releaseDateView.setText(movie.getReleaseDate());

        TextView ratingView = linearView.findViewById(R.id.vote_avg_text);
        ratingView.setText(String.valueOf(movie.getVoteAVG()));

        TextView overviewView = linearView.findViewById(R.id.overview_text);
        overviewView.setText(movie.getOverview());

        TextView titleView = linearView.findViewById(R.id.title_text);
        titleView.setText(movie.getTitle());

        return linearView;
    }

}
