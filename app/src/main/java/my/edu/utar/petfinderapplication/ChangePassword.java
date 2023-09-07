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

public class ChangePassword extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView editUserPassword;
    private String username,email,password,phone;
    private FirebaseAuth authProfile;
    ProgressBar progressBar;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();
        editUserPassword = findViewById(R.id.change_password);
        saveBtn = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showUserProfile(firebaseUser);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new password from the editUserPassword TextView
                String newPassword = editUserPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(newPassword)) {
                    changePassword(newPassword); // Call a method to change the password
                } else {
                    Toast.makeText(ChangePassword.this, "Please enter a new password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Add a method to change the password using Realtime Firebase
    private void changePassword(String newPassword) {
        user = authProfile.getCurrentUser();

        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully
                            updatePasswordInDatabase(newPassword);
                        } else {
                            // Password change failed
                            Toast.makeText(ChangePassword.this, "Failed to change password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Add a method to update the password in your Realtime Database
    private void updatePasswordInDatabase(String newPassword) {
        String userID = authProfile.getCurrentUser().getUid();
        DatabaseReference refprofile = FirebaseDatabase.getInstance().getReference("Registered Users");

        refprofile.child(userID).child("password").setValue(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully in Realtime Database
                            Toast.makeText(ChangePassword.this, "Password changed successfully",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), AccountInfo.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Password update in Realtime Database failed
                            Toast.makeText(ChangePassword.this, "Failed to change password",
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

                    editUserPassword.setText(password);
                } else {
                    Toast.makeText(ChangePassword.this, "Failed to connect Firebase",
                            Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangePassword.this, "Failed to connect Firebase",
                        Toast.LENGTH_SHORT).show();

            }


        });

        progressBar.setVisibility(View.GONE);

    }

}
