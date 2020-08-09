package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

/**
 * @author byy
 * Rudimentary class for SDT processing
 * Represents Content Control properties
 */
public class XWPFSDTPr {

    private CTSdtPr sdtPr;

    public XWPFSDTPr(CTSdtPr pr) {
        if (pr == null) {
            return;
        }
        this.sdtPr = pr;
    }

    public CTSdtPr getSdtPr() {
        return sdtPr;
    }

    public void setSdtPr(CTSdtPr sdtPr) {
        this.sdtPr = sdtPr;
    }

    /**
     * @return first SDT Title
     */
    public String getTitle() {
        return sdtPr.getAliasList().size() > 0
                ? sdtPr.getAliasList().get(0).getVal()
                : null;
    }

    /**
     * @todo check is it good
     * @param title
     */
    public void setTitle(String title) {
        if (sdtPr.getAliasList().size() == 0) {
            CTString cttag = CTString.Factory.newInstance();
            cttag.setVal(title);
            sdtPr.getAliasList().add(cttag);
        } else {
            sdtPr.getAliasList().get(0).setVal(title);
        }
    }

    /**
     * @return first SDT Tag
     */
    public String getTag() {
        return sdtPr.getTagList().size() > 0
                ? sdtPr.getTagList().get(0).getVal()
                : null;
    }

    /**
     * @author byy
     * @todo check is it good
     * @param tag
     */
    public void setTag(String tag) {
        if (sdtPr.getTagList().size() == 0) {
            CTString cttag = CTString.Factory.newInstance();
            cttag.setVal(tag);
            sdtPr.getTagList().add(cttag);
        } else {
            sdtPr.getTagList().get(0).setVal(tag);
        }
    }

    public CTLock getLock() {
        return sdtPr.getLockList().size() > 0 ? sdtPr.getLockList().get(0) : null;
    }

    public void setLock(XWPFSDTLock.Enum lock) {
        STLock.Enum stLockEnum = XWPFSDTLock.locks.get(lock);
        if (sdtPr.getLockList().size() == 0) {
            CTLock ctLock = CTLock.Factory.newInstance();
            ctLock.setVal(stLockEnum);
            sdtPr.getLockList().add(ctLock);
        } else {
            sdtPr.getLockList().get(0).setVal(stLockEnum);
        }
    }
}
