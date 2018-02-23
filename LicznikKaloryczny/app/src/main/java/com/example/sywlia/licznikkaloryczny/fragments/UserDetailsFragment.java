package com.example.sywlia.licznikkaloryczny.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.LevelDAO;
import com.example.sywlia.licznikkaloryczny.data.UserDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.ILevelDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.LevelDTO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;

public class UserDetailsFragment extends Fragment {

    private EditText nameEditText,weightEditText,heightEditText;
    private TextView goldOdznakaCount,silverOdznakaCount,brownOdznakaCount,poziom;
    private ImageView animalImageView;
    private Button saveBtn,deleteBtn;
    private long user_id;
    private static final String ARG_PARAM1 = "param1";
    private IUserDAO userDAO;
    UserDTO user;

    private long mParam1;


    private OnFragmentInteractionListener mListener;

    public UserDetailsFragment() {
    }

    public static UserDetailsFragment newInstance(long param1) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container!=null){container.removeAllViews();}
        View view= inflater.inflate(R.layout.fragment_user_details, container, false);
        if(getActivity().getActionBar()!=null){getActivity().getActionBar().setTitle("Panel użytkownika");
            Log.i(Constans.LOG_APP,"actionbar!=null");
        }
        this.userDAO=new UserDAO(getActivity());
        this.user_id=mParam1;
        user=new UserDTO();
        goldOdznakaCount=(TextView) view.findViewById(R.id.user_details_zloto_odznaka);
        silverOdznakaCount=(TextView) view.findViewById(R.id.user_details_srebro_odznaka);
        brownOdznakaCount=(TextView) view.findViewById(R.id.user_details_braz_odznaka);
        poziom=(TextView) view.findViewById(R.id.user_details_poziom);
        animalImageView=(ImageView) view.findViewById(R.id.user_details_animal);
        nameEditText = (EditText) view.findViewById(R.id.user_details_name_edit_text);
        weightEditText=(EditText) view.findViewById(R.id.user_details_weight_edit_text);
        heightEditText=(EditText) view.findViewById(R.id.user_details_height_edit_text);
        saveBtn=(Button)  view.findViewById(R.id.user_details_saveBtn);
        deleteBtn=(Button)  view.findViewById(R.id.user_details_deleteBtn);

        setButtons();
        if(this.user_id!=0){
            setEditTexts();
        }else
        {
            deleteBtn.setEnabled(false);
            deleteBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonColor));

            setAnimalImg(1);
        }
        return view;
    }



    private void setEditTexts() {
        user=userDAO.getUser(this.user_id);
        goldOdznakaCount.setText(String.valueOf(user.getMedalsGold()));
        silverOdznakaCount.setText(String.valueOf(user.getMedalsSliver()));
        brownOdznakaCount.setText(String.valueOf(user.getMedalsBrown()));
        int iloscMedali=user.getMedalsGold()-user.getMedalsBrown();
        setAnimalImg(iloscMedali);
        nameEditText.setText(user.getName());
        weightEditText.setText(String.valueOf(user.getWeight()));
        heightEditText.setText(String.valueOf(user.getHeight()));

    }

    private void setAnimalImg(int iloscMedali) {
        ILevelDAO poziomDAO=new LevelDAO(getActivity());
        LevelDTO poziom;
        if(iloscMedali<0) {
            poziom = poziomDAO.getPoziom(0);
        }else{
            poziom = poziomDAO.getPoziom(this.user.getPunktyRozowuju());
        }
        this.poziom.setText(poziom.getImie());
        animalImageView.setImageResource(poziom.getImageSrc());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setButtons() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEditText.getText().toString();
                String weight=weightEditText.getText().toString();
                String height=heightEditText.getText().toString();
                user.setName(name);
                user.setWeight(Double.parseDouble(weight));
                user.setId(UserDetailsFragment.this.user_id);
                if(UserDetailsFragment.this.user_id==0){
                    user.setPoziom(0);
                    user.setMedalsBrown(0);
                    user.setMedalsSliver(0);
                    user.setMedalsGold(0);
                    user.setPunktyRozowuju(0);
                }else{
                    user.setPoziom(user.getPoziom());
                    user.setMedalsBrown(user.getMedalsBrown());
                    user.setMedalsSliver(user.getMedalsSliver());
                    user.setMedalsGold(user.getMedalsGold());
                    user.setPunktyRozowuju(user.getPunktyRozowuju());
                }
                user.setLogged(1);
                user.setHeight(Double.parseDouble(height));
                try {

                    long userID=userDAO.insertUser(user);
                    if(userID!=0){
                        user.setId(userID);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), R.string.error_insert_user,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

                onButtonPressed(true);

                Fragment loginFragment=new SelectTrainingFragment();
                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_user_details,loginFragment);
                ft.addToBackStack(null);
                ft.commit();
                Toast.makeText(getActivity(), R.string.saving_label,Toast.LENGTH_SHORT).show();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userDAO.deleteUser(user_id);

                Fragment loginFragment=new UserListFragment();
                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_user_details,loginFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
    public void onButtonPressed(boolean uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean uri);
    }

}