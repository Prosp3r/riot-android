/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;

public class MatrixGcmListenerService extends GcmListenerService {

    private static final String LOG_TAG = "GcmListenerService";
    private Boolean mCheckLaunched = false;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(LOG_TAG, " onMessageReceived ");

        for (String key : data.keySet()) {
            Log.e(LOG_TAG, " >>> " + key + " : " + data.get(key));
        }
        // check if the application has been launched once
        // the first GCM event could have been triggered whereas the application is not yet launched.
        // so it is required to create the sessions and to start/resume event stream
        if (!mCheckLaunched && (null != Matrix.getInstance(getApplicationContext()).getDefaultSession())) {
            CommonActivityUtils.startEventStreamService(this);
            mCheckLaunched = true;
        }
        
        CommonActivityUtils.catchupEventStream(this);
    }
}
