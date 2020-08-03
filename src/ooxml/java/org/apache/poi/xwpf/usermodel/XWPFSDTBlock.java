package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;

public class XWPFSDTBlock extends XWPFAbstractSDT
        implements IBodyElement, IRunBody, ISDTContents, IRunElement {

    private CTSdtBlock sdtBlock;
    private ISDTContent content;


    public XWPFSDTBlock(IBody part) {
        super(part);
        this.sdtBlock = CTSdtBlock.Factory.newInstance();
        this.content = new XWPFSDTContent(sdtBlock.getSdtContent(), part, this);
    }

    public XWPFSDTBlock(CTSdtBlock sdtBlock, IBody part) {
        super(new XWPFSDTPr(sdtBlock.getSdtPr()), part);
        this.sdtBlock = sdtBlock;
        this.content = new XWPFSDTContent(sdtBlock.getSdtContent(), part, this);
    }

    @Override
    public ISDTContent getContent() {
        return content;
    }
}
