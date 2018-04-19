package com.uki121.pooni;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentSaveShare extends Fragment {
    private Button btnShare;
    final String LAP_RECORD = "elapsed_record";

    public void FragmentSaveShare(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savenshare, container, false);

        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = getArguments().getString(LAP_RECORD);
            }
        });
        return view;
    }

}
