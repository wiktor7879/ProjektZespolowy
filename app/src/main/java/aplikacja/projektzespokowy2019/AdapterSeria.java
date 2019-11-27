/*package aplikacja.projektzespokowy2019;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        imFport android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import model.Cwiczenie;
        import model.Plan;
        import model.Seria;

public class AdapterSeria extends ArrayAdapter {
    private final Activity context;
    private final List<Seria> Seria;


    public AdapterSeria(Activity context,List<Seria> Seria) {
        super(context, R.layout.layout_seria, Seria);
        this.context = context;
        this.Seria = Seria;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return Seria.size();
    }

    @Override
    public Object getItem(int position) {
        return Seria.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_seria, null, true);

            holder.editTextPowtorzenia = (EditText) convertView.findViewById(R.id.editTextPowtorzeniaWykonajPlan);
            holder.editTextCiezar = (EditText) convertView.findViewById(R.id.editTextCiezarWykonajPlan);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.editTextPowtorzenia.setText(Seria.get(position).getPowtorzenia().toString());

        holder.editTextPowtorzenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Seria.get(position).setPowtorzenia(Integer.parseInt(holder.editTextPowtorzenia.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.editTextCiezar.setText(Seria.get(position).getCiezar().toString());

        holder.editTextCiezar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Seria.get(position).setCiezar(Integer.parseInt(holder.editTextCiezar.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected EditText editTextPowtorzenia;
        protected EditText editTextCiezar;
    }


}
*/