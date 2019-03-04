/*
 * Copyright 2019, The Android Open Source Project
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

package com.example.android.testing.notes.util;

import static android.app.Activity.RESULT_FIRST_USER;

public class ResultCodes {

  public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;
  public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;
  public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 3;
  public static final int ARCHIVE_RESULT_OK = RESULT_FIRST_USER + 4;
  public static final int RESTORE_RESULT_OK = RESULT_FIRST_USER + 5;
}
