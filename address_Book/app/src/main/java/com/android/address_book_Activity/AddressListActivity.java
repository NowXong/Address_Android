package com.android.address_book_Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.android.Task.NetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
======================                                                                              =======================
======================                                                                              =======================
======================                                 ????????? ?????? ??????                                 =======================
======================                                                                              =======================
======================                                                                              =======================
===========================================================================================================================
===========================================================================================================================
===========================================================================================================================
*/

public class AddressListActivity extends AppCompatActivity {

    final static String TAG = "AddressListActivity";

    String macIP;

    String email, urlAddr;

    private BottomNavigationView mBottomNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_address_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.this, RegisterPeopleActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sf = getSharedPreferences("appData", MODE_PRIVATE);
        macIP = sf.getString("macIP","");
        email = sf.getString("useremail","");

        urlAddr = "http://" + macIP + ":8080/test/";

        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // ?????? ??? ????????? ????????? ????????? ????????????.!
                 BottomNavigate(menuItem.getItemId());


//                switch (menuItem.getItemId()){
//                    case R.id.navigation_1:
//                        urlAddr2 = urlAddr + "people_query_all.jsp?email="+email;
//                        connectGetData(urlAddr2);
//                        break;
//
//                    case R.id.navigation_2:
//                        urlAddr4="";
//                        String group1 = btnGroup1.getText().toString();
//                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email="+emaiol&group=" + group1;
//                        connectGetData(urlAddr4);
//                        break;
//
//                    case R.id.navigation_3:
//                        urlAddr3 = urlAddr + "favorite_people_query_all.jsp?email=qkr@naver.com";
//                        connectGetData(urlAddr3);
//                }
                // BottomNavigate(menuItem.getItemId());



                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
//        mBottomNV.setSelectedItemId(R.id.navigation_2);
//        mBottomNV.setSelectedItemId(R.id.navigation_3);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        urlAddr1 = urlAddr + "people_query_all.jsp?email=qkr@naver.com";
//        connectGetData(urlAddr1);
//        searchArr = new ArrayList<People>();
//        searchArr.addAll(members);
//        Log.v(TAG, "onResume()");
//
//    }
//
//    // NetworkTask?????? ?????? ???????????? ?????????
//    private void connectGetData(String urlAddr) {
//        try {
//            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(AddressListActivity.this, urlAddr);
//            Object obj = peopleNetworkTask.execute().get();
//            members = (ArrayList<People>) obj;
//            Log.v("here", "" + members);
//            adapter = new PeopleAdapter(AddressListActivity.this, R.layout.people_custom_layout, members); // ???????????? ?????? ????????????.
//            listView.setAdapter(adapter);  // ??????????????? ???????????? ?????? ?????? ????????????.
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    private void BottomNavigate(int id) {  //BottomNavigation ????????? ?????? (?????? ??? 3??? ??????)
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            if (id == R.id.navigation_1) {  // ?????? ????????? 1??? ??????

                fragment = new FirstFragment();  // ??????????????? 1????????? ??????
                Bundle bundle = new Bundle(2);
                bundle.putString("useremail", email);
                bundle.putString("macIP", macIP);
                fragment.setArguments(bundle);

            } else if (id == R.id.navigation_2) {

                fragment = new SecondFragment();
                Bundle bundle = new Bundle(2);
                bundle.putString("useremail", email);
                bundle.putString("macIP", macIP);
                fragment.setArguments(bundle);
            } else if (id == R.id.navigation_3){

                fragment = new ThirdFragment();
                Bundle bundle = new Bundle(2);
                bundle.putString("useremail", email);
                bundle.putString("macIP", macIP);
                fragment.setArguments(bundle);
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_list, menu);
        return true;
    }

    //????????? ??????, ToolBar??? ????????? ????????? select ???????????? ???????????? ??????
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_group:
                // User chose the "Settings" item, show the app settings UI...
                LayoutInflater inflate = getLayoutInflater();

                GroupCustomDialogActivity customDialog = new GroupCustomDialogActivity(AddressListActivity.this);
                customDialog.callFunction(email, macIP);
//                new AlertDialog.Builder(AddressListActivity.this)
//                        .setTitle("?????? ??????")
//                        .setMessage((CharSequence) dialogView)
//                        .setIcon(R.mipmap.ic_launcher)
//                        .setCancelable(false) // ??????????????? ???????????? ????????? ??????. (????????? ??? ???????????? ????????? ???????????? ?????????)
//                        .setPositiveButton("??????", null)
//                        .setNegativeButton("??????", null)  // ????????? ????????? ???????????? null
//                        .show();

                return true;
            case R.id.change_passwd:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(AddressListActivity.this, MypagePWActivity01.class);
                startActivity(intent);
                return true;

            case R.id.userDelete:
                new AlertDialog.Builder(AddressListActivity.this)
                        .setTitle("??????")
                        .setMessage("?????? ?????????????????????????")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false) // ??????????????? ???????????? ????????? ??????. (????????? ??? ???????????? ????????? ???????????? ?????????)
                        .setPositiveButton("??????", clickListener)
                        .setNegativeButton("??????", null)  // ????????? ????????? ???????????? null
                        .show();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Intent intent3 = new Intent(AddressListActivity.this, MainActivity.class);

                startActivity(intent3);
                return super.onOptionsItemSelected(item);

        }


    }
        // ???????????? ??????????????? ?????????
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    userDelete();

                }


            }
        };

    private void userDelete() {
        String urlAddr1 = "";
        urlAddr1 = urlAddr + "userInfoDelete.jsp?email=" + email;

        String result = connectDeleteData(urlAddr1);

        if (result.equals("1")) {
            Toast.makeText(AddressListActivity.this, "?????????????????????.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(AddressListActivity.this, MainActivity.class);

            startActivity(intent2);

        } else {
            Toast.makeText(AddressListActivity.this, "?????? ?????????????????????.", Toast.LENGTH_SHORT).show();

        }
    }

        //connection Insert
        private String connectDeleteData(String urlAddr){
            String result = null;

            try{
                NetworkTask deleteNetworkTask = new NetworkTask(AddressListActivity.this, urlAddr, "delete");
                Object obj = deleteNetworkTask.execute().get();
                result = (String) obj;

            } catch (Exception e){
                e.printStackTrace();

            }
            return result;
        }

}