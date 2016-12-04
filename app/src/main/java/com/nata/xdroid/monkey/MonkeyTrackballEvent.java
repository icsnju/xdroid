/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nata.xdroid.monkey;

import android.app.Instrumentation;
import android.view.InputDevice;

/**
 * monkey trackball event
 */
public class MonkeyTrackballEvent extends MonkeyMotionEvent {
	public MonkeyTrackballEvent(int action) {
		super(MonkeyEvent.EVENT_TYPE_TRACKBALL, InputDevice.SOURCE_TRACKBALL,
				action);
	}

	@Override
	protected String getTypeLabel() {
		return "Trackball";
	}

	@Override
	public int fireEvent(Instrumentation inst) {
		try {
//			System.out.println(":Touching Key (" + getTypeLabel() + "): ");
			printInfo();

			inst.sendTrackballEventSync(getEvent());

		} catch (Exception e) {
			System.out.println(":Touching Key (" + getTypeLabel()
					+ ") rejected "+ e.toString());

			return MonkeyEvent.INJECT_FAIL;

		} finally {
			getEvent().recycle();
		}
		return MonkeyEvent.INJECT_SUCCESS;
	}
}
