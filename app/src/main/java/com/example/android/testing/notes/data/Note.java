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

import androidx.annotation.Nullable;
import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Immutable model class for a Note.
 */
public final class Note {

    private final String mId;
    @Nullable
    private final String mTitle;
    @Nullable
    private final String mDescription;
    @Nullable
    private final String mImageUrl;
    private boolean mIsArchived;

    public Note(@Nullable String title, @Nullable String description) {
        this(UUID.randomUUID().toString(), title, description, null, false);
    }

    public Note(@Nullable String title, @Nullable String description, boolean isArchived) {
        this(UUID.randomUUID().toString(), title, description, null, isArchived);
    }

    public Note(@Nullable String title, @Nullable String description, @Nullable String imageUrl) {
        this(UUID.randomUUID().toString(), title, description, imageUrl, false);
    }

    public Note(String id, @Nullable String title, @Nullable String description, @Nullable String imageUrl, boolean isArchived) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
        mIsArchived = isArchived;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getImageUrl() {
        return mImageUrl;
    }

    public boolean isArchived() {
        return mIsArchived;
    }

    public void setIsArchived(boolean isArchived) {
        mIsArchived = isArchived;
    }

    public boolean isEmpty() {
        return (mTitle == null || "".equals(mTitle)) &&
                (mDescription == null || "".equals(mDescription));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equal(mId, note.mId) &&
                Objects.equal(mTitle, note.mTitle) &&
                Objects.equal(mDescription, note.mDescription) &&
                Objects.equal(mImageUrl, note.mImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription, mImageUrl);
    }
}
