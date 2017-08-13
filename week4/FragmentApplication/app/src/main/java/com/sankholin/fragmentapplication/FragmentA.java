package com.sankholin.fragmentapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentA extends Fragment {
    EditText editText;
    Button button;
    //declare the Communicator interface
    Communicator communicator;

    //Create an interface in FragmentA class
    public interface Communicator {
        public void respond(String data);
    }

    @Nullable
    @Override
    // onCreateView method is invoked when a fragment is created
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // load the layout of your created fragment into a view
        View view = inflater.inflate(R.layout.fragment_a_layout, container, false);
        // define your UI components in your layout
        editText = (EditText) view.findViewById(R.id.etOne);
        button = (Button) view.findViewById(R.id.btnButtonA);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.respond(editText.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            communicator = (Communicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement communicator interface");
        }
    }
}
