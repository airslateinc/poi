package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TestXWPFSDTBlock {

    @Test
    public void testCreateSdtFromScratch() {
        XWPFDocument doc = new XWPFDocument();

        XWPFParagraph p1 = doc.createParagraph();
        XWPFRun r1 = p1.createRun();
        r1.setText("First paragraph text");

        XWPFSDTBlock block1 = doc.createSdt();


        XWPFParagraph p2 = doc.createParagraph();
        XWPFRun r2 = p2.createRun();
        r2.setText("Second paragraph text");

        XWPFParagraph p3 = doc.createParagraph();
        XWPFRun r3 = p3.createRun();
        r3.setText("Third paragraph text");

        System.out.println(doc.getDocument().getBody().toString());
    }

    public void testElementsStructureInsideSdtBlock() throws Exception {
        // todo
    }

    /**
     * Test SdtPr get/set tag, title, lock for SdtBlock
     * @throws Exception
     */
    @Test
    public void testSdtPrForBlock() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");

        XWPFAbstractSDT sdtBlock = (XWPFAbstractSDT) doc.getBodyElements().get(2);

        assertTrue(sdtBlock instanceof XWPFSDTBlock);

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
    }
}
