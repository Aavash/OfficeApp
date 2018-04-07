package com.example.antivenom.myoffice;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    SqlHandler sqlHandler;
    ListView lvCustomList;
    EditText etName, etPhone;
    Button btnsubmit;
    Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCustomList = (ListView) findViewById(R.id.lv_custom_list);
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnsubmit = (Button) findViewById(R.id.btn_submit);
        sqlHandler = new SqlHandler(this);
        backButton = (Button) findViewById(R.id.backButtonMain);
        showlist(MainActivity.this);


        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnsubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String phoneNo = etPhone.getText().toString();

                String query = "INSERT INTO PHONE_CONTACTS(name,phone) values ('"
                        + name + "','" + phoneNo + "')";
                sqlHandler.executeQuery(query);
                showlist(MainActivity.this);
                etName.setText("");
                etPhone.setText("");

            }
        });

        lvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("rowid",position);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



        lvCustomList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                ContactListItems contactListItems = (ContactListItems)arg0.getItemAtPosition(arg2);
                String slno = contactListItems.getSlno();
                String delQuery = "DELETE FROM PHONE_CONTACTS WHERE slno='"+slno+"' ";
                sqlHandler.executeQuery(delQuery);
                showlist(MainActivity.this);

                return false;
            }
        });

    }

    public void showlist(Context context) {

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
                context, contactList);
        lvCustomList.setAdapter(contactListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
