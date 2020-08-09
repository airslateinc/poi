package org.apache.poi.xwpf.usermodel;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

import java.util.List;

/**
 * @TODO check interfaces, update methods
 */
public class XWPFSDTBlock extends XWPFAbstractSDT
        implements IBodyElement, IRunBody, ISDTContentsBlock, IBody {

    private CTSdtBlock sdtBlock;
    private ISDTContent content;
    private IBody part;

    protected List<XWPFParagraph> paragraphs;
    protected List<XWPFTable> tables;
    protected List<IBodyElement> bodyElements;

    public XWPFSDTBlock(CTSdtBlock sdtBlock, IBody part) {
        super(sdtBlock.getSdtPr());
        this.sdtBlock = sdtBlock;
        this.part = part;
        this.content = new XWPFSDTContentBlock(sdtBlock.getSdtContent(), part);
    }

    @Override
    public ISDTContent getContent() {
        return content;
    }

    /**
     * @return null
     */
    public IBody getBody() {
        return null;
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

    @Override
    public List<IBodyElement> getBodyElements() {
        return null;
    }

    @Override
    public List<XWPFParagraph> getParagraphs() {
        return null;
    }

    @Override
    public List<XWPFTable> getTables() {
        return null;
    }

    @Override
    public XWPFParagraph getParagraph(CTP p) {
        return null;
    }

    @Override
    public XWPFTable getTable(CTTbl ctTable) {
        return null;
    }

    @Override
    public XWPFParagraph getParagraphArray(int pos) {
        return null;
    }

    @Override
    public XWPFTable getTableArray(int pos) {
        return null;
    }

    @Override
    public XWPFParagraph insertNewParagraph(XmlCursor cursor) {
        return null;
    }

    @Override
    public XWPFTable insertNewTbl(XmlCursor cursor) {
        return null;
    }

    @Override
    public void insertTable(int pos, XWPFTable table) {

    }

    @Override
    public XWPFTableCell getTableCell(CTTc cell) {
        return null;
    }

    @Override
    public XWPFDocument getXWPFDocument() {
        return null;
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
