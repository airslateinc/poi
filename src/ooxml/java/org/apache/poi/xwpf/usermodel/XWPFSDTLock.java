package org.apache.poi.xwpf.usermodel;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLock;

import java.util.HashMap;

public class XWPFSDTLock {

    public static HashMap<Enum, STLock.Enum> locks;

    public enum Enum {
        SDT_LOCKED, CONTENT_LOCKED, UNLOCKED, SDT_CONTENT_LOCKED
    }

    static {
        locks = new HashMap<>();
        locks.put(Enum.SDT_LOCKED, STLock.Enum.forInt(STLock.INT_SDT_LOCKED));
        locks.put(Enum.CONTENT_LOCKED, STLock.Enum.forInt(STLock.INT_CONTENT_LOCKED));
        locks.put(Enum.UNLOCKED, STLock.Enum.forInt(STLock.INT_UNLOCKED));
        locks.put(Enum.SDT_CONTENT_LOCKED, STLock.Enum.forInt(STLock.INT_SDT_CONTENT_LOCKED));
    }
}
