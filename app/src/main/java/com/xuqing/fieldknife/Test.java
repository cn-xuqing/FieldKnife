package com.xuqing.fieldknife;

import com.xuqing.fieldknifeannotation.BindObject;

@BindObject
public class Test {
    static {
        new Test();
    }
    private Test(){
        FieldKnife.bind(this);
    }
    public static int TEST=-1;
}
