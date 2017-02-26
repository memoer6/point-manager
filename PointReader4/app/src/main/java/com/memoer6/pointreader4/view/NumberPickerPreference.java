package com.memoer6.pointreader4.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.memoer6.pointreader4.R;

public class NumberPickerPreference extends DialogPreference {


    //a local constant is used to specify the default value in case getPersistedInt() can't
    // return a persisted value.
    private static final int DEFAULT_VALUE = 10;


    int mCurrentValue;
    NumberPicker mNumberPicker;

    //*****Specifying the user interface*****
    //When you extend DialogPreference, you must call setDialogLayoutResource() during
    //in the class constructor to specify the layout for the dialog.
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.numberpicker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);


    }

    //Creates the content view for the dialog (if a custom content view is required).
    @Override
    protected View onCreateDialogView() {
        mNumberPicker = new NumberPicker(getContext());
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(50);
        mNumberPicker.setValue(mCurrentValue);
        return mNumberPicker;
    }

    //***** Saving the setting's value *****
    //If you extend DialogPreference, then you should persist the value only when the dialog
    // closes due to a positive result (the user selects the "OK" button).
    // When a DialogPreference closes, the system calls the onDialogClosed() method. The method
    // includes a boolean argument that specifies whether the user result is "positive"—if
    // the value is true, then the user selected the positive button and you should
    // save the new value.
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        //In this example, mNewValue is a class member that holds the setting's current value.
        // Calling persistInt() saves the value to the SharedPreferences file (automatically
        // using the key that's specified in the XML file for this Preference).

        if (positiveResult) {
            persistInt(mNumberPicker.getValue());
        }
    }


    //****** Initializing the current value ******
    //When the system adds your Preference to the screen, it calls onSetInitialValue() to notify
    // you whether the setting has a persisted value. If there is no persisted value, this call
    // provides you the default value.
    // The onSetInitialValue() method passes a boolean, restorePersistedValue, to indicate whether
    // a value has already been persisted for the setting. If it is true, then you should retrieve
    // the persisted value by calling one of the Preference class's getPersisted*() methods,
    // such as getPersistedInt() for an integer value. You'll usually want to retrieve the
    // persisted value so you can properly update the UI to reflect the previously saved value.
    // If restorePersistedValue is false, then you should use the default value that is
    // passed in the second argument.
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        //Restore existing state or Set default state from the XML attribute
        mCurrentValue = (restorePersistedValue) ? this.getPersistedInt(DEFAULT_VALUE) :
                (Integer) defaultValue;

    }


    //****** Providing a default value *********
    //If the instance of your Preference class specifies a default value
    // (with the android:defaultValue attribute), then the system calls onGetDefaultValue()
    // when it instantiates the object in order to retrieve the value. You must implement this
    // method in order for the system to save the default value in the SharedPreferences.
    //The method arguments provide everything you need: the array of attributes and the index
    // position of the android:defaultValue, which you must retrieve. The reason you must
    // implement this method to extract the default value from the attribute is because you must
    // specify a local default value for the attribute in case the value is undefined.
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }


    /*
    //******* Saving and restoring the Preference's state ******
    //Just like a View in a layout, your Preference subclass is responsible for saving and
    // restoring its state in case the activity or fragment is restarted (such as when the user
    // rotates the screen). To properly save and restore the state of your Preference class, you
    // must implement the lifecycle callback methods onSaveInstanceState() and
    // onRestoreInstanceState().
    //The state of your Preference is defined by an object that implements the Parcelable interface.
    // The Android framework provides such an object for you as a starting point to define your
    // state object: the Preference.BaseSavedState class.
    //To define how your Preference class saves its state, you should extend the
    // Preference.BaseSavedState class. You need to override just a few methods and define
    // the CREATOR object.

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readInt();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    //With the above implementation of Preference.BaseSavedState added to your app (usually as a
    // subclass of your Preference subclass), you then need to implement the onSaveInstanceState()
    // and onRestoreInstanceState() methods for your Preference subclass.
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = mCurrentValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        mNumberPicker.setValue(myState.value);
    }
    */


}
