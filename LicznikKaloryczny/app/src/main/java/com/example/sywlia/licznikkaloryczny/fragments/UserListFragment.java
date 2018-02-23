package com.example.sywlia.licznikkaloryczny.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.activities.MainActivity;
import com.example.sywlia.licznikkaloryczny.adapters.UserListAdapter;
import com.example.sywlia.licznikkaloryczny.data.UserDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;

import java.util.ArrayList;


public class UserListFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<UserDTO> arrayList;
    private int idSelectedItem;
    private IUserDAO userDAO;

    public UserListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container!=null){container.removeAllViews();}
        View view=inflater.inflate(R.layout.fragment_user_list, container, false);

        this.userDAO=new UserDAO(getActivity());
        TextView textView=(TextView)view.findViewById(R.id.login_text_view);
        Log.i(Constans.LOG_APP,"start");

        Button button=(Button) view.findViewById(R.id.create_user_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserDetailsFragment userDetailsFragment=UserDetailsFragment.newInstance(0);
                FragmentManager fm=getChildFragmentManager();
                fm.beginTransaction().replace(R.id.user_list,userDetailsFragment,userDetailsFragment.getTag()).addToBackStack(null).commit();
            }
        });


        arrayList=userDAO.getUsersList();
        listView = (ListView) view.findViewById(R.id.login_list_view);
        if( arrayList!=null){

            ArrayAdapter<UserDTO> arrayAdapter = new UserListAdapter(getActivity(), arrayList);
            listView.setAdapter(arrayAdapter);
            Button loginBtn = (Button) view.findViewById(R.id.login_loginBtn);
            loginBtn.setOnClickListener(this);
            listView.setOnItemClickListener(this);
        }else{
            textView.setText(R.string.brak_uzytkownikow);
            listView.setClickable(false);
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onClick(View v) {
        UserDTO user=arrayList.get(idSelectedItem);
        long id=user.getId();
        userDAO.loginUser(id);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        Intent intent= new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int colorActivate= ContextCompat.getColor(getContext(),R.color.backgroundLighter);
        int colorInActive = ContextCompat.getColor(getContext(),R.color.background);
        parent.setBackgroundColor(colorInActive);
        listView.setBackgroundColor(colorInActive);
        view.setBackgroundColor(colorActivate);
        idSelectedItem=position;
        parent.setSelection(position);
    }

}
