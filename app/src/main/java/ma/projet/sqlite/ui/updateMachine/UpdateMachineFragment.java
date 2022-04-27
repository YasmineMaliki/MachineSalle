package ma.projet.sqlite.ui.updateMachine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentHomeBinding;
import ma.projet.sqlite.databinding.UpdatemachineFragmentBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.salle.SalleFragment;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;

public class UpdateMachineFragment extends Fragment {

    private EditText marque ;
    private EditText reference ;
    private Spinner spinner ;
    private Button modifier ;
    private HashMap data ;
    private Intent i;
    private Serializable s;
    private MachineService machineService;
    private SalleService salleService;


    private UpdatemachineFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UpdateMachineViewModel homeViewModel = new ViewModelProvider(this).get(UpdateMachineViewModel.class);
        Log.d("PPPPP",homeViewModel.getcode().getValue()+"");

        binding = UpdatemachineFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        marque = (EditText) root.findViewById(R.id.marque);
        reference = (EditText) root.findViewById(R.id.reference);
        modifier = (Button) root.findViewById(R.id.modifier);
        spinner = (Spinner) root.findViewById(R.id.Spinner);
        salleService = new SalleService(getContext().getApplicationContext());

        ArrayAdapter<String> adapter;
        List<String> liste= new ArrayList<String>();
        for(Salle salle : salleService.findAll()) {
            liste.add(salle.getCode());
        }
        adapter = new ArrayAdapter<String>(getContext().getApplicationContext(), R.layout.spinner_item, liste);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        marque.setText(""+homeViewModel.getmarque().getValue());
        reference.setText(""+homeViewModel.getRef().getValue());

        Bundle b = this.getArguments();
        if(b != null){
            marque.setText(""+b.getString("marque"));
            reference.setText(""+b.getString("reference"));
            for (int i = 0; i < spinner.getCount(); i++) {
                String item = spinner.getItemAtPosition(i).toString();
                Log.d("BAAAAA",item);

                if (item.equals(b.getString("idSalle"))){
                    spinner.setSelection(i);
                    break;
                }
            }
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salleService = new SalleService(getContext().getApplicationContext());
                machineService = new MachineService(getContext().getApplicationContext());
                int id = salleService.findByCode(spinner.getSelectedItem().toString()).getId();
                machineService.update(new Machine(Integer.parseInt(b.get("id").toString()),marque.getText().toString() , reference.getText().toString() , salleService.findById(id)));
                Log.d("Marque : ",marque.getText().toString());
                Log.d("Reference : ",reference.getText().toString());

                FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                nextFrag.replace(R.id.nav_host_fragment_content_nav, new SlideshowFragment());
                nextFrag.setReorderingAllowed(true);
                nextFrag.addToBackStack(null);
                nextFrag.commit();
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}