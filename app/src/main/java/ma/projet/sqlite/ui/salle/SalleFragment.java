package ma.projet.sqlite.ui.salle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.SalleAdapter;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentSalleBinding;
import ma.projet.sqlite.databinding.MachineSalleFragmentBinding;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.machinesalle.MachineSalleFragment;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;
import ma.projet.sqlite.ui.update.UpdateFragment;

public class SalleFragment extends Fragment {

    private ListView list;
    private SalleService ad;
    private View v;

    private FragmentSalleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SalleViewModel homeViewModel = new ViewModelProvider(this).get(SalleViewModel.class);

        binding = FragmentSalleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = (ListView) root.findViewById(R.id.listView);
        ad = new SalleService(getContext());
        SalleAdapter as = new SalleAdapter(getContext(), ad.findAll());
        list.setAdapter(as);
        list.setOnItemClickListener(this::onItemClick);

       // final TextView textView = binding.textHome;
       // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, ((TextView) view.findViewById(R.id.ids)).getText().toString() + " " + ((TextView) view.findViewById(R.id.code)).getText() + " " + ((TextView) view.findViewById(R.id.libelle)).getText(), Toast.LENGTH_LONG).show();
        v = view;
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
        final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialogBuilder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                        ad.delete(ad.findById(id));
                        FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                        nextFrag.replace(R.id.nav_host_fragment_content_nav, new SalleFragment());
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
                Salle salle = ad.findById(id);
                Bundle b = new Bundle();
                b.putInt("id",salle.getId());
                b.putString("code",salle.getCode());
                b.putString("libelle",salle.getLibelle());
                UpdateFragment mf = new UpdateFragment();
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