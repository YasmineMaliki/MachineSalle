package ma.projet.sqlite.ui.machinesalle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MachineSalleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MachineSalleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Machine Salle fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}