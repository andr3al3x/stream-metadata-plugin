package org.pentaho.di.trans.steps.streammeta;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.util.StringEvaluationResult;
import org.pentaho.di.core.util.StringEvaluator;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

import java.util.List;

/**
 * Created by puls3 on 08/04/15.
 */
public class StreamMeta extends BaseStep implements StepInterface {
    private static Class<?> PKG = StreamMetaMeta.class;

    private StreamMetaMeta meta;
    private StreamMetaData data;

    public StreamMeta(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (StreamMetaMeta) smi;
        data = (StreamMetaData) sdi;

        if ( !super.init( smi, sdi ) ) {
            return false;
        }

        return true;
    }

    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
        meta = (StreamMetaMeta) smi;
        data = (StreamMetaData) sdi;

        int fieldNum;

        // get the current row
        Object[] r = getRow();

        if (first) {
            data.inputRowMeta = getInputRowMeta();
            data.outputRowMeta = data.inputRowMeta.clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this, repository, metaStore);

            data.fieldNames = data.inputRowMeta.getFieldNames();
            data.numFields = data.inputRowMeta.size();

            for (fieldNum = 0; fieldNum < data.numFields; fieldNum++) {
                StringEvaluator evaluator = new StringEvaluator(true);
                data.evaluators.add(evaluator);
            }
        }

        // output the collected metadata results
        if (r == null) {
            for (fieldNum = 0; fieldNum < data.numFields; fieldNum++) {
                int idx = 0;
                Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());
                outputRow[idx++] = data.fieldNames[fieldNum];

                String dataType = "String";
                StringEvaluator evaluator = data.evaluators.get(fieldNum);
                StringEvaluationResult result = evaluator.getAdvicedResult();
                List evaluationResults = evaluator.getStringEvaluationResults();

                ValueMetaInterface conversionMeta = result.getConversionMeta();
                if (!evaluationResults.isEmpty()) {
                    dataType = conversionMeta.getTypeDesc();
                }

                outputRow[idx++] = dataType;
                outputRow[idx++] = conversionMeta.getConversionMask();
                outputRow[idx++] = Long.valueOf(conversionMeta.getLength());
                outputRow[idx++] = Long.valueOf(conversionMeta.getPrecision());
                outputRow[idx++] = conversionMeta.getCurrencySymbol();
                outputRow[idx++] = conversionMeta.getDecimalSymbol();
                outputRow[idx++] = conversionMeta.getGroupingSymbol();
                outputRow[idx++] = ValueMeta.getTrimTypeDesc(conversionMeta.getTrimType());
                outputRow[idx++] = result.getMin() != null? result.getMin().toString(): null;
                outputRow[idx++] = result.getMax() != null? result.getMax().toString(): null;
                outputRow[idx++] = Long.valueOf(result.getNrNull());

                putRow(data.outputRowMeta, outputRow);
            }

            setOutputDone();
            return false;
        }

        // Generate a new id number for the current row.
        data.rownum += 1;

        // evaluate each field for each row
        for (fieldNum = 0; fieldNum < data.numFields; fieldNum++) {
            StringEvaluator evaluator = data.evaluators.get(fieldNum);
            evaluator.evaluateString(data.inputRowMeta.getString(r, fieldNum));
        }

        return true;
    }
}
