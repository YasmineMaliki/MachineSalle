package ma.projet.sqlite.ui.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.ChartMachineSalleBinding;
import ma.projet.sqlite.databinding.FragmentGalleryBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;
import ma.projet.sqlite.util.MySQLiteHelper;

public class MachineParSalleFragment extends Fragment {

    private ChartMachineSalleBinding binding;
    BarChart barChart;
    private MySQLiteHelper helper=null;
    ArrayList<BarEntry> barEntriesMarqueByMachine;


    private static final String TABLE_SALLE = "salle";
    // Table Columns names
    private static final String KEY_ID = "idS";
    private static final String KEY_CODE = "code";
    private static final String KEY_LIBELLE = "libelle";
    private static final String[] COLUMNS = {KEY_ID, KEY_CODE, KEY_LIBELLE};

    private static final String TABLE_MACHINE = "machine";
    // Table Columns names
    private static final String KEY_IDM = "idM";
    private static final String KEY_REFERENCE = "reference";
    private static final String KEY_MARQUE = "marque";
    private static final String KEY_SALLE = "idSalle";
    private static final String[] COLUMNSM = {KEY_IDM, KEY_REFERENCE, KEY_MARQUE,KEY_SALLE};


    ArrayList<String> labelNames;

    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        MachineParSalleViewModel galleryViewModel = new ViewModelProvider(this).get(MachineParSalleViewModel.class);
        helper = new MySQLiteHelper(getActivity().getApplicationContext());
        binding = ChartMachineSalleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        barChart = root.findViewById(R.id.barChart);
        labelNames = new ArrayList<>();
        barEntriesMarqueByMachine = new ArrayList<>();


       /* for(int i=0;i<10;i++){
            float value =(float) (i*10.0);
            BarEntry barEntry = new BarEntry(i,value);
            barEntries.add(barEntry);
        }*/


        String query = "select count(*),"+KEY_LIBELLE+" from "+TABLE_SALLE +", "+TABLE_MACHINE+" where "+KEY_SALLE +"="+KEY_ID+" group by (libelle); ";

        SQLiteDatabase db = this.helper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query , null) ;
        int c =0;
        if (cursor.moveToFirst()) {
            do {
                labelNames.add(cursor.getString(1));
                BarEntry barEntry = new BarEntry(c,Integer.parseInt(cursor.getString(0)));
                barEntriesMarqueByMachine.add(barEntry);
                //drawChart();
                c++;
                Log.d("ccc",cursor.getString(1)+"");

                Log.d("ccc",cursor.getString(0)+"");
                //;
            } while (cursor.moveToNext());

        }
        drawChart();
        return root;
    }

    private void drawChart() {
        BarDataSet barDataSet = new BarDataSet(barEntriesMarqueByMachine,"Nombre de Machines par Salle");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        barDataSet.setDrawValues(false);
        barChart.setData(barData);

        barChart.animateY(2000);
        barChart.getDescription().setText("Nombre de Machines par Marque");
        barChart.getDescription().setTextColor(Color.BLUE);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelNames.size());

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Report Salle By " +
                "Machine");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}