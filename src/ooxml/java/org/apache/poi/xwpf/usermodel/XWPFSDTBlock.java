package org.apache.poi.xwpf.usermodel;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @TODO check interfaces, update methods
 */
public class XWPFSDTBlock extends XWPFAbstractSDT
        implements IBodyElement, IRunBody, ISDTContentsBlock {

    private CTSdtBlock sdtBlock;
    private XWPFSDTContentBlock contentBlock;
    private IBody part;

//    protected List<XWPFParagraph> paragraphs;
//    protected List<XWPFTable> tables;
//    protected List<IBodyElement> bodyElements;

    public XWPFSDTBlock(CTSdtBlock sdtBlock, IBody part) {
        super(sdtBlock.getSdtPr());
        this.contentBlock = new XWPFSDTContentBlock(sdtBlock.getSdtContent(), part);
        this.sdtBlock = sdtBlock;
        this.part = part;
    }

    @Override
    public XWPFSDTContentBlock getContent() {
        return contentBlock;
    }

    public XWPFSDTContentBlock createSdtContent() {
        XWPFSDTContentBlock xwpfsdtContentBlock = new XWPFSDTContentBlock(this.sdtBlock.addNewSdtContent(), part);
        this.contentBlock = xwpfsdtContentBlock;
        return xwpfsdtContentBlock;
    }

    public XWPFSDTPr createSdtPr() {
        XWPFSDTPr xwpfsdtPr = new XWPFSDTPr(this.sdtBlock.addNewSdtPr());
        this.sdtPr = xwpfsdtPr;
        return xwpfsdtPr;
    }
    /**
     * @return null
     */
    public IBody getBody() {
        return part;
    }

    /**
     * @return document part
     */
    public POIXMLDocumentPart getPart() {
        return part.getPart();
    }

    /**
     * @return partType
     */
    public BodyType getPartType() {
        return BodyType.CONTENTCONTROL;
    }

    /**
     * @return element type
     */
    public BodyElementType getElementType() {
        return BodyElementType.CONTENTCONTROL;
    }

    public XWPFDocument getDocument() {
        return part.getXWPFDocument();
    }
}
