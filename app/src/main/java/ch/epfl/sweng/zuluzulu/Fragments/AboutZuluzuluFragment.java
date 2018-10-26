package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutZuluzuluFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutZuluzuluFragment extends Fragment {
    public static final String TAG = "ABOUT_TAG";

    private OnFragmentInteractionListener mListener;

    public AboutZuluzuluFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance
     *
     * @return A new instance of fragment AboutZuluzuluFragment.
     */
    public static AboutZuluzuluFragment newInstance() {
        return new AboutZuluzuluFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_zuluzulu, container, false);

        Button button = view.findViewById(R.id.send_mail);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSendEmail();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Open an external app to send an email to the user
     */
    private void onSendEmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        // Add object and subject
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"epflife@epfl.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About your wonderful app !");

        // Start the activity and ask for a mail
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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
}
