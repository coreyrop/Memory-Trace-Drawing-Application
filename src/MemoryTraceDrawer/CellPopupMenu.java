package MemoryTraceDrawer;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellPopupMenu extends JPopupMenu
{
    private Object cell;
    private mxCell mxcell;
    private mxGraphSubClass graph;
    private Frame frame;

    /*
        CellPopupMenu class constructor
        Creates rename and delete item options
        @ param mxGraphSubClass _graph: the graph this CellPopupMenu is associated with
        @ param Object _cell: the cell that this CellPopupMenu manages
        @ param Frame _frame: the Frame instance this CellPopupMenu is associated with
     */
    public CellPopupMenu(mxGraphSubClass _graph, Object _cell, Frame _frame)
    {
        cell = _cell;
        mxcell = (mxCell) cell;
        graph = _graph;
        frame = _frame;

        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem renameItem = new JMenuItem("Rename");
        deleteItem.addActionListener(new DeleteActionListener());
        renameItem.addActionListener(new RenameActionListener());
        super.add(deleteItem);
        super.addSeparator();
        super.add(renameItem);
    }

    /*
        Handles the event of the delete option being selected
        Deletes the cell from the graph and from the appropriate HashMap if needed
     */
    private class DeleteActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            graph.getModel().beginUpdate();
            try
            {
                mxICell parent = mxcell.getParent();
                MemoryStructure structure = frame.convertCell(parent);

                // case where cell is a box in the Stack or Heap
                if (structure != null)
                {
                    //structure.removeBox(cell);
                    structure.shiftBoxesUp(mxcell.getGeometry().getY());
                }
                // cell is not a box, might be an edge or it might be a component
                else
                {
                    // check if cell is a component
                    mxICell elder = parent.getParent();
                    structure = frame.convertCell(elder);
                    if (structure != null)
                    {
                        // remove this component from the box's list of components
                        //structure.getBoxes().get(parent).remove(cell);
                    }
                }
                graph.removeCells(new Object[]{cell});
                Frame.projectChanged = true;
            }
            finally
            {
                graph.getModel().endUpdate();
            }
        }
    }

    /*
        Handles the event of the rename option being selected
        Prompts the user to input a new label
        Assigns the new label to the cell and displays it
     */
    private class RenameActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            String label = JOptionPane.showInputDialog(null, "Enter New Label");
            if (label != null)
            {
                graph.getModel().beginUpdate();
                try
                {
                    graph.getModel().setValue(cell, label);

                    // if the cell is a component we update the width to fit with the new label
                    if (mxcell.getStyle().equals(Styles.getReferenceStyle()) || mxcell.getStyle().equals(Styles.getPrimitiveStyle()))
                    {
                        mxGeometry geometry = mxcell.getGeometry();
                        mxRectangle rect = mxUtils.getLabelSize(label, Styles.getPlaceHolder(), false, mxConstants.LINE_HEIGHT);
                        double textWidth = (rect.getWidth() + 5);
                        geometry.setWidth(textWidth);
                    }
                    Frame.projectChanged = true;
                }
                finally
                {
                    graph.getModel().endUpdate();
                }
            }
        }
    }
}
