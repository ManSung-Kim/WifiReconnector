package jactlab.wifireconnector;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    private static Context gContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        gContext = getApplicationContext();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        // ui
        private EditText pmEditSSID;
        private EditText pmEditPW;
        private EditText pmEditCONFIG;
        private Button pmBtnConnect;


        private WifiManager pmWMgr;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            pmEditSSID = (EditText) rootView.findViewById(R.id.editSSID);
            pmEditPW= (EditText) rootView.findViewById(R.id.editPW);
            pmEditCONFIG = (EditText) rootView.findViewById(R.id.editConfig);

            // shared
            SharedPreferences lmShared = gContext.getSharedPreferences(StaticData.SHARED_NAME, MODE_PRIVATE);
            String lmStrSSID = lmShared.getString(StaticData.SHARED_KEY_SSID, "");
            String lmStrPW = lmShared.getString(StaticData.SHARED_KEY_PW, "");
            String lmStrCONFIG = lmShared.getString(StaticData.SHARED_KEY_CONFIG, "");
            pmEditSSID.setText(lmStrSSID);
            pmEditPW.setText(lmStrPW);
            pmEditCONFIG.setText(lmStrCONFIG);

            pmBtnConnect = (Button) rootView.findViewById(R.id.btnConnect);
            pmBtnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lmStrSSID = pmEditSSID.getText().toString();
                    String lmStrPW = pmEditPW.getText().toString();
                    String lmStrCONFIG = pmEditCONFIG.getText().toString();

                    if(lmStrSSID.isEmpty() /*|| lmStrPW.isEmpty() || lmStrCONFIG.isEmpty() */) {
                        Toast.makeText(gContext, gContext.getResources().getString(R.string.toastEmptyStr), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences lmShared = gContext.getSharedPreferences(StaticData.SHARED_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor lmEditor = lmShared.edit();
                    lmEditor.putString(StaticData.SHARED_KEY_SSID, lmStrSSID);
                    lmEditor.commit();
                    lmEditor.putString(StaticData.SHARED_KEY_PW, lmStrPW);
                    lmEditor.commit();
                    lmEditor.putString(StaticData.SHARED_KEY_CONFIG, lmStrCONFIG);
                    lmEditor.commit();


                    //pmWMgr = (WifiManager)gContext.getSystemService(gContext.WIFI_SERVICE);
                    //WManager.connectWifi(gContext, lmStrSSID,lmStrPW,lmStrCONFIG);
                    ArrayList<HashMap<String,String>> lmApList = WManager.getScanData(gContext);
                    for(int i=0;i<lmApList.size(); i++) {
                        String llmSSID = lmApList.get(i).get(StaticData.AP_INFO_SSID);
                        Utils.logd("find ssid = "+llmSSID+ " input ssid = "+lmStrSSID);
                        if(llmSSID.equals(lmStrSSID)) {
                            lmStrCONFIG = lmApList.get(i).get(StaticData.AP_INFO_CONFIG);
                            Utils.logd("find config = "+lmStrCONFIG);
                            break;
                        }
                    }

                    WManager.connectWifi(gContext, lmStrSSID,lmStrPW,lmStrCONFIG);
                }
            });




            //WManager.connectWifi(gContext, "hihihi","01033845125","WPA");
            //WManager.getScanData(gContext);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
