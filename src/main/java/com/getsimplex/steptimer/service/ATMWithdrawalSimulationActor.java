package com.getsimplex.steptimer.service;

import akka.actor.UntypedActor;
import com.getsimplex.steptimer.model.ContinueBankingSimulation;


import java.util.logging.Logger;

/**
 * Created by sean on 8/16/2016.
 */
public class ATMWithdrawalSimulationActor extends UntypedActor {
    private static Logger logger = Logger.getLogger(com.getsimplex.steptimer.service.BalanceSimulationActor.class.getName());
    private static boolean stop = false;

    public void onReceive(Object object){

        if (object instanceof ContinueBankingSimulation){
            try {
                BankingSimulationDataDriver.createATMWithdrawals();
                self().tell(new ContinueBankingSimulation(), self());//continue simulation
            } catch (Exception e){
                logger.severe(e.getMessage());
            }
        }

    }
}
