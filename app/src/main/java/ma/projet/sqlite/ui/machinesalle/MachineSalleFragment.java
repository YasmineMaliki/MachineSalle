package ma.projet.sqlite.ui.machinesalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.ListeMachinesSallesAdapter;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentGalleryBinding;
import ma.projet.sqlite.databinding.MachineSalleFragmentBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;

public class MachineSalleFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private MachineSalleFragmentBinding binding;
    private Spinner spinner;
    private SalleService salleService;
    private MachineService machineService;
    private ListView liste;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MachineSalleViewModel galleryViewModel = new ViewModelProvider(this).get(MachineSalleViewModel.class);

        binding = MachineSalleFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        salleService = new SalleService(getActivity().getApplicationContext());
        spinner = (Spinner) root.findViewById(R.id.spinner);
        liste = (ListView) root.findViewById(R.id.listView);
        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<String>();
        for (Salle salle : salleService.findAll()){
            list.add(salle.getCode());
        }
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return root;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        machineService = new MachineService(getActivity().getApplicationContext());
        int id = salleService.findByCode(spinner.getSelectedItem().toString()).getId();
        ListeMachinesSallesAdapter as = new ListeMachinesSallesAdapter(getActivity().getApplicationContext(), machineService.findMachines(id));
        liste.setAdapter(as);
    }
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}