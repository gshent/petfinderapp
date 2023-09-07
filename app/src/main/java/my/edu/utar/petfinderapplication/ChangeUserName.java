package my.edu.utar.petfinderapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeUserName<showUserProfile> extends AppCompatActivity {

    FirebaseAuth auth;
    TextView editUserName;
    private String username,email,password,phone;
    private FirebaseAuth authProfile;
    ProgressBar progressBar;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        auth = FirebaseAuth.getInstance();
        editUserName = findViewById(R.id.change_name);
        saveBtn = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showUserProfile(firebaseUser);

        // Add an OnClickListener to the saveBtn button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new username from the editUserName TextView
                String newUsername = editUserName.getText().toString().trim();

                if (!TextUtils.isEmpty(newUsername)) {
                    updateUsername(newUsername); // Call a method to update the username
                } else {
                    Toast.makeText(ChangeUserName.this, "Please enter a new username",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Add a method to update the username in Firebase
    private void updateUsername(String newUsername) {
        String userID = authProfile.getCurrentUser().getUid();
        DatabaseReference refprofile = FirebaseDatabase.getInstance().getReference("Registered Users");

        // Update the username in the database
        refprofile.child(userID).child("username").setValue(newUsername)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeUserName.this, "Username updated successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), AccountInfo.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(ChangeUserName.this, "Failed to update username",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference refprofile = FirebaseDatabase.getInstance().getReference("Registered Users");

        progressBar.setVisibility(View.VISIBLE);

        refprofile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User readuser = snapshot.getValue(User.class);
                if (readuser != null) {
                    username = readuser.getUsername();
                    email = readuser.getEmail();
                    password = readuser.getPassword();
                    phone = readuser.getPhone();

                    editUserName.setText(username);
                } else {
                    Toast.makeText(ChangeUserName.this, "Failed to connect Firebase",
                            Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangeUserName.this, "Failed to connect Firebase",
                        Toast.LENGTH_SHORT).show();

            }
        });
        progressBar.setVisibility(View.GONE);

    }




}