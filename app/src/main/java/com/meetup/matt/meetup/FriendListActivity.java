package com.meetup.matt.meetup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.meetup.matt.meetup.Adapters.FriendListAdapter;
import com.meetup.matt.meetup.Controllers.LoginController;
import com.meetup.matt.meetup.Controllers.UserRequestController;
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Listeners.AddUserListener;
import com.meetup.matt.meetup.Listeners.GetFriendListListener;
import com.meetup.matt.meetup.WebApi.FriendListApi;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.ResponseDTO;
import com.meetup.matt.meetup.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private EditText mEmailView;
    private SearchView mSearchFriendsView;
    private UserDTO userDetails;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);
        loadFriendList(userDetails.getUserId());

        mSearchFriendsView = (SearchView) findViewById(R.id.search_friend_searchview);
        initializeSearchFriendsView();
    }

    private void initializeSearchFriendsView() {
        CharSequence query = mSearchFriendsView.getQuery();

        mSearchFriendsView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                attemptAddFriend();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void loadFriendList(String userUd) {
        FriendListApi.handleGetFriendList(userUd, getApplicationContext(), new GetFriendListListener() {
            @Override
            public void onDataReceived(ArrayList<UserDTO> fList) {

                Log.d("FLISTSIZE", Integer.toString(fList.size()));

                mRecyclerView = (RecyclerView) findViewById(R.id.friendlist_recycler_view);
                mRecyclerView.setHasFixedSize(false);
                layoutManager = new LinearLayoutManager(FriendListActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);

                mAdapter = new FriendListAdapter(fList);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
//
//    private boolean validateFields(String email) {
//
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            return false;
//        } else if (!LoginController.isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            return false;
//        }
//
//        return true;
//    }

    private void attemptAddFriend() {

        String query = mSearchFriendsView.getQuery().toString();

        FriendListApi.handleAddFriend(userDetails.getUserId(), query, getApplicationContext(), new AddUserListener() {
            @Override
            public void onUserAddedResponse(ResponseDTO response) {
                if (response.getStatus() != 1) {
                    //Toast.makeText(FriendListActivity.this, response.getText(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(FriendListActivity.this, response.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
