package br.org.jms.fila;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteConsumidor {

	public static void main(String[] args) throws NamingException, JMSException {
		//InitialContext que por sua vez se baseia no arquivo de configuração jndi.properties:
		InitialContext context = new InitialContext();

		// imports do package javax.jms
		//fábrica não é criada e sim é feito um lookup dela:
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//Connection connection = factory.createConnection("user", "senha");
		//Inicializar conexao
		connection.start();
       
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		 //confirmar o recebimento da mensagem Session.SESSION_TRANSACTED
		//Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//Trabalhando com Session commit e rollback
		final Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
		
		// lugar concreto onde a mensagem será salvo temporariamente.
		Destination file = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(file);
		//receive ler apenas uma messagem 
		//Message message = consumer.receive();
		// System.out.println("Recebendo a msg: " + message);
		//Consumer delegue o tratamento das mensagens para um objeto da interface MessageListener
		consumer.setMessageListener(new MessageListener() {
			
			//recebe a mensagem e a faz algum processamento com ela
			public void onMessage(Message message) {
				TextMessage textMessage  = (TextMessage)message;
		        try {
		        	//indicar que recebemos a mensagem precisamos confirmar 
		        	//message.acknowledge(); // fazendo programaticamente
					
					//session.commit();	
		        	session.rollback();
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					//session.rollback();
					e.printStackTrace();
					
				}
				
			}
		});
		
		new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

		session.close();
		connection.close();
		context.close();

	}

}
