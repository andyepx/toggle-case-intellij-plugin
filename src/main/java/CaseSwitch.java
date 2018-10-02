import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

public class CaseSwitch extends AnAction {

    public CaseSwitch() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent e) {

        //Get all the required data from data keys
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getProject();

        //Access document, caret, and selection
        final Document document = editor.getDocument();
        final SelectionModel selectionModel = editor.getSelectionModel();

        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        final String s = document.getText(TextRange.from(start, end));

        String to = s;
        if (hasUpperCase(s)) {
            to = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, s);
        } else if (s.indexOf("-") > 0) {
            to = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, s);
        }

        String finalTo = to;
        WriteCommandAction.runWriteCommandAction(project, () -> document.replaceString(start, end, finalTo));

        selectionModel.setSelection(start, start + to.length());
    }

    private static boolean hasUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }
}