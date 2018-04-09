package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Tela principal do aplicativo. Aqui será inserido o Fragment contendo a listagem de filmes e também o menu de opções.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, new GridFragment())
                    .commit();
        }
    }

    // implementar o menu de opções de sort

}
