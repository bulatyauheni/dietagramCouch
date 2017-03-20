package bulat.diet.helper_couch;

import android.app.Application;

import bulat.diet.helper_couch.utils.GATraker;

/**
 * Created by Yauheni.Bulat on 19.12.2016.
 */

public class DietaGramApplication extends Application {

    @Override
    public void onCreate() {
        GATraker.initialize(this);
        super.onCreate();
    }
}
