package ma.projet.sqlite.ui.chart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MachineParSalleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MachineParSalleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Chart fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}