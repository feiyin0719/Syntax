package window;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoWrapper implements UndoableEditListener {
    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private JEditorPane textComponent;

    /**
     * Creates a new instance of UndoWrapper
     */
    public UndoWrapper(JEditorPane aComponent) {
        textComponent = aComponent;
        undoManager = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        textComponent.getDocument().addUndoableEditListener(this);
        textComponent.getInputMap().put((KeyStroke) undoAction.getValue(Action.ACCELERATOR_KEY), "undo");
        textComponent.getInputMap().put((KeyStroke) redoAction.getValue(Action.ACCELERATOR_KEY), "redo");
        textComponent.getActionMap().put("undo", undoAction);
        textComponent.getActionMap().put("redo", redoAction);
    }

    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
        undoAction.updateUndoState();
        redoAction.updateRedoState();
    }

    UndoAction getUndoAction() {
        return undoAction;
    }

    RedoAction getRedoAction() {
        return redoAction;
    }

    /**
     * UndoAction is the Action responsible for handling the undo operation.
     */
    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Cannot undo"); // TODO: I18N
            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Z"));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException cue) {
                // TODO: Use logging?
                cue.printStackTrace(System.err);
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo"); // TODO I18N
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Cannot undo"); // TODO I18N
            }
        }
    }

    /**
     * RedoAction is the Action responsible for handling the redo operation.
     */
    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Cannot redo"); // TODO I18N
            setEnabled(false);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Y"));
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException cre) {
                // TODO: Use logging?
                cre.printStackTrace(System.err);
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo"); // TODO I18N
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Cannot redo"); // TODO I18N
            }
        }
    }
}
