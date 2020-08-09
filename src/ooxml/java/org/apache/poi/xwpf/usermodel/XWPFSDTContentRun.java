package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XWPFSDTContentRun implements ISDTContent {

    private IRunBody parent;
    private CTSdtContentRun ctContentRun;
    private List<XWPFRun> runs = new ArrayList<>();
    private List<IRunElement> iruns = new ArrayList<>();

    public XWPFSDTContentRun(CTSdtContentRun ctContentRun, IRunBody parent) {
        if (ctContentRun == null) {
            return;
        }
        this.ctContentRun = ctContentRun;
        this.parent = parent;

        for (CTR ctr : this.ctContentRun.getRList()) {
            XWPFRun run = new XWPFRun(ctr, parent);
            runs.add(run);
//            bodyElements.add(run);
        }
    }

    public List<IRunElement> getIRuns() {
        return Collections.unmodifiableList(iruns);
    }

    public void createCopyOfExistingRunToSdtContent(XWPFRun run) {
        CTR ctr = ctContentRun.addNewR();
        ctr.set(run.getCTR());
        XWPFRun xwpfRun = new XWPFRun(ctr, parent);
        iruns.add(xwpfRun);
    }

    /**
     * Appends a new run to SDT content
     *
     * @return a new text run
     */
    public XWPFRun createRun() {
        XWPFRun xwpfRun = new XWPFRun(ctContentRun.addNewR(), parent);
        runs.add(xwpfRun);
        iruns.add(xwpfRun);
        return xwpfRun;
    }

    @Override
    public String getText() {
        StringBuilder text = new StringBuilder();
        boolean addNewLine = false;
        for (int i = 0; i < iruns.size(); i++) {
            Object o = iruns.get(i);
            if (o instanceof XWPFSDTRun) {
                text.append(((XWPFSDTRun) o).getContent().getText());
                addNewLine = true;
            } else if (o instanceof XWPFRun) {
                text.append(o);
                addNewLine = false;
            }
            if (addNewLine && i < iruns.size() - 1) {
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
