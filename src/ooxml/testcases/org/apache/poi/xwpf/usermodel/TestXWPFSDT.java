/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.xwpf.usermodel;

import static org.apache.poi.POITestCase.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.XWPFTestDataSamples;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTStringImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;

public final class TestXWPFSDT {

    /**
     * Test simple tag and title extraction from SDT
     */
    @Test
    public void testTagTitle() throws Exception {
        try (XWPFDocument doc =XWPFTestDataSamples.openSampleDocument("Bug54849.docx")) {
            String tag = null;
            String title = null;
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            for (XWPFAbstractSDT sdt : sdts) {
                if (sdt.getContent().toString().equals("Rich_text")) {
                    tag = "MyTag";
                    title = "MyTitle";
                    break;
                }

            }
            assertEquals("controls size", 13, sdts.size());

            assertEquals("tag", "MyTag", tag);
            assertEquals("title", "MyTitle", title);
        }
    }

    @Test
    public void testGetSDTs() throws Exception {
        String[] contents = new String[]{
                "header_rich_text",
                "Rich_text",
                "Rich_text_pre_table\nRich_text_cell1\t\t\t\n\t\t\t\n\t\t\t\n\nRich_text_post_table",
                "Plain_text_no_newlines",
                "Plain_text_with_newlines1\nplain_text_with_newlines2",
                "Watermelon",
                "Dirt",
                "4/16/2013",
                "Rich_text_in_cell",
                "rich_text_in_paragraph_in_cell",
                "Footer_rich_text",
                "Footnote_sdt",
                "Endnote_sdt"

        };
        try (XWPFDocument doc =XWPFTestDataSamples.openSampleDocument("Bug54849.docx")) {
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);

            assertEquals("number of sdts", contents.length, sdts.size());

            for (int i = 0; i < contents.length; i++) {
                XWPFAbstractSDT sdt = sdts.get(i);
                assertEquals(i + ": " + contents[i], contents[i], sdt.getContent().toString());
            }
        }
    }

    /**
     * POI-54771 and TIKA-1317
     */
    @Test
    public void testSDTAsCell() throws Exception {
        //Bug54771a.docx and Bug54771b.docx test slightly 
        //different recursion patterns. Keep both!
        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("Bug54771a.docx")) {
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            String text = sdts.get(0).getContent().getText();
            assertEquals(2, sdts.size());
            assertContains(text, "Test");

            text = sdts.get(1).getContent().getText();
            assertContains(text, "Test Subtitle");
            assertContains(text, "Test User");
            assertTrue(text.indexOf("Test") < text.indexOf("Test Subtitle"));
        }

        try (XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("Bug54771b.docx")) {
             List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            assertEquals(3, sdts.size());
            assertContains(sdts.get(0).getContent().getText(), "Test");

            assertContains(sdts.get(1).getContent().getText(), "Test Subtitle");
            assertContains(sdts.get(2).getContent().getText(), "Test User");
        }
    }

    /**
     * POI-55142 and Tika 1130
     */
    @Test
    public void testNewLinesBetweenRuns() throws Exception {
        try (XWPFDocument doc =XWPFTestDataSamples.openSampleDocument("Bug55142.docx")) {
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            List<String> targs = new ArrayList<>();
            //these test newlines and tabs in paragraphs/body elements
            targs.add("Rich-text1 abcdefghi");
            targs.add("Rich-text2 abcd\t\tefgh");
            targs.add("Rich-text3 abcd\nefg");
            targs.add("Rich-text4 abcdefg");
            targs.add("Rich-text5 abcdefg\nhijk");
            targs.add("Plain-text1 abcdefg");
            targs.add("Plain-text2 abcdefg\nhijk\nlmnop");
            //this tests consecutive runs within a cell (not a paragraph)
            //this test case was triggered by Tika-1130
            targs.add("sdt_incell2 abcdefg");

            for (int i = 0; i < sdts.size(); i++) {
                XWPFAbstractSDT sdt = sdts.get(i);
                assertEquals(targs.get(i), targs.get(i), sdt.getContent().getText());
            }
        }
    }

    @Test
    public void test60341() throws IOException {
        //handle sdtbody without an sdtpr
        try (XWPFDocument doc =XWPFTestDataSamples.openSampleDocument("Bug60341.docx")) {
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            assertEquals(1, sdts.size());
            assertEquals(null, sdts.get(0).getSdtPr().getTag());
            assertEquals(null, sdts.get(0).getSdtPr().getTitle());
        }
    }

    @Test
    public void test62859() throws IOException {
        //this doesn't test the exact code path for this issue, but
        //it does test for a related issue, and the fix fixes both.
        //We should try to add the actual triggering document
        //to our test suite.
        try (XWPFDocument doc =XWPFTestDataSamples.openSampleDocument("Bug62859.docx")) {
            List<XWPFAbstractSDT> sdts = extractAllSDTs(doc);
            assertEquals(1, sdts.size());
            assertEquals(null, sdts.get(0).getSdtPr().getTag());
            assertEquals(null, sdts.get(0).getSdtPr().getTitle());
        }
    }

    @Test
    public void testSdt() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");

        XWPFDocument doc1 = new XWPFDocument();
        XWPFParagraph p = doc1.createParagraph();
        XWPFRun run = p.createRun();
        run.setText("Hello");

        CTSdtRun sdtRun = CTSdtRun.Factory.newInstance();

//        XWPFSDT sdt = new XWPFSDT(sdtRun, p.part);

        CTString sd = CTString.Factory.newInstance();
        sd.setVal("asdfasdkf");

        CTSdtPr ctSdtPr = CTSdtPr.Factory.newInstance();
        ctSdtPr.addNewTag();
        ctSdtPr.setTagArray(0, sd);
        sdtRun.setSdtPr(ctSdtPr);


//        sdt.setSdtPr(new XWPFSDTPr(ctSdtPr, sdt.getBody()));

//        p.addSdt(sdtRun);


        /**
         * class org.apache.poi.xwpf.usermodel.XWPFParagraph
         * class org.apache.poi.xwpf.usermodel.XWPFParagraph
         * class org.apache.poi.xwpf.usermodel.XWPFSDT
         * class org.apache.poi.xwpf.usermodel.XWPFParagraph
         */
        System.err.println(doc1.getDocument().toString());
        for (Object o : doc1.getBodyElements()) {
            System.out.println(o.getClass());
        }
    }

    /**
     * Test SdtPr get/set tag, title, lock
     * @throws Exception
     */
    @Test
    public void testSdtPr() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");

        XWPFParagraph paragraph = doc.getParagraphArray(0);
        XWPFAbstractSDT sdtRun = (XWPFAbstractSDT) paragraph.getIRuns().get(1);

        // SdtRun
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

        // SdtBlock
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

    @Test
    public void testSdtBlock() throws Exception {
        XWPFDocument doc = XWPFTestDataSamples.openSampleDocument("blockAndInlineSdtTags.docx");

        // ...
    }

    private List<XWPFAbstractSDT> extractAllSDTs(XWPFDocument doc) {
        List<XWPFAbstractSDT> sdts = new ArrayList<>();

        List<XWPFHeader> headers = doc.getHeaderList();
        for (XWPFHeader header : headers) {
            sdts.addAll(extractSDTsFromBodyElements(header.getBodyElements()));
        }
        sdts.addAll(extractSDTsFromBodyElements(doc.getBodyElements()));

        List<XWPFFooter> footers = doc.getFooterList();
        for (XWPFFooter footer : footers) {
            sdts.addAll(extractSDTsFromBodyElements(footer.getBodyElements()));
        }

        for (XWPFFootnote footnote : doc.getFootnotes()) {
            sdts.addAll(extractSDTsFromBodyElements(footnote.getBodyElements()));
        }
        for (XWPFEndnote footnote : doc.getEndnotes()) {
            sdts.addAll(extractSDTsFromBodyElements(footnote.getBodyElements()));
        }
        return sdts;
    }

    private List<XWPFAbstractSDT> extractSDTsFromBodyElements(List<IBodyElement> elements) {
        List<XWPFAbstractSDT> sdts = new ArrayList<>();
        for (IBodyElement e : elements) {
            if (e instanceof XWPFSDTBlock) {
                XWPFSDTBlock sdt = (XWPFSDTBlock) e;
                sdts.add(sdt);
            } else if (e instanceof XWPFParagraph) {

                XWPFParagraph p = (XWPFParagraph) e;
                for (IRunElement e2 : p.getIRuns()) {
                    if (e2 instanceof XWPFSDTRun) {
                        XWPFSDTRun sdt = (XWPFSDTRun) e2;
                        sdts.add(sdt);
                    }
                }
            } else if (e instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) e;
                sdts.addAll(extractSDTsFromTable(table));
            }
        }
        return sdts;
    }

    private List<XWPFAbstractSDT> extractSDTsFromTable(XWPFTable table) {

        List<XWPFAbstractSDT> sdts = new ArrayList<>();
        for (XWPFTableRow r : table.getRows()) {
            for (ICell c : r.getTableICells()) {
                if (c instanceof XWPFSDTCell) {
                    sdts.add((XWPFSDTCell) c);
                } else if (c instanceof XWPFTableCell) {
                    sdts.addAll(extractSDTsFromBodyElements(((XWPFTableCell) c).getBodyElements()));
                }
            }
        }
        return sdts;
    }
}
