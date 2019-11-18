package seven.hansung.nonamed.utility;

import android.content.Context;
import android.widget.ArrayAdapter;


import seven.hansung.nonamed.R;

public class BoardSearchSpinner {
    public ArrayAdapter<CharSequence> creatAdapter(ArrayAdapter<CharSequence> spinerAdapter,int id, Context ctx){
        //R.array.board_search_spinner
        spinerAdapter= ArrayAdapter.createFromResource(ctx, id, R.layout.spinnerlayout);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinerAdapter;
    }

}
