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

package com.example.android.testing.notes.data;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * Defines an interface to the service API that is used by this application. All data request should
 * be piped through this interface.
 */
public interface NotesServiceApi {

    interface NotesServiceCallback<T> {

        void onLoaded(T notes);
    }

    void getAllNotes(NotesServiceCallback<List<Note>> callback);

    void getNote(String noteId, NotesServiceCallback<Note> callback);

    void saveNote(Note note);

    void archiveNote(@NonNull Note note);

    void restoreNote(@NonNull Note note);

    void deleteNote(@NonNull Note note);

    void deleteAllNotes();

    void deleteArchivedNotes();
}
