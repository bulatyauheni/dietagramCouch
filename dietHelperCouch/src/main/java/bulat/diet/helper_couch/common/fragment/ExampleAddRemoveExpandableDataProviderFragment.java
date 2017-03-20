/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package bulat.diet.helper_couch.common.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import bulat.diet.helper_couch.common.data.AbstractAddRemoveExpandableDataProvider;
import bulat.diet.helper_couch.common.data.ExampleAddRemoveExpandableDataProvider;


public class ExampleAddRemoveExpandableDataProviderFragment extends Fragment {
    private ExampleAddRemoveExpandableDataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // keep the mDataProvider instance
        mDataProvider = new ExampleAddRemoveExpandableDataProvider();
    }

    public AbstractAddRemoveExpandableDataProvider getDataProvider() {
        return mDataProvider;
    }
}
