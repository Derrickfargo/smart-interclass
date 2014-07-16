package cn.com.incito.classroom.ui.widget;


import com.popoy.tookit.Enum.Enumeration;

public interface IDictionarizable {

    void setDictionary(Enumeration[] dict);

    Enumeration[] getDictionary();
}