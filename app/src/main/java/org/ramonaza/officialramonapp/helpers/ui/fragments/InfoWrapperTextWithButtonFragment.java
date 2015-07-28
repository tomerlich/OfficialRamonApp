package org.ramonaza.officialramonapp.helpers.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.helpers.backend.InfoWrapper;
import org.ramonaza.officialramonapp.helpers.ui.other.InfoWrapperTextWithButtonAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class InfoWrapperTextWithButtonFragment extends Fragment {

    protected View rootView; //Root View containing all other children
    protected ProgressBar progressBar;
    protected int mLayoutId; //For children to override as necessary.
    protected ListView listView;
    protected InfoWrapperTextWithButtonAdapter mAdapter;
    protected GetInfoWrappers currentAsync;


    public InfoWrapperTextWithButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        if (mLayoutId == 0) mLayoutId = R.layout.fragment_info_wrapper_button_list;
        rootView = inflater.inflate(mLayoutId, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.cProgressBar);
        listView=(ListView) rootView.findViewById(R.id.infowrapperbuttonlist);

        mAdapter=new InfoWrapperTextWithButtonAdapter(getActivity()) {
            @Override
            public String getButtonText() {
                return buttonName();
            }

            @Override
            public void onButton(InfoWrapper info) {
                onButtonClick(info);
            }
        };
        refreshData();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoWrapper thisWrapper = (InfoWrapper) listView.getItemAtPosition(position);
                onButtonClick(thisWrapper);
            }
        });
        return rootView;
    }

    public void refreshData() {
        if(currentAsync != null) currentAsync.cancel(true);
        currentAsync = new GetInfoWrappers(getActivity(), mAdapter, progressBar);
        currentAsync.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onDetach() {
        if(currentAsync != null) currentAsync.cancel(true);
        super.onDetach();
    }


    /**
     * Method to get each button name.
     * @return the name of the button.
     */
    public abstract String buttonName();

    /**
     * The action each button performs.
     *
     * @param mWrapper the button's InfoWrapper; can be cast as necessary
     */
    public abstract void onButtonClick(InfoWrapper mWrapper);

    /**
     * Generates the activity's InfoWrapper list in an async task.
     *
     * @return the list of InfoWrappers
     */
    public abstract InfoWrapper[] generateInfo();

    /**
     * The class for retrieving the InfoWrappers.
     */
    protected class GetInfoWrappers extends AsyncTask<Void, Integer, InfoWrapper[]> {
        protected Context mContext;
        protected ProgressBar mBar;
        protected InfoWrapperTextWithButtonAdapter mAdapter;

        /**
         * Constructs the activity.
         *
         * @param context      the context to use
         * @param adapter      adapter to populate
         * @param progressBar  the bar to report progress to
         */
        public GetInfoWrappers(Context context, InfoWrapperTextWithButtonAdapter adapter, ProgressBar progressBar) {
            this.mContext = context;
            this.mBar = progressBar;
            this.mAdapter=adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBar.setVisibility(View.VISIBLE);
            mAdapter.clear();
        }

        @Override
        protected InfoWrapper[] doInBackground(Void... params) {
            return generateInfo();
        }

        @Override
        protected void onPostExecute(InfoWrapper[] infoWrappers) {
            super.onPostExecute(infoWrappers);
            if (!isAdded() || isDetached()) {
                return; //In case the calling activity is no longer attached
            }
            mAdapter.addAll(infoWrappers);
            mBar.setVisibility(View.GONE);

        }
    }

}
