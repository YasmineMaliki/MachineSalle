package ma.projet.sqlite.ui.updateMachine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateMachineViewModel extends ViewModel {

    private  MutableLiveData<Integer> id;
    private  MutableLiveData<String> code;
    private  MutableLiveData<String> marque;
    private  MutableLiveData<String> ref;


    public UpdateMachineViewModel() {
        id = new MutableLiveData<>();
        code = new MutableLiveData<>();
        marque = new MutableLiveData<>();
        ref = new MutableLiveData<>();
    }

    public void selectId(Integer item) {
        id.setValue(item);
    }
    public void selectMarque(String item) {
        marque.setValue(item);
    }    public void selectRef(String item) {
        ref.setValue(item);
    }    public void selectSalle(String item) {
        code.setValue(item);
    }
    public LiveData<String> getmarque() {
        return marque;
    }
    public LiveData<Integer> getidd() {
        return id;
    }
    public LiveData<String> getcode() {
        return code;
    }
    public LiveData<String> getRef() {
        return ref;
    }
}