package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* To do */
public class FragmentPersonalSet extends Fragment {
    public FragmentPersonalSet() {};
    private Book b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_personal_set, container, false);

        FragmentManager fm = getFragmentManager();
        Fragment frag_title = fm.findFragmentById(R.id.personaL_title_fragment);
        Fragment frag_contents = fm.findFragmentById(R.id.personal_contents_fragment);

        /*
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frag_home_container, frag_title, "personal_set_title");
        transaction.addToBackStack(null);

        transaction.commit();

        transaction.replace(R.id.frag_home_container, newFragment, tag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        */
        return v;
    }
}
