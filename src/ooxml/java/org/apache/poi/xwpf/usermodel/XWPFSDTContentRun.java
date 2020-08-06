package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.util.ArrayList;
import java.util.List;

public class XWPFSDTContentRun implements ISDTContent {
    // private final IBody part;
    // private final XWPFDocument document;

    private List<XWPFRun> runs = new ArrayList<>();
    private List<XWPFSDTRun> contentControls = new ArrayList<>();
    private List<IRunElement> runElements = new ArrayList<>();

    public XWPFSDTContentRun(CTSdtContentRun sdtRun, IRunBody parent) {
        if (sdtRun == null) {
            return;
        }
        for (CTR ctr : sdtRun.getRArray()) {
            XWPFRun run = new XWPFRun(ctr, parent);
            // runs.add(run);
            runElements.add(run);
        }
    }

    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();
        boolean addNewLine = false;
        for (int i = 0; i < runElements.size(); i++) {
            Object o = runElements.get(i);
            if (o instanceof XWPFSDTRun) {
                text.append(((XWPFSDTRun) o).getContent().getText());
                addNewLine = true;
            } else if (o instanceof XWPFRun) {
                text.append(o);
                addNewLine = false;
            }
            if (addNewLine && i < runElements.size() - 1) {
                text.append("\n");
            }
        }
        return text.toString();
    }

    @Override
    public String toString() {
        return getText();
    }
}
