package my.edu.utar.petfinderapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountInfo extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textViewUserName,textViewEmail,textViewPassword,textViewPhone;
    private String username,email,password,phone;
    private FirebaseAuth authProfile;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        auth = FirebaseAuth.getInstance();
        textViewUserName = findViewById(R.id.displayusername);
        textViewEmail = findViewById(R.id.displayemail);
        textViewPassword = findViewById(R.id.displaypassword);
        textViewPhone = findViewById(R.id.displayphone);
        progressBar = findViewById(R.id.progressBar);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        
        if(firebaseUser == null){
            Toast.makeText(AccountInfo.this, "User's details are not available",
                    Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);

        }

    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();

        DatabaseReference refprofile = FirebaseDatabase.getInstance().getReference("Registered Users");
        refprofile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               User readuser = snapshot.getValue(User.class);
               if (readuser != null){
                   username = readuser.getUsername();
                   email = readuser.getEmail();
                   password = readuser.getPassword();
                   phone = readuser.getPhone();

                   textViewUserName.setText(username);
                   textViewEmail.setText(email);
                   textViewPassword.setText(password);
                   textViewPhone.setText(phone);

               }
               progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AccountInfo.this, "Failed to connect Firebase",
                        Toast.LENGTH_SHORT).show();

            }
        });



    }

    //ActionBar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu items
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);

        } else if (id == R.id.menu_change_name){
            Intent intent = new Intent(AccountInfo.this,ChangeUserName.class);
            startActivity(intent);

        } else if (id == R.id.menu_change_password){
            Intent intent = new Intent(AccountInfo.this,ChangePassword.class);
            startActivity(intent);

        } else if (id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(AccountInfo.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccountInfo.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else {
            Toast.makeText(AccountInfo.this,"Invalid Action",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}