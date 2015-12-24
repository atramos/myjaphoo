package org.myjaphoo.gui.thumbtable.thumbcache;

import org.myjaphoo.gui.ThumbTypeDisplayMode;

/**
* LoadMsg
*
* @author mla
* @version $Id$
*/
class LoadMsg {
    long id;
    int column;
    boolean center;
    Integer size;
    ThumbTypeDisplayMode mode;
    ThumbIsLoadedCallback loadCallBack;
    ThumbLoadActor loadActorToUse;

    LoadMsg(long id, int column, boolean center, Integer size, ThumbTypeDisplayMode mode, ThumbIsLoadedCallback loadCallBack, ThumbLoadActor loadActorToUse) {
        this.id = id;
        this.column = column;
        this.center = center;
        this.size = size;
        this.mode = mode;
        this.loadCallBack = loadCallBack;
        this.loadActorToUse = loadActorToUse;
    }
}
