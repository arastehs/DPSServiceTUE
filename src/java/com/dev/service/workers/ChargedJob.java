package com.dev.service.workers;


import com.dev.common.exceptions.CardNotFoundException;
import com.dev.model.dao.ChargedRecordProducerDao;

import java.sql.SQLException;
import java.util.logging.Logger;

public class ChargedJob extends Job {

    private static final Logger logger = Logger.getLogger(ChargedJob.class.toString());

    @Override
    public Object call() throws CardNotFoundException, SQLException {
        ChargedRecordProducerDao chargedRecord = new ChargedRecordProducerDao();

        return  chargedRecord.chargedAcknowledged(id);

    }
}
