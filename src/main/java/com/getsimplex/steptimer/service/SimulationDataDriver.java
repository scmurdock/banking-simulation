package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.DeviceMessage;
import com.getsimplex.steptimer.model.RapidStepTest;
import com.getsimplex.steptimer.model.StediEvent;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;

public class SimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static List<Customer> testCustomers = new ArrayList<Customer>();
    private static Random random = new Random();
    private static Gson gson = new Gson();
    private static Session remoteSession;
    private static boolean simulationActive = false;
    private static Map<String, Long> mostRecentTestTime = new HashMap<String, Long>();
    private static boolean solutionActive = false;

    static {
        solutionActive = Boolean.valueOf(System.getProperty("solutionActive"));//this makes it like the Spark app is sending messages (only for teacher use)
    }

    public static synchronized void setSimulationActive(boolean active){
        simulationActive= active;
    }

    public static synchronized  boolean getSimulationActive(){
        return simulationActive;
    }

    public static synchronized void setRemoteSession(Session session){
        remoteSession=session;
    }

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
                customer.setPhone("8015551212");
                customer.setBirthDay((2020-nextCustomerAge++)+"-01-01");//spread age out evenly
                CreateNewCustomer.createCustomer(customer);
                testCustomers.add(customer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
