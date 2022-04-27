package ma.projet.sqlite.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.SalleAdapter;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentHomeBinding;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.machinesalle.MachineSalleFragment;
import ma.projet.sqlite.ui.salle.SalleFragment;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;
import ma.projet.sqlite.ui.update.UpdateFragment;

public class HomeFragment extends Fragment {
    private EditText code;
    private EditText libelle;
    private Button add;
    private ListView list;
    private SalleService ad;
    SalleService db = null;
    private FragmentHomeBinding binding;
    private View v;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        code = (EditText) root.findViewById(R.id.code);
        libelle = (EditText) root.findViewById(R.id.libelle);
        add = (Button) root.findViewById(R.id.add);
        list = (ListView) root.findViewById(R.id.recycle_view_machine_fragment);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db =  new SalleService(getActivity().getApplicationContext());
                db.add(new Salle(code.getText().toString(), libelle.getText().toString()));
                Toast.makeText(getActivity().getApplicationContext(), " Salle crée avec succès !!" , Toast.LENGTH_LONG).show();
                code.setText("");
                libelle.setText("");

                ad = new SalleService(getContext());
                SalleAdapter as = new SalleAdapter(getContext(), ad.findAll());
                list.setAdapter(as);
                as.notifyDataSetChanged();

            }
        });

       /* list = (Button) root.findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                nextFrag.replace(R.id.nav_host_fragment_content_nav, new SalleFragment());
                nextFrag.setReorderingAllowed(true);
                nextFrag.addToBackStack(null);
                nextFrag.commit();
            }
        });*/
        list = (ListView) root.findViewById(R.id.recycle_view_machine_fragment);
        ad = new SalleService(getContext());
        SalleAdapter as = new SalleAdapter(getContext(), ad.findAll());
        list.setAdapter(as);
        as.notifyDataSetChanged();
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
                        nextFrag.replace(R.id.nav_host_fragment_content_nav, new HomeFragment());
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