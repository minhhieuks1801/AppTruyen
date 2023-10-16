package com.example.apptruyen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.databinding.ActivityMainBinding;
import com.example.apptruyen.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvTenTaiKhoan, tvEmail;
    private ImageView imgAvatar;
    private NavigationView mnavigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<User> listUsers;
    public static final int MY_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_trangchu, R.id.nav_tutruyen, R.id.nav_thongbao, R.id.nav_taikhoan)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initUi();
        showUserInformation();
        addUser();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sreach) {
            Intent intent = new Intent(this, TimKiemActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initUi(){
        mnavigationView = findViewById(R.id.nav_view);
        tvTenTaiKhoan = mnavigationView.getHeaderView(0).findViewById(R.id.txtTen);
        tvEmail = mnavigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        imgAvatar = mnavigationView.getHeaderView(0).findViewById(R.id.imgAvatar);
    }

    public void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            tvTenTaiKhoan.setVisibility(View.GONE); //ẩn
        }
        else {
            tvTenTaiKhoan.setVisibility(View.VISIBLE); //Hiện
            tvTenTaiKhoan.setText(name+"");
        }

        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar).into(imgAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Gọi phương thức showUserInformation() để cập nhật thông tin người dùng
            showUserInformation();
        }
    }

    private void addUser(){
        List<String> k = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("User");

        DatabaseReference newUserRef = myRef1.push();
        User user1 = new User(user.getUid(), user.getDisplayName(), "");
        listUsers = new ArrayList<>();

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUsers != null){
                    listUsers.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    listUsers.add(user);
                }
                for (int i = 0; i <listUsers.size(); i++){
                    k.add(listUsers.get(i).getId());
                }
                if(k.contains(user1.getId())){
                    return;
                }
                DatabaseReference myRef2 = database.getReference("User").child(user1.getId());
                myRef2.setValue(user1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}