package ma.projet.sqlite.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.MachineAdapter;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.databinding.FragmentSlideshowBinding;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.ui.updateMachine.UpdateMachineFragment;
import ma.projet.sqlite.ui.updateMachine.UpdateMachineViewModel;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private ListView list;
    private MachineService ms;
    private View v ;
    UpdateMachineViewModel gallery;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = (ListView) root.findViewById(R.id.listView);
        ms = new MachineService(getContext());
        MachineAdapter as = new MachineAdapter(getContext(), ms.findAll());
        list.setAdapter(as);
        list.setOnItemClickListener(this::onItemClick);

       // final TextView textView = binding.textSlideshow;
        //slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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
                        ms.delete(ms.findById(id));
                        FragmentTransaction nextFrag= getFragmentManager().beginTransaction();

                        nextFrag.replace(R.id.nav_host_fragment_content_nav, new SlideshowFragment());
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

                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateMachineFragment.class);
                int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                Machine machine = ms.findById(id);
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