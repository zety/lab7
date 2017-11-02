package com.example.user.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;

    List<Artist> artistList;
    ListView listViewArtists;

    //Database reference object
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create database reference
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        //get values from XML
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonAdd = (Button) findViewById(R.id.buttonAddArtist);

        listViewArtists = (ListView) findViewById(R.id.ListViewArtist); artistList = new ArrayList<>();
        //attach clicklistener to the button
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });


    }
    @Override

    protected void onStart() {


        super.onStart();

        //attaching value event listener

        databaseArtists.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {



                //clearing the previous artist list

                artistList.clear();



                //iterating through all the nodes

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //getting artist

                    Artist artist = postSnapshot.getValue(Artist.class);

                    //adding artist to the list

                    artistList.add(artist);

                }



                //creating adapter

                ArtistList artistAdapter = new ArtistList(MainActivity.this, artistList);

                //attaching adapter to the listview

                listViewArtists.setAdapter(artistAdapter);

            }



            @Override

            public void onCancelled(DatabaseError databaseError) {



            }

        });

    }
        private void addArtist() {
            //get artistname and convert to string from editextname
            String name = editTextName.getText().toString().trim();

            //check if the name is not empty
            if (!TextUtils.isEmpty(name)) {
                //if exist push data to firebase database
                //store inside id in database
                //every time data stored the id will be unique
                String id = databaseArtists.push().getKey();
                //store
                Artist artist = new Artist(id, name);
                //store artist inside unique id
                databaseArtists.child(id).setValue(artist);
                Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();

            } else {
                //if the name is empty
                //if the value is not given displaying a toast
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            }

        }

    }



