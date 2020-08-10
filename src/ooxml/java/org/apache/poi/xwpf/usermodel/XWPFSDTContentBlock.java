package org.apache.poi.xwpf.usermodel;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XWPFSDTContentBlock implements ISDTContent {

    // private final XWPFDocument document;
    // private List<XWPFSDT> contentControls = new ArrayList<>();
    private IBody parent;
    private List<IBodyElement> bodyElements = new ArrayList<>();
    private List<XWPFParagraph> paragraphs = new ArrayList<>();
    private List<XWPFTable> tables = new ArrayList<>();

    public XWPFSDTContentBlock(CTSdtContentBlock block, IBody part) {
        if (block == null) {
            return;
        }
        XmlCursor cursor = block.newCursor();
        cursor.selectPath("./*");
        while (cursor.toNextSelection()) {
            XmlObject o = cursor.getObject();
            if (o instanceof CTP) {
                XWPFParagraph p = new XWPFParagraph((CTP) o, part);
                bodyElements.add(p);
                paragraphs.add(p);
            } else if (o instanceof CTTbl) {
                XWPFTable t = new XWPFTable((CTTbl) o, part);
                bodyElements.add(t);
                tables.add(t);
            } else if (o instanceof CTSdtBlock) {
                XWPFSDTBlock c = new XWPFSDTBlock(((CTSdtBlock) o), part);
                bodyElements.add(c);
//                contentControls.add(c);
            }
        }
        cursor.dispose();
    }

//    @Override
    public List<IBodyElement> getBodyElements() {
        return Collections.unmodifiableList(bodyElements);
    }

//    @Override
    public List<XWPFParagraph> getParagraphs() {
        return Collections.unmodifiableList(paragraphs);
    }

//    @Override
    public List<XWPFTable> getTables() {
        return Collections.unmodifiableList(tables);
    }

    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();
        boolean addNewLine = false;
        for (int i = 0; i < bodyElements.size(); i++) {
            Object o = bodyElements.get(i);
            if (o instanceof XWPFParagraph) {
                appendParagraph((XWPFParagraph) o, text);
                addNewLine = true;
            } else if (o instanceof XWPFTable) {
                appendTable((XWPFTable) o, text);
                addNewLine = true;
            } else if (o instanceof XWPFSDTBlock) {
                text.append(((XWPFSDTBlock) o).getContent().getText());
                addNewLine = true;
            }
            if (addNewLine && i < bodyElements.size() - 1) {
                text.append("\n");
            }
        }
        return text.toString();
    }

    private void appendTable(XWPFTable table, StringBuilder text) {
        //this works recursively to pull embedded tables from within cells
        for (XWPFTableRow row : table.getRows()) {
            List<ICell> cells = row.getTableICells();
            for (int i = 0; i < cells.size(); i++) {
                ICell cell = cells.get(i);
                if (cell instanceof XWPFTableCell) {
                    text.append(((XWPFTableCell) cell).getTextRecursively());
                } else if (cell instanceof XWPFSDTCell) {
                    text.append(((XWPFSDTCell) cell).getContent().getText());
                }
                if (i < cells.size() - 1) {
                    text.append("\t");
                }
            }
            text.append('\n');
        }
    }

    private void appendParagraph(XWPFParagraph paragraph, StringBuilder text) {
        for (IRunElement run : paragraph.getRuns()) {
            text.append(run);
        }
    }

    @Override
    public String toString() {
        return getText();
    }
}
