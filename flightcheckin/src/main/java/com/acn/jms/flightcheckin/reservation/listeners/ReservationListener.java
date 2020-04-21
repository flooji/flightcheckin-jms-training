package com.acn.jms.flightcheckin.reservation.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.acn.jms.flightcheckin.model.Passenger;

public class ReservationListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		ObjectMessage objectMessage = (ObjectMessage) message;
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			TextMessage confirmationMessage = jmsContext.createTextMessage();
			
			Passenger passenger = (Passenger) objectMessage.getObject();
			String messageID = objectMessage.getJMSMessageID();
			System.out.println("Ongoing reservation for: "+ passenger);
			
			confirmationMessage.setText("Reservation successful for "+ passenger);
			confirmationMessage.setJMSCorrelationID(messageID);
			
			JMSProducer producer = jmsContext.createProducer();
			producer.send(objectMessage.getJMSReplyTo(), confirmationMessage);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
