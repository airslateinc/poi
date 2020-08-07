package org.apache.poi.xwpf.usermodel;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTSdtRunImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XWPFSDTRun extends XWPFAbstractSDT
        implements IRunBody, IRunElement, ISDTContentsRun {

    private CTSdtRun sdtRun;
    private IRunBody parent;
    private CTSdtContentRun contentRun;
//    private XWPFSDTContentRun content;

    /**
     * Experimental constructor
     * @param sdtRun
     */
    public XWPFSDTRun(CTSdtRun sdtRun) {
        super(new XWPFSDTPr(sdtRun.getSdtPr()));
        this.sdtRun = sdtRun;
    }

    public void setParent(IRunBody part) {
        this.parent = part;
    }

    public XWPFSDTRun(CTSdtRun sdtRun, IRunBody part) {
        super(new XWPFSDTPr(sdtRun.getSdtPr()));
        this.sdtRun = sdtRun;
        this.parent = part;
//        this.content = new XWPFSDTContentRun(this.sdtRun.getSdtContent(), part);

        this.sdtRun.setSdtContent(
                sdtRun.getSdtContent() == null
                    ? CTSdtContentRun.Factory.newInstance()
                    : sdtRun.getSdtContent()
        );

        this.contentRun = this.sdtRun.getSdtContent();
//
//        CTR r = this.contentRun.addNewR();
//        XWPFRun xwpfRun = new XWPFRun(r, this);
//        xwpfRun.setText("text in CC");

//        if (sdtRun == null) {
//            return;
//        }
        for (CTR ctr : contentRun.getRArray()) {
            XWPFRun run = new XWPFRun(ctr, this);
//            run.setText();
            // runs.add(run);
            runElements.add(run);
        }
    }

    public void copyExistingRunToContent(XWPFRun run) {
        CTR ctr = this.contentRun.addNewR();
        ctr.set(run.getCTR());
        XWPFRun xwpfRun = new XWPFRun(ctr, this);
        runElements.add(xwpfRun);
    }
    public void addExistingRun(XWPFRun run) {
        CTR ctr = this.contentRun.addNewR();
        ctr.set(run.getCTR());
//        ctr.setRPr(run.getCTR().getRPr());
//        ctr.set(run.getCTR().getRPr());
    }

    @Override
    public ISDTContent getContent() {
        return null;
    }

    @Override
    public XWPFDocument getDocument() {
        return parent.getDocument();
    }

    @Override
    public POIXMLDocumentPart getPart() {
        return parent.getPart();
    }

    /**
     * ===============================================================================
     * Move the code below to {@link XWPFSDTContentRun}
     */

    // private final IBody part;
    // private final XWPFDocument document;
    private List<XWPFRun> runs = new ArrayList<>();
    //    private List<XWPFSDTRun> contentControls = new ArrayList<>();
    private List<IRunElement> runElements = new ArrayList<>();

    public List<IRunElement> getRunElements() {
        return Collections.unmodifiableList(runElements);
    }



//    public XWPFSDTContentRun(CTSdtContentRun sdtRun, IRunBody parent) {
//
//    }

//    @Override
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
