package com.greenhill.orderingsystems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenhill.orderingsystems.Common.Common;
import com.greenhill.orderingsystems.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    private EditText editMobileNum, editPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editMobileNum = findViewById(R.id.editMobileNum);
        editPassword = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        //firebase
        FirebaseApp.initializeApp(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //loading
                final ProgressDialog dialog = new ProgressDialog(SignIn.this);
                dialog.setMessage("Please Wait...");
                dialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check to see if user exists in database
                        if (dataSnapshot.child(editMobileNum.getText().toString()).exists()) {

                            //close loading box
                            dialog.dismiss();

                            //get user info
                            User user = dataSnapshot.child(editMobileNum.getText().toString()).getValue(User.class);
                            user.setPhone(editMobileNum.getText().toString()); //set phone
                            if (user.getPassword().equals(editPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this,HomeNavigation.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }else
                        {
                            Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


}
