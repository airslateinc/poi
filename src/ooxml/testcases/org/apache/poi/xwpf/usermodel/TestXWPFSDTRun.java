package org.apache.poi.xwpf.usermodel;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The following test class should meet the next requirements:
 * - A client can create SDTRUN &
 */
public final class TestXWPFSDTRun {

    @Test
    public void testInsertSDTRunWithRunBetweenRunsInParagraph() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        p.createRun().setText("first ");
        XWPFRun run = p.createRun();
        run.setText("second ");
        p.createRun().setText("third ");

        XWPFSDTRun sdtRun = p.insertNewSDTRunBeforeRun(run);
        sdtRun.copyExistingRunToContent(run);
        p.removeRun(p.getRuns().indexOf(run));

//        System.out.println(doc.getDocument().getBody().toString());

        assertEquals(XWPFSDTRun.class, p.getIRuns().get(1).getClass());
        assertEquals("second ", ((XWPFSDTRun) p.getIRuns().get(1)).getText());
        /*
        <p xmlns="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
          <r>
            <t xml:space="preserve">first</t>
          </r>
          <sdt>
            <sdtContent>
              <r>
                <t xml:space="preserve">second</t>
              </r>
            </sdtContent>
          </sdt>
          <r>
            <t xml:space="preserve">third</t>
          </r>
        </p>
         */
    }

    @Test
    public void testInsertSDTRunBeforeRun() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        p.createRun().setText("First ");
        XWPFRun run = p.createRun();
        run.setText("second ");
        p.createRun().setText("third ");

        // verify that second element is Run
        assertEquals(XWPFRun.class, p.getIRuns().get(1).getClass());
        assertEquals(3, p.getIRuns().size());

        p.insertNewSDTRunBeforeRun(run);
        System.out.println(doc.getDocument().getBody().toString());
//        assertEquals(4, p.getIRuns().size());
//        assertEquals(XWPFSDTRun.class, p.getIRuns().get(1).getClass());
//        System.out.println(doc.getDocument().getBody().toString());
    }

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
    public void testWrapRunInSDTRunInsideParagraph() {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();

        p.createRun().setText("First ");

        XWPFRun run = p.createRun();
        run.setText("second ");
        run.setFontFamily("Times New Roman");
        run.setFontSize(40);

        p.createRun().setText("third ");

        // verify that second element is Run
        assertEquals(XWPFRun.class, p.getIRuns().get(1).getClass());


        XWPFSDTRun sdtRun = p.replaceRunWithSdtRun(run);
        System.err.println(doc.getDocument().getBody().toString());


        // verify that second was wrapped by SDT & there is Run inside SDT
        assertEquals(XWPFSDTRun.class, p.getIRuns().get(1).getClass());

        XWPFSDTRun sdtRun1 = (XWPFSDTRun) p.getIRuns().get(1);
        assertEquals(XWPFRun.class, sdtRun1.getRunElements().get(0).getClass());
        assertEquals("second ", ((XWPFRun) sdtRun1.getRunElements().get(0)).getText(0));

//        try {
//            doc.write(new FileOutputStream(new File("out_sdtrun.docx")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
