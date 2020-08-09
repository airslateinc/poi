package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.*;

/**
 * The following test class should meet the next requirements:
 * - A client can create SDTRUN &
 */
public final class TestXWPFSDTRun {

    public void saveToFile(XWPFDocument doc, String fileName) throws IOException {
        try {
            doc.write(new FileOutputStream(new File(String.format("%s.docx", fileName))));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }

    @Test
    public void testCreateRunInsideSdtContent() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        XWPFSDTRun sdt = p.createSdtRun();

        XWPFSDTContentRun sdtContent = sdt.createSdtContent();
        XWPFRun run = sdtContent.createRun();
        run.setText("text in SDT");

        assertEquals(1, sdt.getContent().getIRuns().size());
        assertEquals("text in SDT", ((XWPFRun) sdt.getContent().getIRuns().get(0)).getText(0));
    }

    @Test
    public void testInsertSDTRunWithRunBetweenRunsInParagraph() throws IOException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        p.createRun().setText("first ");
        XWPFRun run = p.createRun();
        run.setText("second ");
        run.setFontFamily("Times New Roman");
        run.setFontSize(40);
        p.createRun().setText("third ");

        // get position of run & insert sdt element before
        XWPFSDTRun sdtRun = p.insertNewSDTRunBeforeRun(run);

        // create PR & Content for SDT
        XWPFSDTPr sdtPr = sdtRun.createSdtPr();
        XWPFSDTContentRun sdtContent = sdtRun.createSdtContent();

        sdtPr.setTag("new-inline-tag");
        sdtPr.setTitle("new-inline-title");
        sdtPr.setLock(XWPFSDTLock.Enum.SDT_CONTENT_LOCKED);

        // copy existing run to sdt content & remove run from Paragraph
        sdtContent.createCopyOfExistingRunToSdtContent(run);
        p.removeRun(p.getRuns().indexOf(run));

        System.out.println(doc.getDocument().getBody().toString());

        assertEquals(3, p.getIRuns().size());
        assertEquals(2, p.getRuns().size());
        assertEquals(XWPFSDTRun.class, p.getIRuns().get(1).getClass());
        assertEquals("second ", ((XWPFSDTRun) p.getIRuns().get(1)).getContent().getText());
    }

    /**
     * Test SdtPr get/set tag, title, lock for SdtRun
     * @throws Exception
     */
    @Test
    public void testSdtPrForRunExisting() throws Exception {
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
