package org.ramonaza.officialramonapp.people.rides.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.people.backend.ContactDatabaseContract;
import org.ramonaza.officialramonapp.people.backend.ContactDatabaseHandler;
import org.ramonaza.officialramonapp.people.backend.ContactDatabaseHelper;
import org.ramonaza.officialramonapp.people.backend.ContactInfoWrapper;
import org.ramonaza.officialramonapp.people.rides.backend.DriverInfoWrapper;
import org.ramonaza.officialramonapp.people.rides.backend.RidesDatabaseHandler;
import org.ramonaza.officialramonapp.people.rides.ui.activities.RidesAlephManipActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RidesAlephManipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RidesAlephManipFragment extends Fragment {

    private int alephID;
    private ContactInfoWrapper mAleph;
    private TextView dataview;
    private PopView popTask;
    public static final String EXTRA_ALEPHID= RidesAlephManipActivity.EXTRA_ALEPHID;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inID  the id of the aleph
     * @return A new instance of fragment RidesAlephManipFragment.
     */
    public static RidesAlephManipFragment newInstance(int inID) {
        RidesAlephManipFragment fragment = new RidesAlephManipFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ALEPHID, inID);
        fragment.setArguments(args);
        return fragment;
    }

    public RidesAlephManipFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        this.alephID=args.getInt(EXTRA_ALEPHID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_rides_aleph_manip, container, false);
        this.dataview=(TextView) rootView.findViewById(R.id.ContactInfoView);
        refreshData();
        return rootView;
    }

    private void refreshData(){
        if(popTask != null) popTask.cancel(true);
        popTask= new PopView(getActivity());
        popTask.execute(alephID);
    }


    private class PopView extends AsyncTask<Integer, Void, Void>{

        private Context context;
        private DriverInfoWrapper[] drivers;

        public PopView(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            SQLiteDatabase db=new ContactDatabaseHelper(context).getWritableDatabase();
            RidesDatabaseHandler rhandler= new RidesDatabaseHandler(db);
            ContactDatabaseHandler chandler= new ContactDatabaseHandler(db);
            mAleph=chandler.getContact(params[0]);
            String[] whereclause=new String[]{
                    String.format("%s in (SELECT %s FROM %s WHERE %s = %s)", ContactDatabaseContract.DriverListTable._ID,
                            ContactDatabaseContract.RidesListTable.COLUMN_CAR,ContactDatabaseContract.RidesListTable.TABLE_NAME,
                            ContactDatabaseContract.RidesListTable.COLUMN_ALEPH,mAleph.getId())
            };
            drivers=rhandler.getDrivers(whereclause, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String viewData="Name: "+mAleph.getName()+"\n\n"+
                    "Address: "+mAleph.getAddress()+"\n\n"+
                    "School: "+mAleph.getSchool()+"\n\n";
            for(DriverInfoWrapper driver: drivers){
                viewData+="Currently in car: "+driver.getName()+"\n";
            }
            if(drivers.length == 0) viewData+="Not currently in car.";
            dataview.setTextSize(20);
            dataview.setText(viewData);
            getActivity().getActionBar().setTitle(mAleph.getName());
            popTask=null;
        }
    }



}
