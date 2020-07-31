package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLock;

/**
 * @author byy
 * Rudimentary class for SDT processing
 * Represents Content Control properties
 */
public class XWPFSDTPr {

    private CTSdtPr pr;
    private IBody part;

    public XWPFSDTPr() {
        this.pr = CTSdtPr.Factory.newInstance();
    }

    public XWPFSDTPr(CTSdtPr pr) {
        this.pr = pr;
    }

    /**
     * @return first SDT Title
     */
    public String getTitle() {
        CTString[] aliases = pr.getAliasArray();
        if (aliases != null && aliases.length > 0) {
            return aliases[0].getVal();
        }
        return "";
    }

    /**
     * @author byy
     * @todo check is it good
     * @param title
     */
    public void setTitle(String title) {
        if (pr != null) {
            if (pr.getTagList().size() == 0) {
                CTString cttag = CTString.Factory.newInstance();
                cttag.setVal(title);
                pr.getAliasList().add(cttag);
            } else {
                pr.getAliasList().get(0).setVal(title);
            }
        }
    }

    /**
     * @return first SDT Tag
     */
    public String getTag() {
        CTString[] tags = pr.getTagArray();
        if (tags != null && tags.length > 0) {
            return tags[0].getVal();
        }
        return "";
    }

    /**
     * @author byy
     * @todo check is it good
     * @param tag
     */
    public void setTag(String tag) {
        if (pr != null) {
            if (pr.getTagList().size() == 0) {
                CTString cttag = CTString.Factory.newInstance();
                cttag.setVal(tag);
                pr.getTagList().add(cttag);
            } else {
                pr.getTagList().get(0).setVal(tag);
            }
        }
    }

    public CTLock getLock() {
        return pr.getLockList().size() > 0 ? pr.getLockList().get(0) : null;
    }

    public void setLock(XWPFSDTLock.Enum lock) {
        STLock.Enum stLockEnum = XWPFSDTLock.locks.get(lock);
        if (pr != null) {
            if (pr.getLockList().size() == 0) {
                CTLock ctLock = CTLock.Factory.newInstance();
                ctLock.setVal(stLockEnum);
                pr.getLockList().add(ctLock);
            } else {
                pr.getLockList().get(0).setVal(stLockEnum);
            }
        }
    }
}
