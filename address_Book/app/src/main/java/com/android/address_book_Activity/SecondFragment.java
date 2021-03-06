package com.android.address_book_Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.ColorStateListDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.Task.GroupNetworkTask;
import com.android.Task.PeopleNetworkTask;
import com.android.address_book.Group;
import com.android.address_book.GroupAdapter;
import com.android.address_book.People;
import com.android.address_book.PeopleAdapter;
import com.android.address_book.R;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    final static String TAG = "SelectAllActivity";
    String urlAddrBase = null;
    String urlAddr1 = null;
    String urlAddr2 = null;
    String urlAddr3 = null;
    ArrayList<People> members;
    ArrayList<Group> groups;
    PeopleAdapter adapter;
    GroupAdapter groupAdapter;
    ListView listView, groupList;
    String macIP;
    String email, groupName;
    Button btnGroup1, btnGroup2, btnGroup3, btnGroup4;
    HorizontalScrollView horizontalScrollView;
    ArrayList<People> searchArr;
    EditText search_EdT;

    private LinearLayout ll;
    private Button[] tvs;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //connectGetData(urlAddr1);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchArr = new ArrayList<People>();

        // Fragment??? Activity??? ?????????????????? ???????????? ??????????????? ????????? ????????????.
        View v = inflater.inflate(R.layout.fragment_second, container, false);

        // listView??? Ip, jsp??? ????????????
        listView = v.findViewById(R.id.lv_group);

        String urlAddr = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddr + "group_people_query_all.jsp?email=con@naver.com";


        email = getArguments().getString("useremail");
        macIP = getArguments().getString("macIP");

        groupName = "??????";

        urlAddrBase = "http://" + macIP + ":8080/test/";
        urlAddr1 = urlAddrBase + "group_people_query_all.jsp?email=" + email + "&group=" + groupName;
        urlAddr2 = urlAddrBase + "group_query_all.jsp?email=" + email;
//        groupList = v.findViewById(R.id.lv_group_frg);
        horizontalScrollView = v.findViewById(R.id.hsv_01_group);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);  // ??????, ???, ?????????, ?????? ???????????????.




        // ????????? ?????? ?????? ??????
//        btnGroup1 = v.findViewById(R.id.button1);
//        btnGroup2 = v.findViewById(R.id.button2);
//        btnGroup3 = v.findViewById(R.id.button3);
//
//        btnGroup1.setOnClickListener(onCLickListener);
//        btnGroup2.setOnClickListener(onCLickListener);
//        btnGroup3.setOnClickListener(onCLickListener);

        //////////////////////////////////////////////////////
        // ????????? horizontal ??????

        connectGroupGetData(urlAddr2);

        ll = v.findViewById(R.id.ll_01_group);
        tvs = new Button[groups.size() + 3];
        String[] groupDefault = {"??????", "??????", "??????"};

        for (int i=0; i<3; i++){
            // main??? ???????????? ?????? ?????? ??????
            tvs[i] = new Button(getContext());
            tvs[i].setText(groupDefault[i]);
            tvs[i].setTextSize(15);
            tvs[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.scroll_layout));
//            tvs[i].getCompoundDrawablePadding();
            tvs[i].setId(i);
            ll.addView(tvs[i]);
            tvs[i].setLayoutParams(params);
//            ll.setPadding(5,5,5,5);
            tvs[i].setOnClickListener(mClickListener);

        }

        for (int i=3; i<(groups.size()+3); i++){
            // main??? ???????????? ?????? ?????? ??????
            tvs[i] = new Button(getContext());
            tvs[i].setText(groups.get(i-3).getGroupName());
            tvs[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.scroll_layout));
            tvs[i].setTextSize(15);
//            tvs[i].getCompoundDrawablePadding();
            tvs[i].setId(i);
            ll.addView(tvs[i]);
            tvs[i].setLayoutParams(params);
//            ll.setPadding(5,5,5,5);
            tvs[i].setOnClickListener(mClickListener);

        }



        search_EdT = v.findViewById(R.id.search_ET_Second);
        search_EdT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_EdT.getText().toString();
                search(text);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewPeopleActivity.class);  // ????????? ??????????????? ???????????? ?????? ?????? ??? ??????????????? ????????????
                intent.putExtra("peopleno", members.get(position).getNo());
                intent.putExtra("useremail", members.get(position).getUseremail());
                intent.putExtra("phonetel", members.get(position).getTel());
                intent.putExtra("macIP", macIP);


                startActivity(intent);
            }
        });

        return v;
    }

    //////////////////////////////////////////////////////
    // ????????? horizontal ?????? - click ?????????
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchArr.clear();
            search_EdT.setText("");
            for (int i=0; i<(groups.size()+3); i++) {
                if(v.getId() == tvs[i].getId()){
                    Log.v("here", Integer.toString(tvs[i].getId()));
                    String groupName = tvs[i].getText().toString();
                    Log.v("here", groupName);
                    urlAddr3 = urlAddrBase + "group_people_query_all.jsp?email=" + email + "&group=" + groupName;;
                    Log.v("here", urlAddr3);
                    connectGetData(urlAddr3);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        connectGetData(urlAddr1);
        connectGroupGetData(urlAddr2);
        Log.v(TAG, "onResume()");

    }

    // NetworkTask?????? ?????? ???????????? ?????????
    private void connectGetData(String urlAddr) {
        try {
            PeopleNetworkTask peopleNetworkTask = new PeopleNetworkTask(getContext(), urlAddr);
            Object obj = peopleNetworkTask.execute().get();
            members = (ArrayList<People>) obj;
            Log.v("here", "" + members);
            adapter = new PeopleAdapter(getContext(), R.layout.people_custom_layout, members, urlAddrBase); // ???????????? ?????? ????????????.
            listView.setAdapter(adapter);  // ??????????????? ???????????? ?????? ?????? ????????????.
            searchArr.addAll(members);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    // group NetworkTask?????? ?????? ???????????? ?????????
//    private ArrayList<Group> connectSelectData(String urlAddr) {
//        try {
//            GroupNetworkTask selectNetworkTask = new GroupNetworkTask(getContext(), urlAddr, "select");
//            Object obj = selectNetworkTask.execute().get();
//            groups = (ArrayList<Group>) obj;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return groups;
//    }

    // NetworkTask?????? ?????? ???????????? ?????????
    private ArrayList<Group> connectGroupGetData(String urlAddr) {
        try {
            GroupNetworkTask groupNetworkTask = new GroupNetworkTask(getContext(), urlAddr, "select");
            Object obj = groupNetworkTask.execute().get();
            groups = (ArrayList<Group>) obj;
            Log.v("here", "" + groups);
//            groupAdapter = new GroupAdapter(getContext(), R.layout.group_custom_layout, groups); // ???????????? ?????? ????????????.
//            groupAdapter = new GroupAdapter(getContext(), R.layout.group_custom_layout, groups); // ???????????? ?????? ????????????.
//            groupList.setAdapter(groupAdapter);  // ??????????????? ???????????? ?????? ?????? ????????????.


        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    public void search(String charText) {
        members.clear();
        if (charText.length() == 0) {
            members.addAll(searchArr);
        }
        else {
            for (int i = 0; i < searchArr.size(); i++) {
                if (searchArr.get(i).getName().contains(charText)) {
                    members.add(searchArr.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


//    // ????????? ?????? ?????? ( ?????? , ?????? , ??????????????????)
//    View.OnClickListener onCLickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//
//                case R.id.button1:
//                        urlAddr4 = "";
//                        String group1 = btnGroup1.getText().toString();
//
//                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group1;
////                        urlAddr4 = urlAddr + "group_people_query_all.jsp?email=" + email + "&group=" + group1;
//                        connectGetData(urlAddr4);
//                        break;
//
//                case R.id.button2:
//                    urlAddr4 = "";
//                    String group2 = btnGroup2.getText().toString();
//
//                    urlAddr4 = urlAddr + "group_people_query_all.jsp?email=qkr@naver.com&group=" + group2;
//                    connectGetData(urlAddr4);
//                    break;
//
//            }
//        }
//    };

}