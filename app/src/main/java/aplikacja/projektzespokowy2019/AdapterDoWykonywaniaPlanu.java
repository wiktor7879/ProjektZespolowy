
package aplikacja.projektzespokowy2019;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import Fragments.fragmentWykonajPlan;
        import model.Cwiczenie;
        import model.Plan;
        import model.Seria;

public class AdapterDoWykonywaniaPlanu extends ArrayAdapter {
    private final Activity context;
    private final List<Cwiczenie> listaCwiczen;
    private final List<model.Plan> Plan;
    private final int pozycja;

    public AdapterDoWykonywaniaPlanu(Activity context, List<Plan> Plan, List<Cwiczenie> listaCwiczen,int pozycja) {  //activity,
        super(context, R.layout.layout_plan, listaCwiczen);
        this.context = context;
        this.Plan = Plan;
        this.listaCwiczen = listaCwiczen;
        this.pozycja = pozycja;
    }

    public View getView(int position, View view, ViewGroup parent) {

        String[] nameCwiczenArray = new String[Plan.get(position).getListaIdCwiczen().size()];
        Integer[] serie = new Integer[Plan.get(position).getListaIdCwiczen().size()];

        for(Integer i=0;i<Plan.get(position).getListaIdCwiczen().size();i++)
        {
            for(Integer j=0;j<listaCwiczen.size();j++)
            {
                if(Plan.get(position).getListaIdCwiczen().get(i).toString().equals(listaCwiczen.get(j).getId().toString()))
                {
                    nameCwiczenArray[i] = listaCwiczen.get(j).getNazwa().toString();
                    serie[i] = listaCwiczen.get(j).getSerie();
                }
            }
        }


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_plan, null, true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.idNazwaCwiczeniaWykonajPlan);
        LinearLayout lista = (LinearLayout) rowView.findViewById(R.id.idListaSeriiWykonajPlan);
        nameTextField.setText("Nazwa Cwiczenia : " + nameCwiczenArray[position]);

//wypisuje serie
        List<EditText> idEditTextPowtorzenia = new ArrayList<EditText>();
        List<EditText> idEditTextCiezar = new ArrayList<EditText>();


        /*
            for (int i = 0; i < serie[position]; i++) {
                View view1 = inflater.inflate(R.layout.layout_seria, null, true); //view 1 to jest textBox
                TextView text = (TextView) view1.findViewById(R.id.idKtoraSeriaWykonajPlan);
                EditText editPowtorzenia = (EditText) view1.findViewById(R.id.editTextPowtorzeniaWykonajPlan);
                EditText editCiezar = (EditText) view1.findViewById(R.id.editTextCiezarWykonajPlan);
                idEditTextCiezar.add(editCiezar);
                idEditTextPowtorzenia.add(editPowtorzenia);
                int j = i + 1;
                text.setText("Seria " + j);
                lista.addView(view1);
            }
*/







        return rowView;
    }


}
