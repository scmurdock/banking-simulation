package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.*;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BankingSimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static String[] atmLocations = {"Alabama", "France", "New Mexico", "Georgia", "India", "Australia", "China", "Mexico", "Canada", "New Zealand", "Indonesia", "Thailand", "Phillipines", "Uganda", "Ghana", "Nigeria", "Argentina", "Chile", "Togo", "Ivory Coast", "DR Congo", "South Africa", "Brazil", "Ukraine", "Jordan", "United Arab Emirates", "Egypt", "Afghanistan", "Syria", "Iraq", "Italy"};
    private static Integer[] atmLocationIds = {398920909,230929000,240402443,430930954,290230232,400303443,845985494,230930920,549845945,243834877,50606959,443834834,34343434,234959880,23438598,52094390,389498439,65688665,55454544,878897665,783409438,23982398,434904334,2023902309,409349834,309249430,23092092,345045945,239092309,0132230320,655488954};
    private static List<Customer> testCustomers = new ArrayList<Customer>();
    private static Random random = new Random();
    private static Gson gson = new Gson();
    private static boolean simulationActive = false;


    public static synchronized void generateTestCustomers(int numberOfUsers) {

        testCustomers.clear();
        int nextCustomerAge = 55;
        for (int i = 0; i < numberOfUsers - 1; i++) {
            try {
                Customer customer = new Customer();
                String firstName = firstNames[random.nextInt(numberOfUsers)];
                String lastName = lastNames[random.nextInt(numberOfUsers)];
                customer.setCustomerName(firstName + " " + lastName);
                customer.setEmail(firstName + "." + lastName + "@test.com");
                customer.setPhone(String.valueOf(random.nextInt(9)+"015551212"));
                customer.setAccountNumber(String.valueOf(String.valueOf(random.nextInt(999999999))));
                customer.setBirthDay((2020-nextCustomerAge++)+"-01-01");//spread age out evenly
                customer.setHomeLocationId(atmLocationIds[random.nextInt(30)]);

                PrivacyOptInRecord privacyOptInRecord = new PrivacyOptInRecord();
                privacyOptInRecord.setShareCurrentLocation(random.nextBoolean());
                privacyOptInRecord.setShareEmailWithThirdParties(random.nextBoolean());
                privacyOptInRecord.setShareHomeBranchLocation(random.nextBoolean());
                privacyOptInRecord.setSharePhoneNumberWithMarketing(random.nextBoolean());
                privacyOptInRecord.setSocialBankingAppProfilePublic(random.nextBoolean());

                customer.setPrivacyOptIn(privacyOptInRecord);

                CreateNewCustomer.createCustomer(customer);
                KafkaTopicMessage customerMessage = new KafkaTopicMessage();
                customerMessage.setTopic("bank-customers");
                customerMessage.setKey(customer.getAccountNumber());
                customerMessage.setMessage(gson.toJson(customer));
                MessageIntake.route(customerMessage);
                testCustomers.add(customer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void createBalanceUpdates(){
        for (Customer testCustomer:testCustomers){
            try {
                KafkaTopicMessage balanceMessage = new KafkaTopicMessage();
                balanceMessage.setTopic("balance-updates");
                balanceMessage.setKey(testCustomer.getAccountNumber());
                balanceMessage.setMessage(String.valueOf(random.nextInt(100000)) + "." + String.valueOf(random.nextInt(99)));
                MessageIntake.route(balanceMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending balance update for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }

    public static void createATMVisits(){
        for (Customer testCustomer:testCustomers){
            try {
                KafkaTopicMessage atmMessage = new KafkaTopicMessage();
                atmMessage.setTopic("atm-visits");
                atmMessage.setKey(testCustomer.getAccountNumber());
                atmMessage.setMessage(String.valueOf(testCustomer.getHomeLocationId()));
                MessageIntake.route(atmMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending atm visit for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }


    public static void createPrivacyUpdates(){//change opt ins for customer including share current location
        for (Customer testCustomer:testCustomers){
            try {
                testCustomer.getPrivacyOptIn().setShareCurrentLocation(random.nextBoolean());
                testCustomer.getPrivacyOptIn().setShareEmailWithThirdParties(random.nextBoolean());
                testCustomer.getPrivacyOptIn().setShareHomeBranchLocation(random.nextBoolean());
                testCustomer.getPrivacyOptIn().setSharePhoneNumberWithMarketing(random.nextBoolean());
                testCustomer.getPrivacyOptIn().setSocialBankingAppProfilePublic(random.nextBoolean());
                KafkaTopicMessage privacyMessage = new KafkaTopicMessage();
                privacyMessage.setTopic("privacy-updates");
                privacyMessage.setKey(testCustomer.getAccountNumber());
                privacyMessage.setMessage(gson.toJson(testCustomer.getPrivacyOptIn()));
                MessageIntake.route(privacyMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending atm visit for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }


    public static void createDeposits(){
        for (Customer testCustomer:testCustomers){
            try {

                Deposit deposit = new Deposit();
                deposit.setAmount(Float.valueOf(String.valueOf(random.nextInt(1000)) + "." + String.valueOf(random.nextInt(99))));
                deposit.setAccountNumber(testCustomer.getAccountNumber());
                deposit.setDateAndTime(new Date());
                KafkaTopicMessage depositMessage = new KafkaTopicMessage();
                depositMessage.setTopic("bank-deposits");
                depositMessage.setKey(testCustomer.getAccountNumber());
                depositMessage.setMessage(gson.toJson(deposit));
                MessageIntake.route(depositMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending deposit for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }

    public static void createATMWithdrawals(){
        for (Customer testCustomer:testCustomers){
            try {
                Long transactionId = System.currentTimeMillis();
                ATMTransaction atmTransaction = new ATMTransaction();
                atmTransaction.setTransactionId(transactionId);
                atmTransaction.setTransactionDate(new Date(transactionId));//this works because the transaction id is the milliseconds of the current time/date
                atmTransaction.setAtmLocationId(testCustomer.getHomeLocationId());

                if (testCustomer.getCustomerName().startsWith("A") || testCustomer.getCustomerName().startsWith("J")) {//suspicious activity
                    atmTransaction.setAtmLocationId(atmLocationIds[random.nextInt(30)]);
                } else{
                    atmTransaction.setAtmLocationId(testCustomer.getHomeLocationId());
                }

                CustomerLocation customerLocation = new CustomerLocation();
                customerLocation.setAccountNumber(testCustomer.getAccountNumber());
                if (testCustomer.getCustomerName().startsWith("A") || testCustomer.getCustomerName().startsWith("J")) {//traveling outside of home area
                    customerLocation.setCurrentLocationId(atmLocationIds[random.nextInt(30)]);
                } else{
                    customerLocation.setCurrentLocationId(testCustomer.getHomeLocationId());
                }

               // JedisData.loadToJedis(customerLocation, CustomerLocation.class);//should create Redis events with customer location

                KafkaTopicMessage customerCurrentLocationMessage = new KafkaTopicMessage();//customer opted in to share their location on their device to prevent fraud
                customerCurrentLocationMessage.setTopic("customer-current-location");
                customerCurrentLocationMessage.setKey(testCustomer.getAccountNumber());
                customerCurrentLocationMessage.setMessage(gson.toJson(customerLocation));
                MessageIntake.route(customerCurrentLocationMessage);

                KafkaTopicMessage atmwithdrawalMessage = new KafkaTopicMessage();
                atmwithdrawalMessage.setTopic("atm-withdrawals");
                atmwithdrawalMessage.setKey(testCustomer.getAccountNumber());
                atmwithdrawalMessage.setMessage(gson.toJson(atmTransaction));
                MessageIntake.route(atmwithdrawalMessage);

                Withdrawal withdrawal = new Withdrawal();
                withdrawal.setTransactionId(transactionId);
                withdrawal.setAmount(Float.valueOf(String.valueOf(random.nextInt(1000)) + "." + String.valueOf(random.nextInt(99))));
                withdrawal.setDateAndTime(new Date(transactionId));//this works because the transaction id is the milliseconds of the current time/date
                withdrawal.setAccountNumber(testCustomer.getAccountNumber());

                KafkaTopicMessage withdrawalMessage = new KafkaTopicMessage();
                withdrawalMessage.setTopic("bank-withdrawals");
                withdrawalMessage.setKey(testCustomer.getAccountNumber());
                withdrawalMessage.setMessage(gson.toJson(withdrawal));
                MessageIntake.route(withdrawalMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending withdrawal for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }
}
