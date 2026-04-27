package com.codeexplainer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ExplainAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        Project project = event.getData(CommonDataKeys.PROJECT);
        if (project == null) return;
        //make text area pop up when using plug-in
        ToolWindowManager.getInstance(project).getToolWindow("CodeExplainer").show();
        Document document = editor.getDocument();
        String code = document.getText();
        CodeExplainerPanel.instance.explain(code);
    }
}
