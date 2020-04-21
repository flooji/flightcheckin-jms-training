package com.acn.jms.flightcheckin.checkin;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.acn.jms.flightcheckin.checkin.listeners.ConfirmationListener;
import com.acn.jms.flightcheckin.model.Passenger;

public class CheckInApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();) {

			JMSProducer producer = jmsContext.createProducer();
			
			List<Passenger> passengerGroup = new ArrayList<Passenger>();
			passengerGroup.add(new Passenger(1L,"Mary","Poppins","mary@gmail.com","0752321925"));
			passengerGroup.add(new Passenger(2L,"Olaf","Omani","oo@gmail.com","0752321924"));
			passengerGroup.add(new Passenger(3L,"Amanda","Asborn","lal@gmail.com","0762312127"));
			passengerGroup.add(new Passenger(4L,"Lit","Lot","lit@gmail.com","0772322923"));
			passengerGroup.add(new Passenger(5L,"Remus","Rays","remus@gmail.com","0752321929"));
			passengerGroup.add(new Passenger(6L,"Walter","McBob","mcbob@gmail.com","0752321926"));
			
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			objectMessage.setJMSReplyTo(replyQueue);
			
			for (Passenger passenger : passengerGroup) {
				objectMessage.setObject(passenger);
				producer.send(requestQueue, objectMessage);
				System.out.println("Sending "+objectMessage.getJMSMessageID()+" "+passenger.getFirstName());
			}			
			
			JMSConsumer consumer = jmsContext.createConsumer(replyQueue);
			consumer.setMessageListener(new ConfirmationListener());

			Thread.sleep(12000);
		}
	}
}
