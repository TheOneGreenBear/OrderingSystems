package com.greenhill.orderingsystems;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.greenhill.orderingsystems.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;


public class SignUp extends AppCompatActivity {

    private MaterialEditText editPassword, editName, editMobileNum;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        editMobileNum = findViewById(R.id.editMobileNum);
        btnSignUp = findViewById(R.id.btnSignUp);

        //firebase
        FirebaseApp.initializeApp(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //loading
                final ProgressDialog dialog = new ProgressDialog(SignUp.this);

                dialog.setMessage("Please Wait...");
                dialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //check to see if phone number is already used on another account

                        if (editName.getText().length()< 1)
                        {
                            dialog.dismiss();
                            Toast.makeText(SignUp.this, "Name Is Required", Toast.LENGTH_SHORT).show();
                        }
                        if (editMobileNum.getText().length() < 11) {
                            dialog.dismiss();
                            Toast.makeText(SignUp.this, "Mobile Number is too short", Toast.LENGTH_SHORT).show();
                        }
                        if (editPassword.getText().length() < 6) {
                            dialog.dismiss();
                            Toast.makeText(SignUp.this, "Password too short", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            if (dataSnapshot.child(editMobileNum.getText().toString()).exists()) {
                                dialog.dismiss();
                                // Toast.makeText(SignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                User user = new User(editName.getText().toString(), editPassword.getText().toString());
                                table_user.child(editMobileNum.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
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
