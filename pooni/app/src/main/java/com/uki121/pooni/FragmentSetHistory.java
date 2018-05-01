package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentSetHistory extends Fragment {
    private Button btn_history, btn_setting;

    public void FragmentSetHistory(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_home, container, false);
        init(view);
        return view;
    }
    public void init(View view) {
        //intialize button and theirs listener
        btn_setting = (Button) view.findViewById(R.id.btn_setbook);
        btn_history = (Button) view.findViewById(R.id.btn_history);
        historyButtonListener  btnlistener= new historyButtonListener();
        btn_setting.setOnClickListener(btnlistener);
        btn_history.setOnClickListener(btnlistener);
    }
    class historyButtonListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_setbook :
                    break;
                case R.id.btn_history :
                    Intent history = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(history);
                    break;
                default :
                    Log.w("history_buttonListener", "There is no such button identifier");
                    break;
            }
        }
    }
}
