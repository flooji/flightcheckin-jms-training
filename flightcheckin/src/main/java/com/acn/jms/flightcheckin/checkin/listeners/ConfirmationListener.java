package com.acn.jms.flightcheckin.checkin.listeners;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;



public class ConfirmationListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println(textMessage.getJMSCorrelationID()+ ": "+ textMessage.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}