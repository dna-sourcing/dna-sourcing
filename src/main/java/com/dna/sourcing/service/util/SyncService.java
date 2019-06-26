package com.dna.sourcing.service.util;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.dna.sourcing.dao.Event;
import com.dna.sourcing.mapper.EventMapper;
import com.dna.sourcing.mapper.ddo.ActionDnaidMapper;
import com.dna.sourcing.util.ThreadUtil;
import com.github.ontio.network.exception.ConnectorException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

@Service
public class SyncService {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(SyncService.class);

    //
    private EventMapper eventMapper;
    private ActionDnaidMapper actionDnaidMapper;
    //
    private ChainService chainService;

    @Autowired
    public SyncService(EventMapper eventMapper,
                       ActionDnaidMapper actionDnaidMapper,
                       ChainService chainService) {
        //
        this.eventMapper = eventMapper;
        this.actionDnaidMapper = actionDnaidMapper;
        //
        this.chainService = chainService;
    }

    public void confirmTx(String txhash) {

        ThreadUtil.getInstance().submit(new Runnable() {

            @Override
            public void run() {

                for (int retry = 0; retry < 5; retry++) {
                    try {
                        Thread.sleep(6 * 1000);
                        Object event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        while (event == null || StringUtils.isEmpty(event)) {
                            // sdk.addAttributes(ontId,password,key,valueType,value);
                            Thread.sleep(6 * 1000);
                            event = chainService.ontSdk.getConnect().getSmartCodeEvent(txhash);
                        }
                        // 获取交易所在的区块高度
                        int height = chainService.ontSdk.getConnect().getBlockHeightByTxHash(txhash);
                        //
                        String eventStr = JSON.toJSONString(event);
                        Event record = new Event();
                        record.setTxhash(txhash);
                        record.setEvent(eventStr);
                        record.setHeight(height);
                        record.setCreateTime(new Date());
                        eventMapper.insertSelective(record);
                        //
                        break;
                    } catch (ConnectorException | IOException e) {
                        //
                        // logger.error(e.getMessage());
                        logger.error(e.getMessage());

                        //
                        chainService.switchOntSdk();
                        //
                        continue;
                    } catch (Exception e) {
                        //
                        logger.error(e.getMessage());
                        //
                        break;
                    }
                }
            }
        });

    }
}
