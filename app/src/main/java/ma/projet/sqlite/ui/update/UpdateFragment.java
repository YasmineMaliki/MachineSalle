package ma.projet.sqlite.ui.update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.util.HashMap;

import ma.projet.sqlite.R;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.databinding.FragmentSlideshowBinding;
import ma.projet.sqlite.databinding.UpdateFragmentBinding;
import ma.projet.sqlite.service.SalleService;
import ma.projet.sqlite.ui.salle.SalleFragment;
import ma.projet.sqlite.ui.slideshow.SlideshowFragment;
import ma.projet.sqlite.ui.slideshow.SlideshowViewModel;

public class UpdateFragment extends Fragment {

    private UpdateFragmentBinding binding;

    private EditText code;
    private EditText libelle;
    private Button modifier;
    private HashMap data;
    private Intent i;
    private Serializable s;
    private SalleService salleService;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = UpdateFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        code = (EditText) root.findViewById(R.id.codeM);
        libelle = (EditText) root.findViewById(R.id.libelleM);
        modifier = (Button) root.findViewById(R.id.edit);

        Bundle b = this.getArguments();
        if(b != null){

            code.setText("" + b.get("code"));
            libelle.setText("" + b.get("libelle"));
        } else {
            Toast.makeText(getContext(), " Impossible", Toast.LENGTH_LONG).show();
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salleService = new SalleService(getContext().getApplicationContext());
                salleService.update(new Salle(Integer.parseInt(b.get("id").toString()), code.getText().toString(), libelle.getText().toString()));
                FragmentTransaction nextFrag= getFragmentManager().beginTransaction();
                nextFrag.replace(R.id.nav_host_fragment_content_nav, new SalleFragment());
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
