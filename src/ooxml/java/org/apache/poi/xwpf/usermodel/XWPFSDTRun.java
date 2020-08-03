package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTSdtRunImpl;

public class XWPFSDTRun extends XWPFAbstractSDT
        implements IBodyElement, IRunBody, ISDTContents, IRunElement {

    private CTSdtRun sdtRun;
    private ISDTContent content;

    public XWPFSDTRun(IBody part) {
        super(part);
        this.sdtRun = CTSdtRun.Factory.newInstance();
        this.content = new XWPFSDTContent(sdtRun.getSdtContent(), part, this);
    }

    public XWPFSDTRun(CTSdtRun sdtRun, IBody part) {
        super(new XWPFSDTPr(sdtRun.getSdtPr()), part);
        this.sdtRun = sdtRun;
        this.content = new XWPFSDTContent(this.sdtRun.getSdtContent(), part, this);
    }

    @Override
    public ISDTContent getContent() {
        return content;
    }
}
