package org.pentaho.di.ui.trans.steps.streammeta;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.streammeta.StreamMetaMeta;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * Created by puls3 on 08/04/15.
 */
public class StreamMetaDialog extends BaseStepDialog implements StepDialogInterface {

    private static Class<?> PKG = StreamMetaMeta.class; // for i18n purposes, needed by Translator2!!

    private StreamMetaMeta input;

    public StreamMetaDialog( Shell parent, Object in, TransMeta tr, String sname ) {
        super( parent, (BaseStepMeta) in, tr, sname );
        input = (StreamMetaMeta) in;
    }

    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
        props.setLook( shell );
        setShellImage( shell, input );

        ModifyListener lsMod = new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                input.setChanged();
            }
        };
        changed = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout( formLayout );
        shell.setText( BaseMessages.getString(PKG, "StreamMetaDialog.Shell.Title") );

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label( shell, SWT.RIGHT );
        wlStepname.setText( BaseMessages.getString( PKG, "StreamMetaDialog.Stepname.Label" ) );
        props.setLook( wlStepname );
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment( 0, 0 );
        fdlStepname.right = new FormAttachment( middle, -margin );
        fdlStepname.top = new FormAttachment( 0, margin );
        wlStepname.setLayoutData( fdlStepname );
        wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        wStepname.setText( stepname );
        props.setLook( wStepname );
        wStepname.addModifyListener( lsMod );
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment( middle, 0 );
        fdStepname.top = new FormAttachment( 0, margin );
        fdStepname.right = new FormAttachment( 100, 0 );
        wStepname.setLayoutData( fdStepname );

        // Some buttons
        wOK = new Button( shell, SWT.PUSH );
        wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
        wCancel = new Button( shell, SWT.PUSH );
        wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

        setButtonPositions( new Button[] { wOK, wCancel }, margin, wStepname );

        // Add listeners
        lsCancel = new Listener() {
            public void handleEvent( Event e ) {
                cancel();
            }
        };
        lsOK = new Listener() {
            public void handleEvent( Event e ) {
                ok();
            }
        };

        wCancel.addListener( SWT.Selection, lsCancel );
        wOK.addListener( SWT.Selection, lsOK );

        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected( SelectionEvent e ) {
                ok();
            }
        };

        wStepname.addSelectionListener( lsDef );

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener( new ShellAdapter() {
            public void shellClosed( ShellEvent e ) {
                cancel();
            }
        } );

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged( changed );

        shell.open();
        while ( !shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
        return stepname;
    }

    /**
     * Copy information from the meta-data input to the dialog fields.
     */
    public void getData() {
        wStepname.selectAll();
        wStepname.setFocus();
    }

    private void cancel() {
        stepname = null;
        input.setChanged( changed );
        dispose();
    }

    private void ok() {
        if ( Const.isEmpty( wStepname.getText() ) ) {
            return;
        }

        stepname = wStepname.getText(); // return value

        dispose();
    }
}
