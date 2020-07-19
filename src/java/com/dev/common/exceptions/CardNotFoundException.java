package com.dev.common.exceptions;

import org.apache.log4j.Logger;

public class CardNotFoundException extends Exception{
    private static Logger log = Logger.getLogger(CardNotFoundException.class);
    public CardNotFoundException(String cardId, Short seqNo) {
        log.error("No Card found for this info, CardId: " + cardId + " Seq_no : " + seqNo+ "in tbl: SENT_CHARGE");
    }

}
