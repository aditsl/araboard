package es.puntoweb.board;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

    private static Boolean override;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        override=Araboard.getBoardOverride(getApplicationContext());
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }





    public  static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getInitialConfig();
            CheckBoxPreference  chkOverride= (CheckBoxPreference) findPreference("board_override");
            chkOverride.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if ((Boolean) o == false){
                        showBoardPrefs(false);
                    }else{
                        showBoardPrefs(true);
                    }
                    return true;
                }
            });

        }

        private void   getInitialConfig(){
            if (!override){
               showBoardPrefs(false);
            }

        }

        private void showBoardPrefs(Boolean enabled)
        {
            EditTextPreference editBoardRows=(EditTextPreference) findPreference("board_rows");
            EditTextPreference editBoardColumns=(EditTextPreference) findPreference("board_columns");
            editBoardColumns.setEnabled(enabled);
            editBoardRows.setEnabled(enabled);

        }


    }

}