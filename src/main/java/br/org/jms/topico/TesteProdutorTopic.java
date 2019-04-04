package br.org.jms.topico;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteProdutorTopic {

	public static void main(String[] args) throws Exception {

				InitialContext context = new InitialContext();

				ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
				Connection connection = factory.createConnection();
				connection.start();
		       
				 
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);				
			
				Destination topico  = (Destination) context.lookup("loja");
				MessageProducer producer = session.createProducer(topico);
				
				for (int i = 60; i < 70; i++) {
					Message message = session.createTextMessage("<pedido><id>00"+i+"</id></pedido>");
			        producer.send(message);
					
				}    
		        
		        session.close();
		        connection.close();
		        context.close();

	}

}
