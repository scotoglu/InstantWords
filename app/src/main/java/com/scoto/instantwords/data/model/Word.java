package com.scoto.instantwords.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_words")
public class Word implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "definition")
    private String definition;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "bg_color")
    private String bgColor;

    @ColumnInfo(name = "is_selected")
    private boolean isSelected;

    @ColumnInfo(name = "is_reminded")
    private int isReminded;

    @ColumnInfo(name = "is_learned")
    private boolean isLearned;

    @ForeignKey(entity = Category.class,
            parentColumns = "c_id",
            childColumns = "id",
            onUpdate = ForeignKey.CASCADE)
    @ColumnInfo(name = "c_id")
    private int c_id;//Category ID


    public Word(String word, String definition, int c_id) {
        this.word = word;
        this.definition = definition;
        this.createdAt = System.currentTimeMillis();
        this.isSelected = false;
        this.c_id = c_id;
        this.isReminded = 0;
        this.isLearned = false;
    }


    protected Word(Parcel in) {
        id = in.readInt();
        word = in.readString();
        definition = in.readString();
        createdAt = in.readLong();
        bgColor = in.readString();
        c_id = in.readInt();
        isReminded = in.readInt();
        isSelected = in.readByte() != 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };


    public int getC_id() {
        return c_id;
    }


    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setIsReminded(int isReminded) {
        this.isReminded = isReminded;
    }

    public int getIsReminded() {
        return isReminded;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(isReminded);
        dest.writeString(word);
        dest.writeString(definition);
        dest.writeInt(c_id);
        dest.writeLong(createdAt);
        dest.writeString(bgColor);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
