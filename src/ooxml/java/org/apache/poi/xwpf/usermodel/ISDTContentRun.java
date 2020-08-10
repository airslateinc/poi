package org.apache.poi.xwpf.usermodel;

public interface ISDTContentRun {

    /**
     * Appends a new run to SDT content
     *
     * @return a new text run
     */
    XWPFRun createRun();

    XWPFRun copyAndInsertExistingRun(XWPFRun run);
}
