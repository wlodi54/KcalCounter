package com.example.sywlia.licznikkaloryczny.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.UserDAO;
import com.example.sywlia.licznikkaloryczny.fragments.HistoryListFragment;
import com.example.sywlia.licznikkaloryczny.fragments.PlanListFragment;
import com.example.sywlia.licznikkaloryczny.fragments.SelectTrainingFragment;
import com.example.sywlia.licznikkaloryczny.fragments.StatisticFragment;
import com.example.sywlia.licznikkaloryczny.fragments.UserDetailsFragment;
import com.example.sywlia.licznikkaloryczny.fragments.UserListFragment;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;

public class MainActivity extends AppCompatActivity
        implements UserDetailsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    IUserDAO userDAO;
    public long userId;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDAO = new UserDAO(context);
        if (userDAO.getUsersList() != null) {
            if (!checkIfUserLogged()) {
                UserListFragment userDetails = new UserListFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content_main, userDetails).commit();
            } else {

                SelectTrainingFragment selectTraining=SelectTrainingFragment.newInstance(userId);
                FragmentManager fm=getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content_main,selectTraining,selectTraining.getTag()).commit();
            }
        }else{
            addNewUserFragment(0);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }
    private boolean checkIfUserLogged() {

        if(userDAO.getUserLoged()!=null){
            UserDTO userDto = userDAO.getUserLoged();
            this.userId= userDto.getId();
            return true;
        }else
            return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        } else{
            drawer.openDrawer(GravityCompat.END);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_training) {

            SelectTrainingFragment selectTraining=SelectTrainingFragment.newInstance(this.userId);
            FragmentManager fm=getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main,selectTraining,selectTraining.getTag()).commit();
        } else if (id == R.id.nav_history) {
            HistoryListFragment historyfragment=HistoryListFragment.newInstance(this.userId);
            FragmentManager fm=getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main,historyfragment).commit();

        } else if (id == R.id.nav_change_user) {

            UserListFragment userDetails = new UserListFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main, userDetails).commit();
        } else if (id == R.id.nav_edit_user) {
            checkIfUserLogged();
            addNewUserFragment(this.userId);


        } else if (id == R.id.nav_statistic) {
            StatisticFragment statisticFragment= StatisticFragment.newInstance(this.userId);
            FragmentManager fm=getSupportFragmentManager();

            fm.beginTransaction().replace(R.id.content_main,statisticFragment).commit();
        }else if(id==R.id.nav_plan) {
            PlanListFragment listPlanFragment = PlanListFragment.newInstance(this.userId);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_main, listPlanFragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNewUserFragment(long id) {
        UserDetailsFragment userDetailsFragment=UserDetailsFragment.newInstance(id);
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_main,userDetailsFragment,userDetailsFragment.getTag()).commit();
    }

    @Override
    public void onFragmentInteraction(boolean uri) {

    }
}
