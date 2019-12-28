/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.preferences;

import java.io.File;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.script.ArchiScriptPlugin;


/**
 * Script Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class ScriptPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    public static final String ID = "com.archimatetool.script.preferences.ScriptPreferencePage";  //$NON-NLS-1$
    
    private static final String HELP_ID = "com.archimatetool.script.prefs"; //$NON-NLS-1$
    
    private Text fScriptsFolderTextField;
    private Text fEditorPathTextField;
    
    private Combo fDoubleClickBehaviourCombo;
    
    private String[] DOUBLE_CLICK_BEHAVIOURS = {
            Messages.ScriptPreferencePage_4,
            Messages.ScriptPreferencePage_5,
            Messages.ScriptPreferencePage_6
    };
    
    private Combo fJSCombo;
    
    private String[] JS_VERSIONS = {
            Messages.ScriptPreferencePage_10,
            Messages.ScriptPreferencePage_11,
    };
    
	public ScriptPreferencePage() {
		setPreferenceStore(ArchiScriptPlugin.INSTANCE.getPreferenceStore());
	}
	
    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        Group settingsGroup = new Group(client, SWT.NULL);
        settingsGroup.setText(Messages.ScriptPreferencePage_0);
        settingsGroup.setLayout(new GridLayout(3, false));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = 500;
        settingsGroup.setLayoutData(gd);
        
        // Scripts folder location
        Label label = new Label(settingsGroup, SWT.NULL);
        label.setText(Messages.ScriptPreferencePage_8 + ":"); //$NON-NLS-1$
        
        fScriptsFolderTextField = UIUtils.createSingleTextControl(settingsGroup, SWT.BORDER, false);
        fScriptsFolderTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button folderButton = new Button(settingsGroup, SWT.PUSH);
        folderButton.setText(Messages.ScriptPreferencePage_2);
        folderButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String folderPath = chooseFolderPath();
                if(folderPath != null) {
                    fScriptsFolderTextField.setText(folderPath);
                }
            }
        });

        // Editor path
        label = new Label(settingsGroup, SWT.NULL);
        label.setText(Messages.ScriptPreferencePage_1);
        
        fEditorPathTextField = UIUtils.createSingleTextControl(settingsGroup, SWT.BORDER, false);
        fEditorPathTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button fileButton = new Button(settingsGroup, SWT.PUSH);
        fileButton.setText(Messages.ScriptPreferencePage_2);
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String path = chooseEditor();
                if(path != null) {
                    fEditorPathTextField.setText(path);
                }
            }
        });
        
        // Double-click behaviour
        label = new Label(settingsGroup, SWT.NULL);
        label.setText(Messages.ScriptPreferencePage_7);
        fDoubleClickBehaviourCombo = new Combo(settingsGroup, SWT.READ_ONLY);
        fDoubleClickBehaviourCombo.setItems(DOUBLE_CLICK_BEHAVIOURS);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fDoubleClickBehaviourCombo.setLayoutData(gd);
        
        // JS Versions
        label = new Label(settingsGroup, SWT.NULL);
        label.setText(Messages.ScriptPreferencePage_12);
        fJSCombo = new Combo(settingsGroup, SWT.READ_ONLY);
        fJSCombo.setItems(JS_VERSIONS);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        fJSCombo.setLayoutData(gd);
        
        setValues();
        
        return client;
    }

    private String chooseFolderPath() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        dialog.setText(Messages.ScriptPreferencePage_8);
        dialog.setMessage(Messages.ScriptPreferencePage_9);
        File file = new File(fScriptsFolderTextField.getText());
        if(file.exists()) {
            dialog.setFilterPath(fScriptsFolderTextField.getText());
        }
        return dialog.open();
    }

    private String chooseEditor() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
        dialog.setText(Messages.ScriptPreferencePage_3);
        File file = new File(fEditorPathTextField.getText());
        if(file.exists()) {
            dialog.setFilterPath(fEditorPathTextField.getText());
        }
        return dialog.open();
    }

    private void setValues() {
        fScriptsFolderTextField.setText(getPreferenceStore().getString(PREFS_SCRIPTS_FOLDER));
        fEditorPathTextField.setText(getPreferenceStore().getString(PREFS_EDITOR));
        fDoubleClickBehaviourCombo.select(getPreferenceStore().getInt(PREFS_DOUBLE_CLICK_BEHAVIOUR));
        fJSCombo.select(getPreferenceStore().getInt(PREFS_JS_ENGINE));
    }
    
    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(PREFS_SCRIPTS_FOLDER, fScriptsFolderTextField.getText());
        getPreferenceStore().setValue(PREFS_EDITOR, fEditorPathTextField.getText());
        getPreferenceStore().setValue(PREFS_DOUBLE_CLICK_BEHAVIOUR, fDoubleClickBehaviourCombo.getSelectionIndex());
        getPreferenceStore().setValue(PREFS_JS_ENGINE, fJSCombo.getSelectionIndex());
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fScriptsFolderTextField.setText(getPreferenceStore().getDefaultString(PREFS_SCRIPTS_FOLDER));
        fEditorPathTextField.setText(getPreferenceStore().getDefaultString(PREFS_EDITOR));
        fDoubleClickBehaviourCombo.select(getPreferenceStore().getDefaultInt(PREFS_DOUBLE_CLICK_BEHAVIOUR));
        fJSCombo.select(getPreferenceStore().getDefaultInt(PREFS_JS_ENGINE));
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}