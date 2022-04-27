package ma.projet.sqlite.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.MachineAdapter;
import ma.projet.sqlite.adapter.SalleAdapter;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentGalleryBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;
import ma.projet.sqlite.ui.update.UpdateFragment;
import ma.projet.sqlite.ui.updateMachine.UpdateMachineFragment;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private EditText marque ;
    private EditText reference ;
    private Button create ;
    private Button menu ;
    private Spinner spinner ;
    private SalleService salleService;
    private ListView list;
    private MachineService machineService;
    private View v ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        salleService = new SalleService(getActivity().getApplicationContext());
        marque =  root.findViewById(R.id.marque);
        reference =  root.findViewById(R.id.reference);
        create = root.findViewById(R.id.btnCreate);
        //list =  root.findViewById(R.id.recycle_view_machine_fragmentM);
        spinner =  root.findViewById(R.id.Spinner);

        ArrayAdapter<String> adapter;
        List<String> liste= new ArrayList<String>();
        for(Salle salle : salleService.findAll()) {
            liste.add(salle.getCode());
        }
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, liste);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineService machineService = new MachineService(getActivity().getApplicationContext());
                salleService = new SalleService(getActivity().getApplicationContext());
                Salle salle = salleService.findByCode(spinner.getSelectedItem().toString());
                machineService.add(new Machine(marque.getText().toString(), reference.getText().toString(), salle ));
                Toast.makeText(getActivity().getApplicationContext() ," Machine crée avec succès !! ", Toast.LENGTH_LONG).show();
                reference.setText("");
                marque.setText("");
                spinner.setSelection(0);
                list = (ListView) root.findViewById(R.id.recycle_view_machine_fragmentM);
                machineService = new MachineService(getContext());
                MachineAdapter as = new MachineAdapter(getContext(), machineService.findAll());
                list.setAdapter(as);
            }
        });

        /*listeMachines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                nextFrag.replace(R.id.nav_host_fragment_content_nav, new SlideshowFragment());
                nextFrag.setReorderingAllowed(true);
                nextFrag.addToBackStack(null);
                nextFrag.commit();
            }
        });*/
        list = (ListView) root.findViewById(R.id.recycle_view_machine_fragmentM);
        machineService = new MachineService(getContext());
        MachineAdapter as = new MachineAdapter(getContext(), machineService.findAll());
        list.setAdapter(as);
        list.setOnItemClickListener(this::onItemClick);


       // final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        v = view;
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
        final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialogBuilder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                        machineService.delete(machineService.findById(id));
                        FragmentTransaction nextFrag= getFragmentManager().beginTransaction();

                        nextFrag.replace(R.id.nav_host_fragment_content_nav, new GalleryFragment());
                        nextFrag.setReorderingAllowed(true);
                        nextFrag.addToBackStack(null);
                        nextFrag.commit();
                    }
                });
                alertDialogBuilder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder1.create();
                alertDialog.show();
            }
        });

        alertDialogBuilder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateFragment.class);

                int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                Machine machine = machineService.findById(id);
                Bundle b = new Bundle();
                b.putInt("id",machine.getId());
                b.putString("idSalle",machine.getSalle().getCode());
                b.putString("marque",machine.getMarque());
                b.putString("reference",machine.getRefernce());
                UpdateMachineFragment mf = new UpdateMachineFragment();
                mf.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_nav,mf).setReorderingAllowed(true).addToBackStack(null).commit();


            }
        });



        AlertDialog alertDialog1 = alertDialogBuilder.create();
        alertDialog1.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}