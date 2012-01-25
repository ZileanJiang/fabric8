/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.fabric.demo.activemq;

import org.fusesource.mq.fabric.JMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class ProducerThread extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerThread.class);

    int messageCount = 1000;
    String dest;
    protected JMSService service;
    int sleep = 0;
    int sentCount = 0;

    public ProducerThread(JMSService service, String dest) {
        this.dest = dest;
        this.service = service;
    }

    public void run() {
        MessageProducer producer = null;
        try {
            producer = service.createProducer(dest);
            for (sentCount = 0; sentCount < messageCount; sentCount++) {
                producer.send(createMessage(sentCount));
                LOG.info("Sent 'test message: " + sentCount + "'");
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
        LOG.info("Producer thread finished");
    }

    protected Message createMessage(int i) throws Exception {
        return service.createTextMessage("test message: " + i);
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getSentCount() {
        return sentCount;
    }
}
