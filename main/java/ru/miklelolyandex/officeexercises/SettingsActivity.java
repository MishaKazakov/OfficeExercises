package ru.miklelolyandex.officeexercises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import static ru.miklelolyandex.officeexercises.NotificationScheduler.turnOffOnNotificaton;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            SettingsActivity m = new SettingsActivity();
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_main);
            bindPreferenceSummaryToValue(findPreference("screen_on"));
            bindPreferenceSummaryToValue(findPreference("beginNotification"));
            bindPreferenceSummaryToValue(findPreference("endNotification"));
            bindPreferenceSummaryToValue(findPreference("exercise_duration"));
//            bindPreferenceSummaryToValue(findPreference("interval_duration"));
            bindPreferenceSummaryToValue(findPreference("interval_timer"));
            bindPreferenceSummaryToValue(findPreference("sensor_switch"));

            switchListener(findPreference("switch_notification"));
            switchListener(findPreference("beginNotification"));
            switchListener(findPreference("endNotification"));
            switchListener(findPreference("interval_timer"));
        }
    }

    private static void switchListener(Preference preference){

        if (preference instanceof SwitchPreference) {

            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SwitchPreference pref = (SwitchPreference) preference;
                    turnOffOnNotificaton(preference.getContext(), pref.isChecked());
                    stopNotification(preference);
                    return false;
                }
            });
        } else {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    turnOffOnNotificaton(preference.getContext(), true);
                    stopNotification(preference);
                    return false;
                }
            });
        }
    }

    private static void stopNotification(Preference preference){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        Boolean sensorPref = preferences.getBoolean("sensor_switch", false);
        if (!sensorPref) {
            preference.getContext().stopService( new Intent(preference.getContext(), NotificationService.class));
        }
    }


    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof SwitchPreference){
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(),false));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof SwitchPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                SwitchPreference switchPreference = (SwitchPreference) preference;
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void changeNotificationSchedule(Preference preference, String whatHasChanged, String value){

    }


    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected( item );
    }


}