package com.dev.service.workers;

import com.dev.model.dao.QuotaBulkProducerDao;

import java.util.logging.Logger;

public class QuotaJob extends Job {


    private static final Logger logger = Logger.getLogger(QuotaJob.class.toString());

    @Override
    public Object call() throws Exception {
        QuotaBulkProducerDao quotaBulkProducerDao = new QuotaBulkProducerDao();

        return quotaBulkProducerDao.sendDailyBulk(this.getId());

    }
}
