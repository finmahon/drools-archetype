/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package drools.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.drools.core.time.SessionPseudoClock;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;

public class Rule1Test {
    static final Logger LOG = LoggerFactory.getLogger(RuleTest.class);

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        LOG.info("Creating kieBase");
        KieBase kieBase = kContainer.getKieBase();

        LOG.info("There should be rules: ");
        for ( KiePackage kp : kieBase.getKiePackages() ) {
            for (Rule rule : kp.getRules()) {
                LOG.info("kp " + kp + " rule " + rule.getName());
            }
        }

        List<Command<?>> commands = new ArrayList<>(); 
        KieCommands commandFactory = kieServices.getCommands(); 
        commands.add( commandFactory.newSetGlobal( "controlSet", new ArrayList<String>(), true ) );
        commands.add(commandFactory.newInsert(new Measurement("color", "red")));
        commands.add(commandFactory.newFireAllRules());
        commands.add(commandFactory.newGetObjects("objects"));
        commands.add(commandFactory.newDispose());

        BatchExecutionCommand batchExecutionCommand = commandFactory.newBatchExecution(commands);

        KieSession ksession = kieBase.newKieSession();

        ExecutionResults results = ksession.execute( batchExecutionCommand );
        // results.getValue( "list1" ); // returns the ArrayList
        // results.getValue( "person" ); // returns the inserted fact Person
        // results.getValue( "Get People" );// returns the query as a QueryResults instance.
        System.out.println(results.getValue("objects"));
        LOG.info("qqqqq22222 {}", results.getValue("objects"));




        // ServiceResponse<ExecutionResults> response = rulesClient.executeCommandsWithResults(CONTAINER_ID, batchExecutionCommand);
        // ServiceResponse<ExecutionResults> response = rulesClient.executeCommandsWithResults(CONTAINER_ID, batchExecutionCommand);
        // ExecutionResults results = response.getResult();
        // System.out.println(results.getValue("objects"));
        // LOG.info("qqqqq {}", results.getValue("objects"));




        // LOG.info("Creating kieSession");
        // KieSession session = kieBase.newKieSession();

        // LOG.info("Populating globals");
        // List<String> check = new ArrayList<String>();
        // session.setGlobal("controlSet", check);
        // LOG.info("initial checks to string {}", check);

        // LOG.info("Now running data");

        // Measurement mRed= new Measurement("color", "red");
        // session.insert(mRed);
        // // session.fireAllRules();

        // Measurement mGreen= new Measurement("color", "green");
        // session.insert(mGreen);
        // // session.fireAllRules();

        // Measurement mBlue= new Measurement("color", "blue");
        // session.insert(mBlue);

        // Measurement mBlueq= new Measurement("color", "blue");
        // session.insert(mBlueq);
        // session.fireAllRules();

        // LOG.info("Final checks");

        // LOG.info("Final checks to string {}", check);

        // if (session != null) {
        //     session.dispose();
        // }

        // assertEquals("Size of object in Working Memory is 3", 4, session.getObjects().size());
        // assertTrue("contains red", check.contains("red"));
        // assertTrue("contains green", check.contains("green"));
        // assertTrue("contains blue", check.contains("blue"));

    }
}