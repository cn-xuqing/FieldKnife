package test;

import com.xuqing.fieldknife.FieldKnife;
import com.xuqing.fieldknifeannotation.BindObject;

@BindObject
public class Test {
    public static int TEST=-1;
    static {
        new Test();
    }
    private Test(){
        FieldKnife.bind(this);
    }
}
