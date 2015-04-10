package org.pentaho.di.trans.steps.streammeta;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.util.StringEvaluator;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puls3 on 08/04/15.
 */
public class StreamMetaData extends BaseStepData implements StepDataInterface {
    public RowMetaInterface inputRowMeta;
    public RowMetaInterface outputRowMeta;
    List<StringEvaluator> evaluators;
    long rownum = 0;
    long numFields;
    String[] fieldNames;

    public StreamMetaData() {
        super();

        evaluators = new ArrayList<StringEvaluator>();
    }
}
