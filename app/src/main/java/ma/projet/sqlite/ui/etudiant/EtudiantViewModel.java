package ma.projet.sqlite.ui.etudiant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EtudiantViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public EtudiantViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is etudiant fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}