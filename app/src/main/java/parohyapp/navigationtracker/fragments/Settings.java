package parohyapp.navigationtracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import parohyapp.navigationtracker.R;

/**
 * Created by tomas on 3/13/2016.
 */
public class Settings extends Fragment {

    private View view;

    private enum Status{
        OPEN,CLOSE
    }
    private Status currentStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment_layout,container,false);
        this.view = view;
        currentStatus = Status.CLOSE;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View getView(){
        return view;
    }

    public void toggle(){
        ViewGroup parent = (ViewGroup) view;
        int max = parent.getChildCount();
        switch (currentStatus){
            case OPEN:
                currentStatus = Status.CLOSE;
                for(int i = 0; i < max; i++){
                    parent.getChildAt(i).setVisibility(View.GONE);
                }
                break;
            case CLOSE:
                currentStatus = Status.OPEN;
                for(int i = 0; i < max; i++){
                    parent.getChildAt(i).setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
