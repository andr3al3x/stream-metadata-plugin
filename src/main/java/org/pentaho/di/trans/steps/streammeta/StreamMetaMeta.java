package org.pentaho.di.trans.steps.streammeta;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.metastore.api.IMetaStore;

import java.util.List;

/**
 * Created by puls3 on 08/04/15.
 */
@Step(
        id = "StreamMetaStep",
        image = "icon.png",
        i18nPackageName = "org.pentaho.di.trans.steps.streammeta",
        name = "StreamMeta.Name.Default",
        description = "StreamMeta.Name.Desc",
        categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Utility"
)
public class StreamMetaMeta extends BaseStepMeta implements StepMetaInterface {
    private static Class<?> PKG = StreamMetaMeta.class;

    public StreamMetaMeta() {
        super();
    }

    @Override
    public void setDefault() {

    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int i, TransMeta transMeta, Trans trans) {
        return new StreamMeta(stepMeta, stepDataInterface, i, transMeta, trans);
    }

    @Override
    public StepDataInterface getStepData() {
        return new StreamMetaData();
    }

    public Object clone() {
        Object retval = super.clone();
        return retval;
    }

    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository, IMetaStore metaStore) {
        CheckResult cr;
        if (prev == null || prev.size() == 0) {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString(
                    PKG, "StreamMeta.CheckResult.NotReceivingFields"), stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(
                    PKG, "StreamMeta.CheckResult.StepRecevingData", prev.size() + ""), stepMeta);
            remarks.add(cr);
        }

        // See if we have input streams leading to this step!
        if (input.length > 0) {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(
                    PKG, "StreamMeta.CheckResult.StepRecevingData2"), stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(
                    PKG, "StreamMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepMeta);
            remarks.add(cr);
        }
    }

    @Override
    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {
        RowMetaInterface fields = new RowMeta();

        ValueMetaInterface columnName = new ValueMeta("column_name", ValueMeta.TYPE_STRING);
        columnName.setLength(500);
        columnName.setPrecision(-1);
        columnName.setOrigin(name);
        fields.addValueMeta(columnName);

        ValueMetaInterface fieldType = new ValueMeta("type", ValueMeta.TYPE_STRING);
        fieldType.setLength(500);
        fieldType.setPrecision(-1);
        fieldType.setOrigin(name);
        fields.addValueMeta(fieldType);

        ValueMetaInterface conversionMask = new ValueMeta("conversion_mask", ValueMeta.TYPE_STRING);
        conversionMask.setLength(500);
        conversionMask.setPrecision(-1);
        conversionMask.setOrigin(name);
        fields.addValueMeta(conversionMask);

        ValueMetaInterface length = new ValueMeta("length", ValueMeta.TYPE_INTEGER);
        length.setOrigin(name);
        fields.addValueMeta(length);

        ValueMetaInterface precision = new ValueMeta("precision", ValueMeta.TYPE_INTEGER);
        precision.setOrigin(name);
        fields.addValueMeta(precision);

        ValueMetaInterface currencySymbol = new ValueMeta("currency_symbol", ValueMeta.TYPE_STRING);
        currencySymbol.setLength(500);
        currencySymbol.setPrecision(-1);
        currencySymbol.setOrigin(name);
        fields.addValueMeta(currencySymbol);

        ValueMetaInterface decimalSymbol = new ValueMeta("decimal_symbol", ValueMeta.TYPE_STRING);
        decimalSymbol.setLength(500);
        decimalSymbol.setPrecision(-1);
        decimalSymbol.setOrigin(name);
        fields.addValueMeta(decimalSymbol);

        ValueMetaInterface groupingSymbol = new ValueMeta("grouping_symbol", ValueMeta.TYPE_STRING);
        groupingSymbol.setLength(500);
        groupingSymbol.setPrecision(-1);
        groupingSymbol.setOrigin(name);
        fields.addValueMeta(groupingSymbol);

        ValueMetaInterface trimType = new ValueMeta("trim_type", ValueMeta.TYPE_STRING);
        trimType.setLength(500);
        trimType.setPrecision(-1);
        trimType.setOrigin(name);
        fields.addValueMeta(trimType);

        ValueMetaInterface minimumValue = new ValueMeta("minimum_value", ValueMeta.TYPE_STRING);
        minimumValue.setLength(500);
        minimumValue.setPrecision(-1);
        minimumValue.setOrigin(name);
        fields.addValueMeta(minimumValue);

        ValueMetaInterface maximumValue = new ValueMeta("maximum_value", ValueMeta.TYPE_STRING);
        maximumValue.setLength(500);
        maximumValue.setPrecision(-1);
        maximumValue.setOrigin(name);
        fields.addValueMeta(maximumValue);

        ValueMetaInterface nullValues = new ValueMeta("null_values", ValueMeta.TYPE_INTEGER);
        nullValues.setOrigin(name);
        fields.addValueMeta(nullValues);

        inputRowMeta.clear();
        inputRowMeta.addRowMeta(fields);
    }
}
