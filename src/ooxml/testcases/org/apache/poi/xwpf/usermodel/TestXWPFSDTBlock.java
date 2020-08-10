package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.*;

/**
 * Test class for manipulation of block level Content Controls.
 * Related classes are:
 *      {@link XWPFSDTBlock}, {@link XWPFSDTPr}, {@link XWPFSDTContentBlock}
 */
public final class TestXWPFSDTBlock {

    /**
     * Verify that Sdt Block Pr is added to Sdt Block
     * and the related object references were updated
     */
    @Test
    public void testCreateSdtBlockPr() {
        XWPFDocument doc = new XWPFDocument();
        XWPFSDTBlock sdtBlock = doc.createSdt();

        XmlCursor cur = doc.getDocument().newCursor();
        cur.toFirstChild(); // move cursor to Body
        cur.toFirstChild(); // move cursor to SDT
        assertTrue(cur.getObject() instanceof CTSdtBlock);

        XWPFSDTPr sdtBlockPr = sdtBlock.createSdtPr();

        cur.toFirstChild();
        assertTrue(cur.getObject() instanceof CTSdtPr);
    }

    /**
     * Verify that Sdt Block Content is added to Sdt Block
     * and the related object references were updated
     */
    @Test
    public void testCreateSdtContentBlock() {
        XWPFDocument doc = new XWPFDocument();
        XWPFSDTBlock sdtBlock = doc.createSdt();

        XmlCursor cur = doc.getDocument().newCursor();
        cur.toFirstChild(); // move cursor to Body
        cur.toFirstChild(); // move cursor to SDT
        assertTrue(cur.getObject() instanceof CTSdtBlock);

        XWPFSDTContentBlock sdtBlockContent = sdtBlock.createSdtContent();

        cur.toFirstChild();
        assertTrue(cur.getObject() instanceof CTSdtContentBlock);
    }

    @Test
    public void testGetParagraphFromSdtBlockContent() throws IOException {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");
        XWPFSDTBlock sdtBlock = (XWPFSDTBlock) doc.getBodyElements().get(2);

        CTP p = sdtBlock.getContent().getParagraphs().get(0).getCTP();
        assertSame(
                sdtBlock.getContent().getParagraphs().get(0),
                sdtBlock.getContent().getParagraph(p)
        );
    }

    @Test
    public void testInsertNewParagraphToSdtBlockContent() throws IOException {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");
        XWPFSDTBlock sdtBlock = (XWPFSDTBlock) doc.getBodyElements().get(2);

        XmlCursor cur = sdtBlock.getContent().getCtSdtContentBlock().newCursor();
        cur.toFirstChild(); // move cursor to Tbl
        cur.toEndToken(); // move cursor to the end of Tbl
        cur.toNextToken(); // move cursor fight after the Tbl

        assertEquals(1, sdtBlock.getContent().getParagraphs().size());

        XWPFParagraph newP = sdtBlock.getContent().insertNewParagraph(cur);

        assertEquals(2, sdtBlock.getContent().getParagraphs().size());
        assertEquals(3, sdtBlock.getContent().getBodyElements().size());
        assertSame(newP, sdtBlock.getContent().getParagraphs().get(0));
    }

    /**
     * Verify that existing Content Control in document is correctly
     * unmarshalled & we can proceed with modifying its content
     * @throws Exception
     */
    @Test
    public void testUnmarshallingSdtBlock() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");
        XWPFSDTBlock sdtBlock = (XWPFSDTBlock) doc.getBodyElements().get(2);

        // Tag
        assertEquals("block-sdt-tag", sdtBlock.getSdtPr().getTag());

        sdtBlock.getSdtPr().setTag("new-block-tag");
        assertEquals("new-block-tag", sdtBlock.getSdtPr().getTag());

        // Title
        assertEquals("block-sdt-title", sdtBlock.getSdtPr().getTitle());

        sdtBlock.getSdtPr().setTitle("new-block-title");
        assertEquals("new-block-title", sdtBlock.getSdtPr().getTitle());

        // Lock
        assertEquals(XWPFSDTLock.locks.get(XWPFSDTLock.Enum.SDT_CONTENT_LOCKED), sdtBlock.getSdtPr().getLock().getVal());

        sdtBlock.getSdtPr().setLock(XWPFSDTLock.Enum.UNLOCKED);
        assertEquals(XWPFSDTLock.locks.get(XWPFSDTLock.Enum.UNLOCKED), sdtBlock.getSdtPr().getLock().getVal());

        // SdtContent
        assertEquals(
                "Some content1",
                sdtBlock.getContent()
                        .getTables()
                        .get(0)
                        .getRows()
                        .get(0)
                        .getCell(0)
                        .getText()
        );
    }
}
