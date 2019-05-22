package com.crazyhitty.chdev.ks.firebasechat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Simple util class for easing the use of shared preference.
 * <p>
 * Created by Kartik_ch on 2/13/2016.
 */
public class SharedPrefUtil {
    /**
     * Name of the preference file
     */
    private static final String APP_PREFS = "application_preferences";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SharedPrefUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Save a string into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveString(String key, String value) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }
    public void savearrayString(String key, final String[] value) {
        Set<String> strings=new Set<String>() {
            @Override
            public int size() {
                return value.length;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        for (int i=0;i<value.length;i++) {
            strings.add(value[i]);
        }
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
       // mEditor.putString(key, value);
        mEditor.putStringSet(key, strings);
        mEditor.commit();
    }

    /**
     * Save a int into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveInt(String key, int value) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * Save a boolean into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    public void saveBoolean(String key, boolean value) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or null.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, null);
    }
    public Set<String> getstringsChat(String key) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getStringSet(key, null);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    public String getString(String key, String defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, 0);
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    public int getInt(String key, int defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Clears the shared preference file
     */
    public void clear() {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().apply();
    }
}
