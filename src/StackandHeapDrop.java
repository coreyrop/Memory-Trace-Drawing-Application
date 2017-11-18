import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

public class StackandHeapDrop implements DropTargetListener {
    private static final String ADD_BOX = "Add Box";
    private static final String ADD_PRIMITIVE = "Add Primitive";

    private JPanel panel;
    private Color defaultColor;
    public StackandHeapDrop(JPanel _panel)
    {
        panel = _panel;
        defaultColor = panel.getBackground();
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) { }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        Transferable data = dtde.getTransferable();
        try {
            if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_BOX)) {
                panel.setBackground(new Color(0.0f, 0.9f, 0.0f, 0.5f));
            } else {
                dtde.rejectDrag();
            }
        } catch(Exception e)
        {
            // This "can never happen"
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) { }

    @Override
    public void dragExit(DropTargetEvent dte) {panel.setBackground(defaultColor);}

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        Transferable data = dtde.getTransferable();

        try
        {
            if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_BOX))
            {
                BoxPanel newPanel = BoxPanel.makeBox(panel);
                if (newPanel != null) {
                    panel.add(newPanel);
                    panel.revalidate();
                }
            }
            else if (data.getTransferData(data.getTransferDataFlavors()[0]).equals(ADD_PRIMITIVE))
            {
                JOptionPane.showMessageDialog(null, "Can't store primitives on Stack or Heap alone");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Can't store references on Stack or Heap alone");
            }
        } catch(Exception e)
        {
            // This "can never happen"
        }
        panel.setBackground(defaultColor);
    }
}