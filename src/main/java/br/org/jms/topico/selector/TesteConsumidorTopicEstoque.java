package br.org.jms.topico.selector;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteConsumidorTopicEstoque {

	public static void main(String[] args) throws Exception {

		InitialContext context = new InitialContext();

		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		//Connection connection = factory.createConnection();
		Connection connection = factory.createConnection("user", "senha");
		//identificar a conexão
		connection.setClientID("estoque"); 
		connection.start();
		//Isso permite deserializar qualquer objeto de uma classe dos pacote
		//System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
		//Caso queira permitir todos os pacotes, coloque:
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
	    
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		

		Topic topico = (Topic) context.lookup("loja");
		//assina o topic
		//MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector", "ebook is null OR ebook=false", false);
			
		consumer.setMessageListener(new MessageListener() {

			public void onMessage(Message message) {
				
				//TextMessage textMessage  = (TextMessage)message;
				
				 ObjectMessage objctMessage = (ObjectMessage) message;
				
		        try {
					//System.out.println(textMessage.getText());
		        	System.out.println(objctMessage.getObject());
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});

		new Scanner(System.in).nextLine();

		session.close();
		connection.close();
		context.close();

	}

}
