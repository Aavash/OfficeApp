package com.example.antivenom.myoffice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    ListView addPageListView;
    SqlHandler sqlHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addPageListView = (ListView) findViewById(R.id.addPageListView);
        sqlHandler = new SqlHandler(this);
        showlist();


        ///Delete module
        addPageListView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                ContactListItems contactListItems = (ContactListItems)arg0.getItemAtPosition(arg2);
                String slno = contactListItems.getSlno();
                String delQuery = "DELETE FROM PHONE_CONTACTS WHERE slno='"+slno+"' ";
                sqlHandler.executeQuery(delQuery);
                showlist();

                return false;
            }
        });

        addPageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ContactListItems contactListItems = (ContactListItems)arg0.getItemAtPosition(arg2);
                String slno = contactListItems.getSlno();

                Intent intent= new Intent(AddActivity.this,UpdateActivity.class);
                intent.putExtra("rowid",slno);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        //fab button module
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }





        });



    }

    public void showlist() {

        ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
        contactList.clear();
        String query = "SELECT * FROM PHONE_CONTACTS ";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setSlno(c1.getString(c1
                            .getColumnIndex("slno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("name")));
                    contactListItems.setPhone(c1.getString(c1
                            .getColumnIndex("phone")));
                    contactList.add(contactListItems);

                } while (c1.moveToNext());
            }
        }
        c1.close();

        ContactListAdapter contactListAdapter = new ContactListAdapter(
                AddActivity.this, contactList);
        addPageListView.setAdapter(contactListAdapter);

    }

}
