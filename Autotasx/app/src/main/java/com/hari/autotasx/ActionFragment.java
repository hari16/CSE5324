package com.hari.autotasx;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.hari.autotasx.ActionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.hari.autotasx.ActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private CheckBox silent;
    private CheckBox wifi;
    private CheckBox sms;
    public static boolean silVar;
    public static boolean wifiVar;
    public static boolean smsVar;
    private OnFragmentInteractionListener mListener;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ActionFragment newInstance(int sectionNumber) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ActionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_action, container, false);
        silent= (CheckBox)rootView.findViewById(R.id.silent);
        wifi= (CheckBox)rootView.findViewById(R.id.wifi);
        sms= (CheckBox)rootView.findViewById(R.id.sms);
        silent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    silVar=true;
                }
                else {
                    silVar=false;
                }

            }
        });
        wifi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    wifiVar=true;

                    //eniter into db
                }
                else{
                    wifiVar=false;

                    //enter null value
                }

            }
        });
        sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    smsVar=true;

                    //eniter into db
                }
                else{
                    smsVar=false;

                    //enter null value
                }

            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        //fab1.setOnCheckedChangeListener(this);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);

                Toast.makeText(getActivity(),"Actions you selected have been set for the location",Toast.LENGTH_SHORT).show();
                //startActivity(intent);
            }
        });
        return rootView;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
