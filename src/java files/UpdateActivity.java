package com.example.antivenom.myoffice;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView updateList;
    Button updateButton;
    EditText nameText;
    EditText phoneText;
    static String rowid;
    String tempSlno, tempName, tempPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nameText = (EditText) findViewById(R.id.updateName);
        phoneText= (EditText) findViewById(R.id.updatePhone);
        updateButton =(Button) findViewById(R.id.updateButton);
        updateList = (ListView) findViewById(R.id.updateList);
        sqlHandler = new SqlHandler(this);

        Intent intent = getIntent();
        rowid  = intent.getStringExtra("rowid");
        editTextDisplay();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName= nameText.getText().toString();
                String newPhone = phoneText.getText().toString();

               sqlHandler.executeQuery("UPDATE PHONE_CONTACTS SET name = '" + newName + "' ,  phone = '" +newPhone + "' where slno = " + rowid + ";");
                nameText.setText("");
                phoneText.setText("");

                Intent intent= new Intent(UpdateActivity.this,AddActivity.class);
                startActivity(intent);


            }
        });

    }

    public void editTextDisplay() {

        ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
        contactList.clear();
        String query = "SELECT * FROM PHONE_CONTACTS where slno  ="  + rowid + ";";
        Cursor c1 = sqlHandler.selectQuery(query);
        
                    ContactListItems contactListItems = new ContactListItems();

                    tempSlno = contactListItems.getSlno();
                    tempName= contactListItems.getName();
                    tempPhone = contactListItems.getPhone();
                    contactList.add(contactListItems);


        c1.close();

        nameText.setText(tempName);
        phoneText.setText(tempPhone);

    }


}
