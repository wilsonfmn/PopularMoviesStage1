package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        View rootView = inflater.inflate(R.layout.detail_body_fragment, container, false);
        rootView.setTag(TAG);

        ImageView posterView = rootView.findViewById(R.id.poster_detail_view);

        Intent intent = getActivity().getIntent();
        Movie movie = (Movie) intent.getParcelableExtra(getString(R.string.movie_parameter));

        Picasso.get()
                .load(movie.getPosterPath())
                .resize(getResources().getInteger(R.integer.posterWidth),
                        getResources().getInteger(R.integer.posterHeight))
                .error(R.drawable.image_not_found)
                .placeholder(R.drawable.loading_icon)
                .into(posterView);

        return rootView;
    }

}
