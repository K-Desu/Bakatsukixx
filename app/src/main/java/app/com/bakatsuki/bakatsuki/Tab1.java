

package app.com.bakatsuki.bakatsuki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import app.com.bakatsuki.bakatsuki.R;


/**
 * Created by khaledAlhindi on 10/6/2017 AD.
 */

public class Tab1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tab1,container,false);
        return rootView;


    }
}
