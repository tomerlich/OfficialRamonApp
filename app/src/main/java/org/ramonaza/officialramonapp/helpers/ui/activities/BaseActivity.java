package org.ramonaza.officialramonapp.helpers.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import org.ramonaza.officialramonapp.R;
import org.ramonaza.officialramonapp.settings.ui.activities.SettingsActivity;
import org.ramonaza.officialramonapp.settings.ui.activities.HelpActivity;

/**
 * Created by ilanscheinkman on 5/9/15.
 */
public abstract class BaseActivity  extends Activity{

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent=new Intent(this,SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_help:
                Intent helpIntent=new Intent(this,HelpActivity.class);
                startActivity(helpIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }
}
