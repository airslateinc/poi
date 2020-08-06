package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public final class TestXWPFSDTRun {

    @Test
    public void testCreateSdtRunInParagraph() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        p.createRun().setText("Sample run ");

        XWPFSDTRun sdtRun = p.createSdtRun();
        sdtRun.getContent().getText();

        Object target1 = ((XWPFParagraph) doc.getBodyElements().get(0)).getIRuns().get(1);
        assertEquals(XWPFSDTRun.class, target1.getClass());
        assertEquals("sdtRunWrapped", target1);
    }

    @Test
    public void testWrapExistingRunInSDTRun() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();

        p.createRun().setText("First ");
        XWPFRun run = p.createRun();
        run.setText("second ");
        p.createRun().setText("third ");

        XWPFSDTRun sdtRun = p.createSdtRun();
        System.err.println(doc.getDocument().getBody().toString());

        try {
            doc.write(new FileOutputStream(new File("out_sdtrun.docx")));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        sdtRun.addExistingRun()
//        p.wrapRunInSDTRun()
    }

    @Test
    public void testElementsStructureInsideSdtRun() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");
        System.err.println(doc.getDocument().getBody().toString());
        System.out.println("Hello");
    }

    /**
     * Test SdtPr get/set tag, title, lock for SdtRun
     * @throws Exception
     */
    @Test
    public void testSdtPrForRun() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");

        XWPFParagraph paragraph = doc.getParagraphArray(0);
        XWPFAbstractSDT sdtRun = (XWPFAbstractSDT) paragraph.getIRuns().get(1);

        assertTrue(sdtRun instanceof XWPFSDTRun);

        // Tag
        assertEquals("inline-sdt-tag", sdtRun.getSdtPr().getTag());

        sdtRun.getSdtPr().setTag("new-inline-tag");
        assertEquals("new-inline-tag", sdtRun.getSdtPr().getTag());

        // Title
        assertEquals("inline-sdt-title", sdtRun.getSdtPr().getTitle());

        sdtRun.getSdtPr().setTitle("new-inline-title");
        assertEquals("new-inline-title", sdtRun.getSdtPr().getTitle());

        // Lock
        assertEquals(XWPFSDTLock.locks.get(XWPFSDTLock.Enum.SDT_CONTENT_LOCKED), sdtRun.getSdtPr().getLock().getVal());

        sdtRun.getSdtPr().setLock(XWPFSDTLock.Enum.SDT_LOCKED);
        assertEquals(XWPFSDTLock.locks.get(XWPFSDTLock.Enum.SDT_LOCKED), sdtRun.getSdtPr().getLock().getVal());
    }
}
