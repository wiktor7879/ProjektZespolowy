package aplikacja.projektzespokowy2019;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {
    private final Activity context;
    private final  String[] nameArray;

    public CustomListAdapter(Activity context, String[] nameArrayParam){
        super(context,R.layout.layout_cwiczenie , nameArrayParam);
        this.context = context;
        this.nameArray = nameArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
      View rowView=inflater.inflate(R.layout.layout_cwiczenie, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.textViewNameCw);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray[position]);
        return rowView;
    }
}
