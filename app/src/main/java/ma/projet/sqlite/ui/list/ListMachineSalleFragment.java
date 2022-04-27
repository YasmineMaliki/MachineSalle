package ma.projet.sqlite.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.ListeMachinesSallesAdapter;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentGalleryBinding;
import ma.projet.sqlite.databinding.ListeMachineSalleFragmentBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;

public class ListMachineSalleFragment extends Fragment {

    private ListeMachineSalleFragmentBinding binding;
    private ListView list;
    private MachineService machineService;
    private View v ;
    private HashMap data ;
    private Intent i;
    private Serializable s;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListMachineSalleViewModel galleryViewModel = new ViewModelProvider(this).get(ListMachineSalleViewModel.class);

        binding = ListeMachineSalleFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = (ListView) root.findViewById(R.id.listView);
        Bundle b = this.getArguments();
        if(b != null) {
            machineService = new MachineService(getContext());
            ListeMachinesSallesAdapter as = new ListeMachinesSallesAdapter(getContext(), machineService.findMachines(Integer.parseInt(b.get("id").toString())));
            list.setAdapter(as);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}