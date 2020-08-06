package org.apache.poi.xwpf.usermodel;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTSdtRunImpl;

public class XWPFSDTRun extends XWPFAbstractSDT
        implements IRunBody, IRunElement, ISDTContentsRun {

    private CTSdtRun sdtRun;
    private ISDTContent content;
    private IRunBody parent;

    public XWPFSDTRun(CTSdtRun sdtRun, IRunBody part) {
        super(new XWPFSDTPr(sdtRun.getSdtPr()));
        this.sdtRun = sdtRun;
        this.parent = part;
        this.content = new XWPFSDTContentRun(this.sdtRun.getSdtContent(), this);
    }

    @Override
    public ISDTContent getContent() {
        return content;
    }

    @Override
    public XWPFDocument getDocument() {
        return parent.getDocument();
    }

    @Override
    public POIXMLDocumentPart getPart() {
        return parent.getPart();
    }
}
