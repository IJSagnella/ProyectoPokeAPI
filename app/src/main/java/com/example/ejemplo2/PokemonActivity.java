package com.example.ejemplo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {


    LinearLayout linear1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        Button volver = findViewById(R.id.buttonVolver);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PokemonActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String url = bundle.getString("url");

            getPoke(url);
        }

    }

    private void getPoke(String url){


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");

                    JSONArray stat = jsonObject.getJSONArray("stats");
                    int atk = stat.getJSONObject(1).getInt("base_stat");
                    int def = stat.getJSONObject(2).getInt("base_stat");
                    int vel = stat.getJSONObject(5).getInt("base_stat");

                    Pokemon pokemon = new Pokemon(id, name, atk, def, vel);

                    completarInfo(pokemon);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void completarInfo(Pokemon pokemon){
        TextView textName = findViewById(R.id.textNombre);
        TextView textNum = findViewById(R.id.textNumero);
        TextView textAtk = findViewById(R.id.textAtk);
        TextView textDef = findViewById(R.id.textDef);
        TextView textVel = findViewById(R.id.textVel);

        textName.setText("Nombre: "+pokemon.getNombre());
        textNum.setText("Numero: "+pokemon.getId());
        textAtk.setText("Ataque: "+pokemon.getAtaque());
        textDef.setText("Defensa: "+pokemon.getDefensa());
        textVel.setText("Velocidad: "+pokemon.getVelocidad());

        ImageView imgView = findViewById(R.id.imageView);

        Glide.with(this)
                .asBitmap()
                .load(pokemon.getImgURL())
                .into(imgView);
    }
}