package org.apache.poi.xwpf.usermodel;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.util.ArrayList;
import java.util.List;

public class XWPFSDTContentRun implements ISDTContent {
    // private final IBody part;
    // private final XWPFDocument document;
    // private List<XWPFParagraph> paragraphs = new ArrayList<>();
    // private List<XWPFTable> tables = new ArrayList<>();
    // private List<XWPFRun> runs = new ArrayList<>();
    // private List<XWPFSDT> contentControls = new ArrayList<>();
    private List<ISDTContents> bodyElements = new ArrayList<>();

    public XWPFSDTContentRun(CTSdtContentRun sdtRun, IBody part, IRunBody parent) {
        if (sdtRun == null) {
            return;
        }
        for (CTR ctr : sdtRun.getRArray()) {
            XWPFRun run = new XWPFRun(ctr, parent);
            // runs.add(run);
            bodyElements.add(run);
        }
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
            } else if (o instanceof XWPFRun) {
                text.append(o);
                addNewLine = false;
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
